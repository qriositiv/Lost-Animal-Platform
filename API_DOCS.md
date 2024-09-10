# REST API ROUTES FOR COMMUNICATING

This is documentation on how information is flowing through REST API.

Based on the architecture information flows like this: <br>
Angular -> Spring -> PostgreSQL <br>
Angular <- Spring <- PostgreSQL <br>
OR <br>
Angular -> Spring -> Python -> Spring -> PostgreSQL <br>
Angular <- Spring <- PostgreSQL

For available routes see: [Spring section](#spring) and [Python section](#python)

##### STATUS CODES:

```
200	                OK	                Everything worked as expected.
400	                Bad Request	        The request was unacceptable, often due to missing a required parameter.
402	                Request Failed	    The parameters were valid but the request failed.
404	                Not Found	        The requested resource doesn’t exist.
409	                Conflict	        The request conflicts with another request.
429	                Too Many Requests	Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
500, 502, 503, 504	Server Errors	    Something went wrong on server end.
```

Any returned response code that is not 200 needs to have an error attribute which specifies the error:

```json
{
  "error": "error message"
}
```

All API routes shall start with `/api` and the following routes are added on to it, for example GET all users:

```bash
curl -X GET "http://193.284.91.104:15387/api/users" \
     -H "Accept: application/json"
```

## SPRING

### user routes

```
POST    /users
GET     /users/:id
GET     /users
DELETE  /users/:id
```

---

Create user.

`/users` **POST**

PARAMETERS

```json
{
  "username": "username",
  "password": "5f4dcc3b5aa765d61d8327deb882cf99",
  "name": "John Smith",
  "phone": 37061432799,
  "email": "straysafe@gmail.com"
}
```

```
username    REQUIRED    STRING
password    REQUIRED    STRING      md5 hashed password
name        OPTIONAL    STRING
phone       OPTIONAL    NUMBER      Follows Lithuanian format +370 country code, 6 city code, 8 unique numbers
email       OPTIONAL    STRING
```

RESPONSE

```
The same as /users/:id GET
```

---

Get specific user.

`/users/:id` **GET**

PARAMETERS

```
none
```

RESPONSE

```json
{
  "id": 1608749258,
  "username": "username",
  "name": "John Smith",
  "phone": 37061432799,
  "email": "straysafe@gmail.com"
}
```

```
id          NUMBER
username    STRING
name        STRING
phone       NUMBER
email       STRING
```

---

Get all users

`/users` **GET**

PARAMETERS

```
none
```

RESPONSE

```json
{
  "data": [
    {
      "id": 1608749258,
      "username": "username",
      "name": "John Smith",
      "phone": 37061432799,
      "email": "straysafe@gmail.com"
    },
    {
      "id": 1801978255,
      "username": "username2",
      "name": null,
      "phone": null,
      "email": null
    }
    {
      ...
    }
  ]
}
```

```
id          NUMBER
username    STRING
name        STRING
phone       NUMBER
email       STRING
```

---

Delete user with ID

`/users/:id` **DELETE**

PARAMETERS

```
none
```

RESPONSE

```json
{
  "id": 1608749258,
  "deleted": true
}
```

```
id          NUMBER      user id
deleted     BOOLEAN     Did it successfully delete?
```

---

---

### report routes

```
POST    /reports
GET     /reports/:id
GET     /reports
GET     /reports/short/:amount
POST    /reports/:id/found
POST    /reports/:id/saw
POST    /reports/:id/cancel
```

---

Create a new lost or seen pet report.

`/reports` **POST**

PARAMETERS

```json
{
  "id": 1608749258,
  "report_type": 0,
  "pet_name": "Persikas",
  "breed": "Poodle",
  "pet_type": 0,
  "pet_size": 1,
  "gender": 0,
  "image": "...",
  "pet_position": {
    "x": 120,
    "y": 200,
    "radius": 20
  },
  "notes": "My dog is very cute, and looks at you when you call him."
}
```

```
id          REQUIRED    NUMBER      user id
report_type REQUIRED    NUMBER      0 means lost, 1 means seen
pet_name    OPTIONAL    STRING      if null or nonexistant then pet_name will be replaced with generated ID
pet_type    REQUIRED    NUMBER      0 is dog, 1 is cat
pet_size    REQUIRED    NUMBER      0 is small, 1 is medium, 2 is large
gender      NUMBER      0 means female, 1 means male
image       REQUIRED    MULTIPART/FORM-DATA
pet_position REQUIRED   ARRAY       The position of the pet in the image, cut out the background and leave only the pet
    x       REQUIRED    NUMBER      Center X position of the circle of pet
    y       REQUIRED    NUMBER      Center Y position of the circle of pet
    radius  REQUIRED    NUMBER      At position X and Y how large is the circle
notes       OPTIONAL    STRING      Text which user can decide to put
```

RESPONSE

```
The same as /reports/:id GET
```

---

Get report information by specifying the specific report id

`/reports/:id` **GET**

PARAMETERS

```
none
```

REPSONSE

```json
{
  "id": 1608749258,
  "phone": 37061873666,
  "email": "straysafe@gmail.com",
  "pet_name": "Persikas",
  "breed": "Poodle",
  "pet_type": 0,
  "pet_size": 1,
  "gender": 0,
  "image": "...",
  "notes": "My dog is very cute, and looks at you when you call him.",
  "timestamp": 2024-04-20T15:30:00Z
}
```

```
id          NUMBER      user id
phone       NUMBER      user phone
email       STRING      user email
pet_name    STRING      if null or nonexistant then pet_name will be replaced with generated ID
pet_type    NUMBER      0 is dog, 1 is cat
pet_size    NUMBER      0 is small, 1 is medium, 2 is large
gender      NUMBER      0 means female, 1 means male
image       MULTIPART/FORM-DATA
notes       STRING      Text which user can decide to put
timestamp   DATE        Timestamp of when the report was created
```

---

Get short version of the reports used for grid view. The amount variable specifies how many reports should be returned

`/reports/short/:amount` **GET**

PARAMETERS

```json
{
  "filters": {
    "location": {
      "address": "Didlaukio 49",
      "radius": 12
    },
    "from_now_till": 2024-04-20T15:30:00Z,
    "pet_type": 0,
    "breed": "Poodle",
    "gender": 0
  }
}
```

```
address     STRING      address where the person lost the pet
radius      NUMBER      radius in kilometers
from_now_till TIMESTAMP ISO-8601 standard time when the oldest report can be seen
pet_type    NUMBER      0 means dog, 1 means cat
breed       STRING      pet breed
gender      NUMBER      0 means female, 1 means male
```

REPSONSE

```json
{
  "data": [
    {
      "username": "username",
      "pet_name": "Persikas",
      "image": "...",
      "timestamp": "2024-04-20T15:30:00Z"
    },
    {
      ...
    }
  ]
}
```

```
username    STRING      username
pet_name    STRING      if null or nonexistant then pet_name will be replaced with generated ID
image       MULTIPART/FORM-DATA
timestamp   TIMESTAMP   ISO-8601 Timestamp of when the report was created
```

---

Get report information by specifying the specific report id

`/reports` **GET**

PARAMETERS

```
none
```

REPSONSE

```json
{
  "data": [
    {
      "id": 1608749258,
      "phone": 37061873666,
      "email": "straysafe@gmail.com",
      "pet_name": "Persikas",
      "breed": "Poodle",
      "pet_type": 0,
      "pet_size": 1,
      "gender": 0,
      "image": "...",
      "notes": "My dog is very cute, and looks at you when you call him."
    },
    {
      ...
    }
  ]
}
```

```
id          NUMBER      user id
pet_name    STRING      if null or nonexistant then pet_name will be replaced with generated ID
pet_type    NUMBER      0 is dog, 1 is cat
pet_size    NUMBER      0 is small, 1 is medium, 2 is large
gender      NUMBER      0 is female, 1 is male
image       MULTIPART/FORM-DATA
notes       STRING      Text which user can decide to put
```

## PYTHON

### image routes

```
POST    /compare
```

---

Compare provided image with specific pet reports. It will compare with specified reports ids,
if none of them were found in the database, then it only calculates histograms and puts in the database.

`/compare` **POST**

PARAMETERS

```json
{
  "compare_ids": [2, 40, 32, 14, 50],
  "source_id": 103
}
```

```
compare_ids   REQUIRED    INTEGER   Array of different ids of different reports to compare with
source_id     REQUIRED    INTEGER   Current report id which is being compared to
```

RESPONSE

```json
{
  "source_id": 1,
  "similarity": [
    {
      "id": 102,
      "value": 0.9655481794658376
    },
    {
      ...
    }
  ]
}
```

```
source_id   The new user uploaded image
similarity  Array of different ids that were compared and how similar they were.
  id        Report id which the source id was compared with
  value     Value specifying how similar these images were. They are from 0 to 1. Where 0 means most similar and 1 means least similar
```
