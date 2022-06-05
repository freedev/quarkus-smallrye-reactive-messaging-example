package it.damore.app0;

import io.smallrye.mutiny.Multi;
import it.damore.Application;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    public Boolean enabled = true;
    protected final Logger log;

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Outgoing("app0-from-producer-to-processor")
    public Multi<ClassA> periodicallySendMessage() {
        AtomicInteger counter = new AtomicInteger();

        if (!enabled) {
            return Multi.createFrom().empty();
        }

        return Multi.createFrom()
                .ticks()
                .every(Duration.ofMillis(50))
                .onItem()
                .transform(t -> {
                    ClassA classA = ClassA.builder()
                            .value("Hello " + counter.getAndIncrement())
                            .build();
                    log.info("Producer emitting " + classA);
                    return classA;
                });
    }
}
