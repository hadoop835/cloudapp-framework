# cloudapp-scheduler-demo-schedulerx2-server

## Before start

Requires schedulerx2 server, and set the schedulerx2 endpoint, region-id, access-key-id and access-key-secret to the
environment variable.

## Configuration

Application configuration example is as follows:

```yaml
io:
  cloudapp:
   scheduler:
     schedulerx2-server:
       enabled: true
       endpoint: ${SCX_ENDPOINT}
       access-key-id: ${SCX_ACCESS_KEY_ID}
       access-key-secret: ${SCX_ACCESS_KEY_SECRET}
       region-id: ${SCX_REGION_ID}
```

## Run demo

Open the SchedulerX2DemoApplication class and run it.