package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log;

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Outgoing("from-producer-to-processor")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    public Multi<ClassA> periodicallySendMessage() {

        return Multi.createFrom()
                .ticks()
                .every(Duration.ofMillis(50))
                .onItem()
                .transform(t -> {
                    ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
                    log.info("Producer emitting " + classA);
                    return classA;
                });
    }
}
