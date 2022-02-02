package it.damore.app;

import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    protected final Logger log;

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Inject
    @Channel("from-producer-to-consumer1")
    Emitter<ClassA> emitter;

    public void periodicallySendMessage() {
        AtomicInteger counter = new AtomicInteger();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                            ClassA message = ClassA
                                    .builder()
                                    .value("Hello " + counter.getAndIncrement())
                                    .build();
                            log.info("Emitting: " + message);
                            emitter.send(message);
                        },
                        1, 100, TimeUnit.MILLISECONDS);
    }

}
