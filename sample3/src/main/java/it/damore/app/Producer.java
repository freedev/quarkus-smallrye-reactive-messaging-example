package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    protected final Logger log;

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Outgoing("app2-from-producer-to-processor")
    @OnOverflow(value = OnOverflow.Strategy.FAIL)
    public Multi<ClassA> periodicallySendMessage() {
        AtomicInteger counter = new AtomicInteger();

        return Multi.createFrom()
                .ticks()
                .every(Duration.ofMillis(200))
                .onItem()
                .transform(t ->  ClassA.builder()
                        .value("Hello " + counter.getAndIncrement())
                        .build())
                .onItem()
                .invoke(msg -> log.info("Producer emitting " + msg))
                .onFailure(mm -> {
                    log.info("Producer NOT EMITTING " + mm);
                    return true;
                })
                .retry()
                .withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(10))
                .indefinitely();
    }
}
