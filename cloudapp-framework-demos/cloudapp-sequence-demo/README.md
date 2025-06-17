# CloudApp Sequence Demo

This project will tell you how to use the CloudApp-Sequence module correctly
and how to inherit it in your project. The specific details are presented in
the form of a module.

### How to use

First, you need to introduce the following configuration in the configuration file:

```yaml
io:
  cloudapp:
    sequence:
      redis:
        enabled: true
        host: localhost
        password: 
```
or
```yaml
io:
  cloudapp:
    sequence:
      snowflake:
        enabled: true
        workerId: 1
        workerIdBits: 5
        sequenceBits: 10
```

Second, add <code>spring-boot-starter-cloudapp</code> and <code>cloudapp-spring-sequence</code>
dependency to dependencies in project's pom file.

```xml
<dependencies>
    <dependency>
        <groupId>${groupId}</groupId>
        <artifactId>spring-boot-starter-cloudapp</artifactId>
        <version>${version number}</version>
    </dependency>
    
    <dependency>
        <groupId>${groupId}</groupId>
        <artifactId>cloudapp-spring-sequence</artifactId>
        <version>${version number}</version>
    </dependency>
</dependencies>
```

Now, you can use <code>@Autowide</code> or <code>@Resource</code> to get a
<code>SequenceGenerate</code> class which your code call the nextId method
to get a unique serial number.

### Modules

* **cloudapp-sequence-snowflake**
* **cloudapp-sequence-common-redis**
* **cloudapp-sequence-mix**
* **cloudapp-sequence-multi-queues**

### cloudapp-sequence-snowflake

This module show you, how to use snowflake to automatically
generate a global sequence.

### cloudapp-sequence-common-redis

If you already use redis and want to use this current redis to generate
serial numbers, you can refer to this module

### cloudapp-sequence-mix

If you want to use both redis and snowflake to generate serial numbers,
you can refer to this module

### cloudapp-sequence-multi-queues

If you want to use redis to generate serial numbers and have multiple
keys, you can refer to this module

