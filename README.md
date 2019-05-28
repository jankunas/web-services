# Defect Registry

## Launch the app

Follow steps below in order to launch the application:

1. ```docker-compose up --build -d```

## How to use SOAP service

```POST http://localhost:80/ws```

### Get Defects
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:getDefectsRequest></wsdl:getDefectsRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### GET Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:getDefectRequest>
         <wsdl:id>1</wsdl:id>
      </wsdl:getDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### GET User(s) Associated with the Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:getUserForDefectRequest>
         <wsdl:defectId>4</wsdl:defectId>
         <wsdl:email>testnew@gmail.com</wsdl:email>
      </wsdl:getUserForDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:getUsersForDefectRequest>
         <wsdl:defectId>4</wsdl:defectId>
      </wsdl:getUsersForDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Create Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:createDefectRequest>
         <wsdl:description>new description</wsdl:description>
         <wsdl:name>new name</wsdl:name>
         <wsdl:priority>HIGH</wsdl:priority>
         <wsdl:user>
            <wsdl:email>testnew@gmail.com</wsdl:email>
            <wsdl:firstName>name</wsdl:firstName>
            <wsdl:lastName>lastName</wsdl:lastName>
         </wsdl:user>
         <wsdl:status>NEW</wsdl:status>
      </wsdl:createDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Create User to Associate with the Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:addUserToDefectRequest>
         <wsdl:defectId>1</wsdl:defectId>
         <wsdl:user>
            <wsdl:email>testas@gmail.com</wsdl:email>
            <wsdl:firstName>testas</wsdl:firstName>
            <wsdl:lastName>testas</wsdl:lastName>
         </wsdl:user>
      </wsdl:addUserToDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### PUT User Associated with the Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:patchUserToDefectRequest>
         <wsdl:defectId>3</wsdl:defectId>
         <wsdl:email>testasasazZZZ@gmail.com</wsdl:email>
         <wsdl:user>
            <wsdl:firstName>FirstNamez</wsdl:firstName>
            <wsdl:email>testasasazZZZ@gmail.com</wsdl:email>
            <wsdl:lastName>lastName</wsdl:lastName>
         </wsdl:user>
      </wsdl:patchUserToDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### PATCH User Associated with the Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:patchUserToDefectRequest>
         <wsdl:defectId>3</wsdl:defectId>
         <wsdl:email>testasasazZZZ@gmail.com</wsdl:email>
         <wsdl:user>
            <wsdl:firstName>FirstNamez</wsdl:firstName>
            <wsdl:email>testasasazZZZ@gmail.com</wsdl:email>
            <wsdl:lastName>lastName</wsdl:lastName>
         </wsdl:user>
      </wsdl:patchUserToDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### Patch Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:patchDefectRequest>
         <wsdl:id>1</wsdl:id>
         <wsdl:description>updated description</wsdl:description>
         <wsdl:name>updated name</wsdl:name>
      </wsdl:patchDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
### Put Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:patchDefectRequest>
         <wsdl:id>1</wsdl:id>
         <wsdl:description>updated description</wsdl:description>
         <wsdl:name>updated name</wsdl:name>
         <wsdl:priority>HIGH</wsdl:priority>
         <wsdl:status>REJECTED</wsdl:status>
      </wsdl:patchDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
### Delete Defect
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsdl="http://www.kurti.lt/defectregistry/wsdl">
   <soapenv:Header/>
   <soapenv:Body>
      <wsdl:deleteDefectRequest>
         <wsdl:id>3</wsdl:id>
      </wsdl:deleteDefectRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
## How to use the rest api

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