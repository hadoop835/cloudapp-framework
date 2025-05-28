# cloudapp-search-demo-elasticsearch

## Before start

Requires elasticsearch server, and set the elasticsearch server address, username
 and password to the environment variable.

## Configuration
Application configuration example is as follows:
```yaml
io:
  cloudapp:
    search:
      elasticsearch:
        enabled: true
        host: # elasticsearch host
        username: # elasticsearch username
        password: # elasticsearch password

```

## Run demo

Open the ElasticsearchDemoApplication class and run it.