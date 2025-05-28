# cloudapp-config-demo-aliyun

## Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and secretKey to the environment variables. or
   Replace the accessKey and secretKey in application.yml

2. Create a microservice space and group in the Alibaba Cloud ACM console.

## Configuration

Application configuration example is as follows:

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848 # nacos server address
        import-check:
          enabled: false
io:
  cloudapp:
    config:
      aliyun:
        read:
          enabled: true
          timeout: # request timeout
          group: # group name
        write:
          enabled: true
          timeout: # request timeout
          group: # group name
          regionId:  # regionId
          namespaceId:  # microservice space id
          domain: # region server domain
          accesskey: # your accessKey
          secretKey: # your secretKey
```

## Run demo

Open the AliyunConfigDemoApplication class and run it.