# Defect Registry

## Launch the app

Follow steps below in order to launch the application:

1. ```docker-compose up --build -d```

## How to use the api

### GET Defects
```
curl -X GET \
  http://localhost:80/defects \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache'
```

```
curl -X GET \
  http://localhost:80/defects/1 \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache,no-cache'
```

### GET User(s) Associated with the Defect

```
curl -X GET \
  http://localhost:80/defects/1/users \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: f07474ec-beff-49a1-8e09-f8459a137781' \
  -H 'cache-control: no-cache,no-cache,no-cache'
```
```
curl -X GET \
  http://localhost:80/defects/1/users/tomas.tomavicius@gmail.com \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: f07474ec-beff-49a1-8e09-f8459a137781' \
  -H 'cache-control: no-cache,no-cache,no-cache'
```


### DELETE Defect

```
curl -X DELETE \
  http://localhost:80/defects/2 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json'
```

### DELETE User Associated With The Defect
```
curl -X DELETE \
  http://localhost:80/defects/1/users/tomas.tomavicius@gmail.com \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json'
```


### POST Defect

```
curl -X POST \
  http://localhost:80/defects \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "name": "Test101",
  "description": "A test defect",
  "priority": "HIGH",
  "status":"NEW"
}'
```
### POST User to Associate with the Defect
```
curl -X POST \
  http://localhost:80/defects/1/users \
  -H 'Postman-Token: 0dd68f57-8d63-4910-87d5-eb1d7728f7a5' \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache,no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "firstName": "Tomas",
    "lastName": "Tomavicius",
    "email": "tomas.tomavicius@gmail.com"
}'
```

### PUT Defect

```
curl -X PUT \
  http://localhost:80/defects/1 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "name": "Test101",
  "description": "A test defect",
  "priority": "HIGH",
  "status":"NEW"
}'
```
### PUT User Associated with the Defect
```
curl -X PUT \
  http://localhost:80/defects/1/users/tomas.tomavicius@gmail.com \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
      "firstName": "Tomas",
      "lastName": "Tomavicius",
      "email": "tomas.tomavicius@gmail.com"
}'
```
### PATCH Defect
```
curl -X PATCH \
  http://localhost:80/defects/3 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "name": "Test101",
  "description": "A test defect",
  "status":"NEW"
}'
```
### PATCH User Associated with the Defect
```
curl -X PATCH \
  http://localhost:80/defects/1/users/tomas.tomavicius@gmail.com \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
      "firstName": "Tomas",
      "lastName": "Lenavicius",
      "email": "tomas.tomavicius@gmail.com"
}'
```