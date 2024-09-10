# StraySafe

#### Team members

- Gerdvila Edvinas
- Narbutas Renaldas
- Nikulin Arsenij

## Project topic: _Lost animals platform_

#### Task description

Develop an web app /web platform that lets you tag a location of where you found a lost animal. With this kind of app you could potentially share the picture, location, comments, etc. with someone who has lost an animal so that they could find it. As an addition having the location of the nearest clinic or shelter nearby could make it even easier to take them some place safe. The system must integrate smart solutions and include several algorithms to support big amounts of users around the world.

#### Vision

Our project is a comprehensive, user-friendly platform dedicated to reuniting lost pets with their owners. This platform helps with searching for lost pets by centralizing all necessary resources and information into one accessible place.

## Overview

- Web server: Apache2
- Frontend framework: Angular
- CSS Framework: Tailwind
- Backend framework: Spring Boot Java
- Image comparison: Python
- Database: PostgreSQL
- Deployment: Ansible
- Dynamic translations: LibreTranslate

- libraries: Leaflet (OpenStreetMaps), Nomanatim geocoder, OpenCV

# Server Setup

There are ansible scripts made to setup the whole system in `ansible` folder.

## Developers setup

#### [DEBIAN] npm and angular-cli

```bash
sudo apt install npm
sudo npm install -g @angular/cli
```

Install nvm using (This is v0.39.7, you can find latest [here](https://nvm.sh)):

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
```

Install 20.11.0 node LTS (Long Term Support) version which works with angular

```bash
nvm install 20.11.0
```

#### [WINDOWS] npm and angular-cli

Download and run nvm setup [here](https://github.com/coreybutler/nvm-windows/releases)

Install 20.11.0 node LTS (Long Term Support) version which works with angular. This will download npm as well.

```bash
nvm install 20.11.0
```

Install angular CLI

```bash
npm install -g @angular/cli
```

#### Database setup

Install latest version:

```bash
sudo apt install postgresql -y
```

Setup postgres user password

```bash
sudo -u postgres psql
```

```bash
ALTER USER postgres WITH PASSWORD 'newpassword';
CREATE DATABASE straysafe;
```

Launch sql files located in `./ansible/sql_scripts`

```bash
psql -U postgres -d straysafe -f "tables.sql"
psql -U postgres -d straysafe -f "data.sql"
```

Open the file pg_hba.conf.

```bash
sudo nano /etc/postgresql/<your_psql_version>/main/pg_hba.conf
```

First line of the settings should be. This will make it that only all IP and all users will be able to connect.

```bash
local all all md5
```

Restart the postgresql server:

```bash
sudo systemd restart postgresql
```

Don't forget to setup a .env file at:`./src/backend/src/main/resources`. Example:

```
DATABASE_IP=111.111.111.111
DB_PORT=2024
DB_NAME=straysafe
DB_USERNAME=postgres
DB_PASSWORD=newpassword
```

## Launch website

DATABASE

```bash
sudo systemctl start postgresql
```

BACKEND

```bash
cd src/backend
mvn package -DskipTests
java -jar target/StraySafe.jar
```

FRONTEND

```bash
cd src/frontend
ng serve
```

For trying production Frontend. Refreshing the page might not redirect you
properly because this does not have apache configurations

```bash
cd src/frontend
ng build --configuration=production
npm run run
```
