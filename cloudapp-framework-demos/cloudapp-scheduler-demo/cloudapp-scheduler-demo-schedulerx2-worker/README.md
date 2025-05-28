# cloudapp-scheduler-demo-schedulerx2-worker

## Before start

Requires schedulerx2 server, and set the schedulerx2 endpoint, region-id, access-key-id and access-key-secret to the
environment variable.

## Configuration

Application configuration example is as follows:

```yaml
io:
  cloudapp:
   scheduler:
     schedulerx2-worker:
       enabled: true
       openapi-endpoint: ${SCX_OPENAPI_ENDPOINT}
       access-key-id: ${SCX_ACCESS_KEY_ID}
       access-key-secret: ${SCX_ACCESS_KEY_SECRET}
       region-id: ${SCX_REGION_ID}
       endpoint: ${SCX_ENDPOINT}
       namespace: ${SCX_NAMESPACE}
       group-id: ${SCX_GROUP_ID}
       app-key: ${SCX_APP_KEY}
```

## Run demo

Open the SchedulerX2WorkerDemoApplication class and run it.