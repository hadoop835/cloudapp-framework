# cloudapp-sequence-mix

## Before start

Requires redis server, and set the redis server address, username and
password to the environment variable.

## Configuration

```yaml
io:
  cloudapp:
    sequence:
      snowflake:
        enabled: true
        workerId: # worker id
        workerIdBits: # worker id bits
        sequenceBits: # sequence bits
      redis:
        enabled: true
        host: # redis host
        password: # redis password
        username: # redis username
        port: # redis port

```

## Run demo

Open the CloudappSequenceMixDemoApplication class and run it.