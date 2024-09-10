#!/bin/bash

cd ../src/backend/src/main/resources/

cat <<EOF > .env
DATABASE_IP=$1
DB_PORT=$2
DB_NAME=$3
DB_USERNAME=$4
DB_PASSWORD=$5
SPRING_USERNAME=$6
SPRING_PASSWORD=$7
JWT_SECRET=$8
SPRING_EMAIL_PASS=$9
EOF

chmod +r .env

if [ -e .env ] && [ -r .env ]; then
    echo ".env file created successfully in straysafe/src/backend/src/main/resources/.env"
else
    echo "Error: .env file creation failed."
    exit 1
fi
