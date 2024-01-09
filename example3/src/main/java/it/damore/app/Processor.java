package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.CustomThreadPoolProducer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class Processor {

    protected final Logger log;

    private final static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Processor.class.getName());

    private AtomicInteger counter = new AtomicInteger();
    private Integer maxGroupSize;
    private Integer maxDelay;
    private Integer maxRetry;
    private Duration initialBackOff;
    private Duration maxBackOff;
    private Integer sleep;
    protected Processor(
                            @ConfigProperty(name = "application.request.processor.max.concurrent.requests", defaultValue = "1")
                            Optional<Integer> optMaxConcurrencyLevel,
                            @ConfigProperty(name = "application.request.processor.max.retry", defaultValue = "30")
                            Optional<Integer> optMaxRetry,
                            @ConfigProperty(name = "application.request.processor.backoff.initial", defaultValue = "10")
                            Optional<Integer> optInitialBackOff,
                            @ConfigProperty(name = "application.request.processor.backoff.max", defaultValue = "50")
                            Optional<Integer> optMaxBackOff,
                            @ConfigProperty(name = "application.request.processor.max.group.size", defaultValue = "100")
                            Optional<Integer> optMaxGroupSize,
                            @ConfigProperty(name = "application.request.processor.max.delay", defaultValue = "10")
                            Optional<Integer> optMaxDelay,
                            @ConfigProperty(name = "application.request.processor.sleep", defaultValue = "5000")
                            Optional<Integer> optSleep
                    ) {
        this.maxRetry = optMaxRetry.orElse(1);
        this.initialBackOff = optInitialBackOff
                .map(Duration::ofSeconds)
                .orElse(Duration.ofSeconds(1));
        this.maxBackOff = optMaxBackOff
                .map(Duration::ofSeconds)
                .orElse(Duration.ofSeconds(5));
        this.maxGroupSize = optMaxGroupSize.orElse(100);
        this.maxDelay = optMaxDelay.orElse(10);
        this.sleep = optSleep.orElse(0);
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    public Multi<List<ClassB>> multi2multi(Multi<ClassA> stream) {
        return stream
//                .emitOn(pool)
                .group()
                .intoLists()
                .of(100)
                .flatMap(l -> {
                    return Multi.createFrom()
                            .deferred(() -> Multi.createFrom().item(l))
                            .onItem()
                            .transform(Unchecked.function(i -> this.convert(i)))
                            .onFailure()
                            .invoke(exception -> log.warn("Failed to read record."))
                            .onFailure(throwable -> {
                                log.errorf(throwable, "initial Failure - initialBackOff %s maxBackOff %s maxRetry %s times",
                                        this.initialBackOff, this.maxBackOff, this.maxRetry);
                                return true;
                            })
                            .retry()
                            .withBackOff(this.initialBackOff, this.maxBackOff)
                            .atMost(this.maxRetry)
                            .onFailure(throwable -> {
                                log.errorf(throwable, "latest Failure - initialBackOff %s maxBackOff %s maxRetry %s times",
                                        this.initialBackOff, this.maxBackOff, this.maxRetry);
                                return true;
                            })
                            .recoverWithCompletion();
                });
    }

    public List<ClassB> convert(List<ClassA> msgList) throws Exception {
        Random r = new Random();
        int i = r.nextInt();
        if (i % 7 != 0) {
            String errMsg = "Random exception raised";
            log.error(errMsg);
            throw new Exception(errMsg);
        }
        longExecution();
        List<ClassB> classBList = msgList.stream().map(msg -> new ClassB(String.format("YYY %s", msg.getValue()))).collect(Collectors.toList());
        return classBList;
    }

    public void longExecution() {
        try {
            Thread.sleep(this.sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
