#!/bin/bash

GREEN='\e[1;32m'
YELLOW='\e[1;33m'
RED='\e[1;31m'
ORANGE='\e[1;35m'
CRESET='\e[0m'

echo "Did you update the folder sql_scripts?"
echo "Did you add spring boot java file in ./StraySafe.jar ?"
echo "Did you add python-dist folder for python to ./python-dist ?"
echo "Did you add dist folder for angular to ./dist ?"
echo "If yes, press any key to continue..."
read -s -n 1

# Check if in the current folder exists all neccessary files
if [ ! -d ./dist ]; then
    echo -e "${RED}Folder 'dist' for angular does not exist in current directory${CRESET}"
    echo -e "${YELLOW}To copy it over use scp -P port_numbers -r dist $(whoami)@$(curl -s icanhazip.com):~/straysafe/ansible${CRESET}"
    exit 1
    elif [ ! -d ./python-dist ]; then
    echo -e "${RED}Folder 'python-dist' for python does not exist in current directory${CRESET}"
    echo -e "${YELLOW}After building python, rename the 'dist' folder to 'python-dist' then copy it with scp${CRESET}"
    echo -e "${YELLOW}mv dist python-dist\nscp -P port_numbers -r python-dist $(whoami)@$(curl -s icanhazip.com > /dev/null):~/straysafe/ansible${CRESET}"
    exit 1
    elif [ ! -e  ./StraySafe.jar ]; then
    echo -e "${RED}File StraySafe.jar for spring boot does not exist in current directory${CRESET}"
    echo -e "${YELLOW}To copy it over use scp -P port_numbers StraySafe.jar $(whoami)@$(curl -s icanhazip.com > /dev/null):~/straysafe/ansible${CRESET}"
    exit 1
fi

PLAYBOOKS=("webserver-update.yaml" "database-update.yaml")

run_playbook() {
    local playbook=$1
    echo "Running $playbook playbook"
    ansible-playbook $playbook -i hosts --ask-vault-pass
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully finished\n${CRESET}"
    else
        echo -e "${ORANGE}Failed. Try again later with\nansible-playbook $playbook -i hosts --ask-vault-pass\n${CRESET}"
    fi
}

for playbook in "${PLAYBOOKS[@]}"; do
    run_playbook $playbook
done
