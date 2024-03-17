package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.smallrye.reactive.messaging.MutinyEmitter;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {
    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log = Logger.getLogger(getClass());
    protected Producer() {}

    @Inject @Channel("from-producer-to-processor")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
    MutinyEmitter<ClassA> emitter;
    public void periodicallySendMessage() {
        AtomicInteger counter = new AtomicInteger();
        Runnable runnable = () -> {
            ClassA message = new ClassA("Hello " + counter.getAndIncrement());

            Uni.createFrom()
                    .item(message)
                    .onItem()
                    .transformToUni(m -> {
                        return Uni.createFrom()
                                .deferred(() -> {
                                    return emitter.send(message);
                                });
                    })
                    .onFailure(e -> {
                        log.error("Exception while sending", e);
                        return true;
                    })
                    .retry()
                    .withBackOff(Duration.ofSeconds(5), Duration.ofSeconds(5))
                    .atMost(10)
                    .replaceWithVoid()
                    .subscribe()
                    .asCompletionStage();
        };

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(runnable, 1, 100, TimeUnit.MILLISECONDS);
    }
}
