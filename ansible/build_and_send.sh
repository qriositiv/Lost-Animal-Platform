#!/bin/bash
set -e

SSH_CON=rena9048@193.219.91.104
PORT=7461
DIR_OF_REMOTE_ANSIBLE_FOLDER=/home/rena9048/straysafe/ansible

CURR_DIR=$(pwd)

cd ../src/backend
mvn package -DskipTests
scp -P $PORT target/StraySafe.jar $SSH_CON:$DIR_OF_REMOTE_ANSIBLE_FOLDER

cd $CURR_DIR
cd ../src/python-backend
poetry build
mv dist python-dist
scp -P $PORT -r python-dist $SSH_CON:$DIR_OF_REMOTE_ANSIBLE_FOLDER
mv python-dist dist

cd $CURR_DIR
cd ../src/frontend
ng build --configuration=production
scp -P $PORT -r dist $SSH_CON:$DIR_OF_REMOTE_ANSIBLE_FOLDER
