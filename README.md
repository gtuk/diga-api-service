# Diga Api 

This project is a basic api around the excellent [diga-api-client](https://github.com/alex-therapeutics/diga-api-client) 
It supports validation and billing.

### Prerequisites
You need to download the following things before running the api
* [Secon keystore generator](https://github.com/mawendo-gmbh/secon-keystore-generator/releases)
* [Mapping file](https://kkv.gkv-diga.de/) and follow the instructions https://github.com/alex-therapeutics/diga-api-client/wiki/Modifying-the-insurance-company-mapping-file-(Krankenkassenverzeichnis_DiGA.xml)
* [annahme-rsa4096.key](https://trustcenter-data.itsg.de/dale/annahme-rsa4096.key)

```bash
java -jar secon-keystore-generator.jar -k annahme-rsa4096.key -p my.prv.key.pem -c my.chain.p7c # This creates one keystore that contains the your private key and all the public insurance keys
```

### Run
Please make sure you set the proper values in the .env file and mount the mapping file and keystores under the expected path
```bash
cp .env.local .env
docker run --env-file .env -p 5000:5000 -v /tmp/Krankenkassenverzeichnis_DiGA.xml:/tmp/Krankenkassenverzeichnis_DiGA.xml -v /tmp/keystore.p12:/tmp/keystore.p12 --name digaApi diga_api
```

### Endpoints
* GET /validate/{CODE}
    ```
    Example response
    {
        "code": "77AAAAAAAAAAAAAX",
        "digavId": "00451000",
        "dayOfServiceProvision": "2021-06-02"
    }
    ```
  
* POST /bill
    ```
    Example payload
    {
        "invoiceNumber": "234",
        "code": "77AAAAAAAAAAAAAX",
        "digavId": "12345000",
        "dayOfServiceProvision": "2021-05-31"
    }
  
    Example response
    {
        "code": "77AAAAAAAAAAAAAX",
        "actionRequired": true,
        "transport": "POST",
        "xRechnung": "<?xml version=\"1.0\"..."
    }
    ```

### TODOS
* Tests
* More validation
* Better error handling
