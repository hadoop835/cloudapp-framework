# cloudapp-redis-demo

## Before start

Requires redis server, and set the redis server address, username and password
to the environment variable.

## Configuration
Application configuration example is as follows:
```yaml
io:
  cloudapp:
    redis:
      enabled: true
      host: # redis host
      password: # redis password
      username: # redis username
      port: 6379
```

## Run demo

Open the RedisDemoApplication class and run it.