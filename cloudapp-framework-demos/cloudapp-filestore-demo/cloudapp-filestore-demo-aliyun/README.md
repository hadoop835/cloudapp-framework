# cloudapp-filestore-demo-aliyun

## Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and
   secretKey to the environment variables. or Replace the accessKey and secretKey
   in application.yml

2. Create a bucket, and set the bucket name to the environment variable

## Configuration
Application configuration example is as follows:
```yaml
io:
  cloudapp:
    filestore:
      aliyun:
        enabled: true
        access-key: # your accessKey
        secret-key: # your secretKey 
        endpoint: # region server endpoint
```

## Run demo

Open the AliyunFileStoreDemoApplication class and run it.