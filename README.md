# Spring Cloud Stream - Reactive Binder Concurrency Issue

Simple SCS repro.

## Pre-requisites

Create a topic called "test"

## Versions

Spring Cloud Stream: 4.0.2
Boot: 3.0.6

## Describe the issue

The [Reactive Kafka Binder documentation](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream-binder-kafka.html#_concurrency) suggests it should be possible to set consumer concurrency above 1.  
The binder should then create the correct number of `KafkaReceiver` as per this value.

Given this config:

```yaml
    stream:
      bindings:
        test-in-0:
          destination: test
          binder: reactorKafka
          consumer:
            concurrency: 2
```

An error is thrown:

```Concurrency > 1 is not supported by reactive consumer, given that project reactor maintains its own concurrency mechanism. Was '...test-in-0.consumer.concurrency=2'```

This is contrary to the documentation.

### Root Cause

The FunctionConfiguration class still contains this check, which doesn't seem appropriate if the binder is the new Reactive one.

```java

    if (consumerProperties != null) {
        function.setSkipInputConversion(consumerProperties.isUseNativeDecoding());
        Assert.isTrue(consumerProperties.getConcurrency() <= 1, "Concurrency > 1 is not supported by reactive "
                + "consumer, given that project reactor maintains its own concurrency mechanism. Was '..."
                + inputBindingName + ".consumer.concurrency=" + consumerProperties.getConcurrency() + "'");
    }
```
If the check is removed, then everything seems to work as expected with a new ReactorKafkaBinder created as per the concurrency setting.