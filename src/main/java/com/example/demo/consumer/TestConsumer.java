package com.example.demo.consumer;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TestConsumer {
  @Bean
  public Function<Flux<Message<String>>, Mono<Void>> test() {
    return events ->
      events.doOnNext(event -> {
        log.info("Got an event: {}", event);
      }).then();
  }
}