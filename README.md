# Defect Registry

## Launch the app

Follow steps below in order to launch the application:

1. ```mvn clean package``` - to build the application.
2. ```docker build -t defect-registry``` - to build the image.
3. ```docker image ls``` - to list all your docker images and verify if the newly built one exists.
4. ```docker run -d -p 5000:8090 defect-registry``` - to run the application on port ```5000```

## How to use the api

### GET

```aidl
curl -X GET \
  http://localhost:5000/defects \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache'
```

```
curl -X GET \
  http://localhost:8090/defects/3 \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache,no-cache'
```

### DELETE

```aidl
curl -X DELETE \
  http://localhost:8090/defects/2 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json'
```

### POST

```aidl
curl -X POST \
  http://localhost:8090/defects \
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

### PUT

```aidl
curl -X PUT \
  http://localhost:8090/defects/3 \
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