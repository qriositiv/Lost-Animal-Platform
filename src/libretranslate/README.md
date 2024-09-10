To setup Libretranslate

[DEBIAN] setup

```bash
sudo ./setup.sh
```

copy over the systemd service and enable it, start is as well

```
sudo cp ./libretranslate.service /etc/systemd/system/
sudo systemctl start libretranslate
sudo systemctl enable libretranslate
```

Setup nginx

```bash
sudo cp ../../ansible/nginx /etc/nginx/sites-available/default
sudo nginx -t
sudo systemctl restart nginx
```

To download more languages

```bash
source ~/LibreTranslate/env/bin/activate
argospm update
argospm install translate-en_lt
```

To test you can send a POST request to the server ip:80/translate On opennebulla this may look like: <br>
http://193.219.91.103:5205/translate

```json
{
  "q": "Hello my people, I love you very much",
  "source": "auto",
  "target": "lt",
  "format": "text"
}
```
