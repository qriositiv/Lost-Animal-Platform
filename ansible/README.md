# ANSIBLE SETUP

Make sure you have at least 3GB disk for using the normal setup and 5GB or more for building locally without artifacts

To launch initial setup, launch `ansible_vm_setup.sh`. It will show ip and port on which the frontend lives.

```bash
./ansible_vm_setup.sh
```

There is website setup with no gitlab artifacts, meaning it will build everything and use from local host.

### LIBRETRANSLATE

Systemd service edit with

```bash
sudo nano /etc/systemd/system/libretranslate.service
```

```
[Unit]
Description=LibreTranslate Service
After=network.target

[Service]
ExecStart=/home/rena9048/LibreTranslate/run.sh
WorkingDirectory=/home/rena9048/LibreTranslate
User=root
Group=root
Restart=always

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable libretranslate
sudo systemctl start libretranslate
```

### Modifying existing VM's

Prepare the build files on the ansible vm, you can send it with

```bash
scp -P port_numbers -r dist user@public_ip:~/straysafe/ansible
```

Files and folders expected:

```
straysafe/ansible/sql_scripts FOR SQL FILES
straysafe/ansible/dist FOR ANGULAR BUILD
straysafe/ansible/python-dist FOR PYTHON BUILD
straysafe/ansible/StraySafe.jar FOR SPRING APP
```

afterwards just launch the shell script for updating

```bash
./update_vms.sh
```

##### useful sql queries if you are connecting directly to database:

Database can be modified by connecting to the database and doing queries. Some useful commands:

```sql
DROP DATABASE straysafe;
CREATE DATABASE straysafe;

-- Rename
ALTER DATABASE straysafe RENAME TO straysafe_old;

-- DROP ALL TABLES
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
    END LOOP;
END $$;
```

To input all sql files from directory:

```bash
for file in ./sql_scripts/*.sql
do
  psql -d straysafe -U straysafe -h public_ip -p port -f "$file"
done
```

