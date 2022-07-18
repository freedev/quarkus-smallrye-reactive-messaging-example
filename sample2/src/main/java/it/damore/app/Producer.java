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

    private AtomicInteger counter = new AtomicInteger();

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Outgoing("from-producer-to-processor")
    public Multi<ClassA> periodicallySendMessage() {

        return Multi.createFrom()
                .ticks()
                .startingAfter(Duration.ofSeconds(2))
                .every(Duration.ofMillis(50))
                .onItem().transform(t -> new ClassA("Hello " + t))
                .onFailure(mm -> {
                    log.info("Producer NOT EMITTING");
                    return true;
                })
                .retry()
                .withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(10))
                .indefinitely()
                .onItem()
                .invoke(msg -> log.info("Producer emitting " + msg));
    }
}
