# cloudapp-messaging-rocketmq-demo

## Before start

1. Create a rocketmq server, and config username and password
2. Create topics and groups

## Configuration
Application configuration example is as follows:
```yaml
io:
  cloudapp:
    messaging:
      rocketmq:
        enabled: true
        nameServer: # server address
        username: # username(accessKey)
        password: # password(secretKey)
        useTLS: false
        enableMsgTrace: false
        traceTopic:
        inputs: #consumer config
          - groupName: test-group
#            accessChannel:
#            messageModel: CLUSTERING
#            pullBatchSize:
#            namespace:
            name: rocketConsumer
            topic: test-topic
#            tags:
        outputs: # producer config
          - name: rocketProducer
#            messageModel: CLUSTERING
            groupName: test-group
#            namespace:
#            sendTimeout:
#            instanceName:
#            retryNextServer:
#            compressMsgBodyOverHowMuch:
#            maxMessageSize:
#            retryTimesWhenSendFailed:
```

## Run demo

Open the KafkaDemoApplication class and run it.