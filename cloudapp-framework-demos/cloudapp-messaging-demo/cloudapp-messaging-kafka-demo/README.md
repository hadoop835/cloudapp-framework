# cloudapp-messaging-kafka-demo

## Before start

1. Create a kafka server, and config username and password
2. Create topics and groups

## Configuration
Application configuration example is as follows:
```yaml
io:
  cloudapp:
    messaging:
      kafka:
        servers: # server address
        username: # username
        password: # password
        mechanism: PLAIN
        security-protocol: SASL_SSL
#        identificationAlgorithm: ""
        ssl: # ssl config
          trust-store-location: classpath:/sasl/mix.4096.client.truststore.jks
#          trust-store-password: KafkaOnsClient
        inputs: #consumer config
          - name:  
            topic: 
            group: 
            max-fetch-bytes: 
            session-timeout: 
            properties:
              
        outputs: # producer config
          - name: 
            group: 
            topic: 
            properties:
              ["allow.auto.create.topics"]: false
```

## Run demo

Open the KafkaDemoApplication class and run it.