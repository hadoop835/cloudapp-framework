# cloudapp-apigateway-demo-aliyun

## Before start

Firstly,create API gateway instance on the CSB console.

Currently, you can create consumers by using code or web page.
For the web page method, you need to log in to the CSB console
and select the current gateway instance to add. For details,
view product documentation. 

The code method requires you to apply for AK and SK on the 
AccessKey management page and set AK and SK to the environment
variables. You can use the configuration file, taking
application.yml as an example:

```yaml
io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        server:
          accessKey: # your accessKey
          secretKey: # your secretKey
          gatewayUri: # server address

```
Now, you can set the authentication information of the created consumer in the configuration file.

## Configuration

Only the following authentication modes are supported: **`Basic`,`API Key`,`JWT`,`OAuth2`**

### Basic
Basic mode application configuration example is as follows:
```yaml
io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        basic:
          username: # your authorized username
          password: # your authorized username
```

### API Key
`API Key` mode application configuration example is as follows:
```yaml
io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        apikey:
          apiKey: # your api key
          headerName:  #request header for storing API Key, default X-API-KEY
```

### JWT

Only supports HMAC algorithm encryption.`JWT` mode application configuration example is as follows:

```yaml

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        jwt:
          keyId: # id of key lowercase letters and numbers
          secret: # your secret
          issuer:
          subject:
          audience:
          expiredSecond: # specifies the number of seconds after
                         # which the token expires
          algorithm: HS256
```

### OAuth2

**The CSB service does not provide oauth server functionality, so oauth
authentication requires an oauth server**. This service also requires an
interface that can be used for verification.`OAuth2` mode application configuration example is as follows:

```yaml
io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        oAuth2:
          scopes: #scope list
            - openid
            - profile
          clientId: # your clientId
          clientSecret: # your clientSecret
          redirectUris: # callback interface
            - http://localhost:8080/oauth/code
          enablePkce: false # enable pkce mode
          tokenUri: #token uri, as https://gitlab.com/oauth/token
          authorizationUri: #oauthorization uri,as https://gitlab.com/oauth/authorize
```

## Run demo

Open the DemoApplication class and run it.