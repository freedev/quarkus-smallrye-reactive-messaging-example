package it.damore.app;

import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {
    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log;
    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Inject @Channel("from-producer-to-processor")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
    Emitter<ClassA> emitter;
    public void periodicallySendMessage() {
        AtomicInteger counter = new AtomicInteger();
        Runnable runnable = () -> {
            ClassA message = new ClassA("Hello " + counter.getAndIncrement());
            log.info("Emitting: " + message);

            boolean sending = true;
            do {
                try {
                    emitter.send(message);
                    sending = false;
                } catch (Exception e) {
                    log.warnf("Handling... %s", e.getMessage());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } while (sending);
        };

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(runnable, 1, 10, TimeUnit.MILLISECONDS);
    }
}