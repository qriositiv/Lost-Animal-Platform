#!/bin/bash

GREEN='\e[1;32m'
YELLOW='\e[1;33m'
RED='\e[1;31m'
ORANGE='\e[1;35m'
CRESET='\e[0m'

ENDPOINT=https://grid5.mif.vu.lt/cloud3/RPC2

# --------------------------------------------------
# OPENNEBULA TEMPLATES AND FUNCTIONS

declare -A TEMPLATE
WEB_TEMPLATE_ID=2485
DB_TEMPLATE_ID=2484
TRANSLATE_TEMPLATE_ID=2648
TEMPLATE_ID_LIST=(2485 2484 2648)
TEMPLATE["$WEB_TEMPLATE_ID,DESC"]="Webserver machine template"
TEMPLATE["$DB_TEMPLATE_ID,DESC"]="Database machine template"
TEMPLATE["$TRANSLATE_TEMPLATE_ID,DESC"]="Libretranslate machine template"

TEMPLATE_DESC=()
for TEMPLATE_ID in "${TEMPLATE_ID_LIST[@]}"; do
    TEMPLATE_DESC+=("${TEMPLATE["$TEMPLATE_ID,DESC"]}")
done

select_template() {
    PS3="Select a template (enter the number): "
    select option in "${TEMPLATE_DESC[@]}"; do
        if [[ "$REPLY" =~ ^[1-3]$ ]]; then
            RESULT=${TEMPLATE_ID_LIST[$REPLY-1]}
            break
        else 
            echo -e "${RED}Invalid selection. Please enter a number between 1 and 2.${CRESET}" >&2
        fi
    done
    
    # Return the selected value
    echo $RESULT
}

ask_if_to_continue() {
    echo "(press 'y' to continue or any other key to exit)"
    read -n 1 -s KEY_PRESS
    if [ "$KEY_PRESS" != 'y' ]; then
        echo -e "${ORANGE}Exiting${CRESET}"
        exit 1
    fi
}

user_authentication() {
    while true; do
        read -p "Enter MIF username: " USERNAME
        read -p "Enter MIF password: " -s PASSWORD
        echo ""
        echo "Trying to authenticate with $USERNAME"
        export ONE_AUTH=$(yes | oneuser login --user $USERNAME --password $PASSWORD --endpoint $ENDPOINT --time 600)
        if echo "$ONE_AUTH" | grep -q "Authentication Token is:"; then
            echo -e "${GREEN}Successfully authenticated${CRESET}"
        else
            echo -e "${RED}Failed to authenticate, please try again${CRESET}"
        fi
        echo "Getting default user password"
        export VM_PASSWORD=$(oneuser show $USERNAME --endpoint $ENDPOINT -x | grep -A100 "<TEMPLATE>" | grep -B100 "</TEMPLATE>" | grep "<PASSWORD>" | sed -n 's/.*<!\[CDATA\[\(.*\)\]\]>.*/\1/p')
        if [ -z "$VM_PASSWORD" ]; then
            echo -e "${RED}Failed to get default user password. Please make sure that you have set PASSWORD attribute in OpenNebula. You can find it in Settings.${CRESET}"
        else
            echo -e "${GREEN}Successfully got default user password${CRESET}"
            break
        fi
    done
}

# --------------------------------------------------
# Get opennebula repo and install opennebula tools

required_packages=("opennebula-tools" "ansible" "sshpass" "python3-psycopg2" "git")
INSTALL_PACKAGES=false
for package in "${required_packages[@]}"; do
    if ! dpkg -l | grep -q $package; then
        echo -e "${PURPLE}Package $package is not installed${CRESET}"
        INSTALL_PACKAGES=true
    fi
done

if [ "$INSTALL_PACKAGES" == true ]; then
    wget -q -O- https://downloads.opennebula.org/repo/repo.key | sudo apt-key add -
    echo "deb [trusted=yes] https://downloads.opennebula.org/repo/5.6/Ubuntu/18.04 stable opennebula" | sudo tee /etc/apt/sources.list.d/opennebula.list
    sudo apt update
    sudo apt install -y ${required_packages[@]}
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully installed required packages${CRESET}"
    else
        echo -e "${RED}Failed to install required packages${CRESET}"
        exit 1
    fi
    ansible-galaxy collection install community.postgresql
fi


# --------------------------------------------------
# Remove old files
rm ~/.ssh/known_hosts hosts vault.yml > /dev/null 2>&1

# --------------------------------------------------
# Generate ssh key and save password with ssh-agent and ssh-add so you don't need to git retype pass every time

echo -e "\nGenerate and add ssh key"
eval `ssh-agent -s`
ssh-keygen -f ~/.ssh/id_rsa
echo "Please enter password for your SSH key: "
ssh-add
echo ""

# --------------------------------------------------
# AUTHENTICATION

read -p "Do you want to use the same account for all vm's? (Y/n): " SAME_ACCOUNT
# Convert to lowercase because it is checked in other places
SAME_ACCOUNT=$(echo $SAME_ACCOUNT | tr '[:upper:]' '[:lower:]')
if [  "$SAME_ACCOUNT" == "" ] || [[ "$SAME_ACCOUNT" == "y" ]]; then
    echo -e "${GREEN}Using the same account for all vm's${CRESET}"
    user_authentication
else
    SAME_ACCOUNT="n"
    echo -e "${GREEN}Using different accounts for vm's${CRESET}"
fi

# --------------------------------------------------
# CREATE VM'S

# Declare an array to store vm id's. Afterwards we declare for each ID its own associative array
VM_ID_LIST=()
declare -A VM_DATA
while true; do
    read -p "Enter VM name: " VM_NAME

    # Choose template
    SELECTED_TEMPLATE_ID=$(select_template)

    if [ "$SAME_ACCOUNT" == "n" ]; then
        user_authentication
    fi

    # Create VM
    RESULT=$(onetemplate instantiate $SELECTED_TEMPLATE_ID --name "$VM_NAME" --endpoint $ENDPOINT)

    # Check if vm was created (If there is an ID, means it has been created)
    VM_ID=$(echo $RESULT |cut -d ' ' -f 3)
    reg='^[0-9]+$'
    if ! [[ "$VM_ID" =~ $reg ]]; then
        echo "$RESULT"
        echo -e "${RED}Could not create "$VM_NAME" for user $USERNAME${CRESET}"
    else
        echo -e "${GREEN}Successfuly created "$VM_NAME" id: $VM_ID${CRESET}"
        VM_ID_LIST+=($VM_ID)
        VM_DATA["$VM_ID,USERNAME"]=$USERNAME
        VM_DATA["$VM_ID,ONE_AUTH"]=$ONE_AUTH
        VM_DATA["$VM_ID,NAME"]=$VM_NAME
        VM_DATA["$VM_ID,VM_PASSWORD"]=$VM_PASSWORD
        VM_DATA["$VM_ID,TEMPLATE_ID"]=$SELECTED_TEMPLATE_ID
    fi

    read -p "Do you want to add more VM's? (y/N): " MORE_VMS
    if [[ "$MORE_VMS" == [Yy] ]]; then
        continue
    else
        break
    fi
done

echo ""

# If no vm's were created, exit
if [ ${#VM_ID_LIST[@]} -eq 0 ]; then
    echo -e "${RED}No VMs were created, exiting${CRESET}"
    exit 1
fi


# --------------------------------------------------
# Wait for vms to start
echo "(Press 'w' to increase wait time or 's' to lower wait time)"
echo "(Press any other key to skip)"
SEC_LEFT=45
while [ $SEC_LEFT -gt 0 ]; do
    echo -ne "${YELLOW}Waiting ${RED}$SEC_LEFT ${YELLOW}seconds for VM to run \r"

    # Wait for 1 second or until user presses a key
    read -t 1 -n 1 KEY_PRESS
    if [ $? -eq 0 ]; then
        if [ "$KEY_PRESS" == 'w' ]; then
            ((SEC_LEFT+=10))
        elif [ "$KEY_PRESS" == 's' ]; then
            ((SEC_LEFT-=10))
        else
            echo -e "\n${YELLOW}Countdown skipped by user."
            break
        fi
    fi

    ((SEC_LEFT--))
done

# --------------------------------------------------
# Download info about vm's, extract info from it and copy ssh key to vm's
# Create vault file

# Need to add -- because the string starts with "---" and messes up printf
printf -- "---\n" > vault.yml

index=0
for VM_ID in "${VM_ID_LIST[@]}"; do
    echo "Retrieving info about ${VM_DATA["$VM_ID,NAME"]}"

    ONE_AUTH="${VM_DATA["$VM_ID,ONE_AUTH"]}" onevm show $VM_ID --endpoint $ENDPOINT > "vm-info.txt"
    VM_DATA["$VM_ID,SSH_CON"]=$(cat vm-info.txt | grep CONNECT\_INFO1| cut -d '=' -f 2 | tr -d '"')
    VM_DATA["$VM_ID,PRIVATE_IP"]=$(cat vm-info.txt | grep PRIVATE\_IP| cut -d '=' -f 2 | tr -d '"')
    VM_DATA["$VM_ID,PUBLIC_IP"]=$(cat vm-info.txt | grep PUBLIC\_IP| cut -d '=' -f 2 | tr -d '"')
    VM_DATA["$VM_ID,PORT_80"]=$(cat vm-info.txt | grep 'TCP_PORT_FORWARDING=' | sed 's/.* \([0-9]\+\):80 .*/\1/')
    VM_DATA["$VM_ID,PORT_8080"]=$(cat vm-info.txt | grep 'TCP_PORT_FORWARDING=' | sed 's/.* \([0-9]\+\):8080 .*/\1/')
    VM_DATA["$VM_ID,PORT_8000"]=$(cat vm-info.txt | grep 'TCP_PORT_FORWARDING=' | sed 's/.* \([0-9]\+\):8000.*/\1/')
    VM_DATA["$VM_ID,PORT_5432"]=$(cat vm-info.txt | grep 'TCP_PORT_FORWARDING=' | sed 's/.* \([0-9]\+\):5432.*/\1/')
    VM_DATA["$VM_ID,SSH"]="${VM_DATA["$VM_ID,USERNAME"]}@${VM_DATA["$VM_ID,PRIVATE_IP"]}"
    echo -e "${GREEN}Successfully retrieved info about ${VM_DATA["$VM_ID,NAME"]}\n\
        SSH Connection: ${VM_DATA["$VM_ID,SSH_CON"]}\n\
        Private ip: ${VM_DATA["$VM_ID,PRIVATE_IP"]}\n\
        Public_ip: ${VM_DATA["$VM_ID,PUBLIC_IP"]}\n\
        [HTTP] Port 80: ${VM_DATA["$VM_ID,PORT_80"]}\n\
        [PSQL] Port 5432: ${VM_DATA["$VM_ID,PORT_5432"]}${CRESET}"

    # Storing needed data from vm's depending on the template WEBSERVER or DATABASE
    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$WEB_TEMPLATE_ID" ]; then
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_pass: \"${VM_DATA["$VM_ID,VM_PASSWORD"]}\"\n" >> vault.yml
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_public_ip: \"${VM_DATA["$VM_ID,PUBLIC_IP"]}\"\n" >> vault.yml
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_private_ip: \"${VM_DATA["$VM_ID,PRIVATE_IP"]}\"\n" >> vault.yml
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_angular_port: \"${VM_DATA["$VM_ID,PORT_80"]}\"\n" >> vault.yml
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_spring_port: \"${VM_DATA["$VM_ID,PORT_8080"]}\"\n" >> vault.yml
        printf "webservers_${VM_DATA["$VM_ID,SSH"]}_python_port: \"${VM_DATA["$VM_ID,PORT_8000"]}\"\n" >> vault.yml
        LAST_WEB_VM_ID=$VM_ID
    fi

    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$DB_TEMPLATE_ID" ]; then
        printf "databases_${VM_DATA["$VM_ID,SSH"]}_pass: \"${VM_DATA["$VM_ID,VM_PASSWORD"]}\"\n" >> vault.yml
        printf "databases_${VM_DATA["$VM_ID,SSH"]}_public_ip: \"${VM_DATA["$VM_ID,PUBLIC_IP"]}\"\n" >> vault.yml
        printf "databases_${VM_DATA["$VM_ID,SSH"]}_private_ip: \"${VM_DATA["$VM_ID,PRIVATE_IP"]}\"\n" >> vault.yml
        printf "databases_${VM_DATA["$VM_ID,SSH"]}_port: \"${VM_DATA["$VM_ID,PORT_5432"]}\"\n" >> vault.yml
        LAST_DB_VM_ID=$VM_ID
    fi

    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$TRANSLATE_TEMPLATE_ID" ]; then
        printf "translators_${VM_DATA["$VM_ID,SSH"]}_pass: \"${VM_DATA["$VM_ID,VM_PASSWORD"]}\"\n" >> vault.yml
        printf "translators_${VM_DATA["$VM_ID,SSH"]}_public_ip: \"${VM_DATA["$VM_ID,PUBLIC_IP"]}\"\n" >> vault.yml
        printf "translators_${VM_DATA["$VM_ID,SSH"]}_private_ip: \"${VM_DATA["$VM_ID,PRIVATE_IP"]}\"\n" >> vault.yml
        printf "translators_${VM_DATA["$VM_ID,SSH"]}_port: \"${VM_DATA["$VM_ID,PORT_80"]}\"\n" >> vault.yml
        LAST_TR_VM_ID=$VM_ID
    fi

    user="${VM_DATA["$VM_ID,USERNAME"]}"
    private_ip="${VM_DATA["$VM_ID,PRIVATE_IP"]}"
    echo "Sending ssh key to $user at $user@$private_ip"
    ssh-keyscan -H "$private_ip" >> ~/.ssh/known_hosts
    sshpass -p "${VM_DATA["$VM_ID,VM_PASSWORD"]}" ssh-copy-id "$user@$private_ip"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully copied ssh key to $user@$private_ip\n${CRESET}"
    else
        echo -e "${RED}Failed to copy ssh key to $user@$private_ip${CRESET}"
        ask_if_to_continue
    fi
    ((index++))
done
rm vm-info.txt
echo ""

# Set database password for all database servers
while true; do
    read -p "Enter new database password: " -s DATABASE_PASSWORD
    echo ""
    read -p "Reenter new database password: " -s DATABASE_PASSWORD2
    echo ""
    if [ "$DATABASE_PASSWORD" == "$DATABASE_PASSWORD2" ]; then
        printf "database_password: \"$DATABASE_PASSWORD\"\n" >> vault.yml
        echo -e "${GREEN}Database password successfully added to vault.yml${CRESET}"
        break
    else
        echo -e "${RED}Passwords do not match, please try again${CRESET}"
    fi
done
echo ""

printf "webservers_${VM_DATA["$LAST_WEB_VM_ID,SSH"]}_database_private_ip: \"${VM_DATA["$LAST_DB_VM_ID,PRIVATE_IP"]}\"\n" >> vault.yml
# --------------------------------------------------
# Encrypt vault
while true; do
    ansible-vault encrypt vault.yml
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully set up vault.yml\n${CRESET}"
        break
    else
        echo "${RED}Vault encryption failed, try again${CRESET}"
    fi
done
echo ""

# --------------------------------------------------
# Create hosts file
echo "Creating hosts file"
echo "" > hosts
printf "[databases]\n" >> hosts
for VM_ID in "${VM_ID_LIST[@]}"; do
    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$DB_TEMPLATE_ID" ]; then
        printf "%s\n" "${VM_DATA["$VM_ID,SSH"]}" >> hosts
        LAST_DB_VM_ID=$VM_ID
    fi
done
printf "[webservers]\n" >> hosts
for VM_ID in "${VM_ID_LIST[@]}"; do
    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$WEB_TEMPLATE_ID" ]; then
        printf "%s\n" "${VM_DATA["$VM_ID,SSH"]}" >> hosts
    fi
done
printf "[translators]\n" >> hosts
for VM_ID in "${VM_ID_LIST[@]}"; do
    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$TRANSLATE_TEMPLATE_ID" ]; then
        printf "%s\n" "${VM_DATA["$VM_ID,SSH"]}" >> hosts
    fi
done
echo -e "${GREEN}Successfully created hosts file\n${CRESET}"

# --------------------------------------------------
# Ping to see if everything is working
echo "Pinging remote machines with ansible"
ansible all -m ping -i hosts
if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}Successfully pinged all machines\n${CRESET}"
else
    echo ""
    echo -e "${RED}Ping failed, are you sure you want to continue${CRESET}"
    ask_if_to_continue
fi

echo "Please update in gitlab ci/cd variables and rerun pipe, wait for it to complete"
echo "DATABASE_IP: ${VM_DATA["$LAST_DB_VM_ID,PRIVATE_IP"]}"
echo "DATABASE_USER_PASSWORD: (can't say password)"
echo "SPRING_API_URL: http://${VM_DATA["$LAST_WEB_VM_ID,PUBLIC_IP"]}"
echo "LIBRETRANSLATE_API_URL: http://${VM_DATA["$LAST_TR_VM_ID,PUBLIC_IP"]}"
echo "press any key to continue..."
read -s -n 1

# --------------------------------------------------
# Run playbooks
PLAYBOOKS=("webserver-setup.yaml" "webserver-download.yaml" "database-setup.yaml" "libretranslate-setup.yaml")

run_playbook() {
    local playbook=$1
    echo "Running $playbook playbook"
    ansible-playbook $playbook -i hosts --ask-vault-pass
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully finished\n${CRESET}"
    else
        echo -e "${ORANGE}Failed. Try again later with\nansible-playbook $playbook -i hosts --ask-vault-pass\n${CRESET}"
        if [ "$playbook" = "webserver-download.yaml" ]; then
            echo "Do not worry, you can upload all builds and send it from this machine, follow the README.md on how to do that"
        fi
    fi
}

for playbook in "${PLAYBOOKS[@]}"; do
    run_playbook $playbook
done

# --------------------------------------------------
# Print SSH connections to the user
for VM_ID in "${VM_ID_LIST[@]}"; do
    echo "[ ${VM_DATA["$VM_ID,NAME"]} ] ${VM_DATA["$VM_ID,SSH_CON"]}"
done

for VM_ID in "${VM_ID_LIST[@]}"; do
    if [ "${VM_DATA["$VM_ID,TEMPLATE_ID"]}" == "$WEB_TEMPLATE_ID" ]; then
        printf "Connect to webserver with %s:%s\n" "${VM_DATA["$VM_ID,PUBLIC_IP"]}" "${VM_DATA["$VM_ID,PORT_80"]}"
    fi
done
