package it.damore.app;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {
    private AtomicInteger counter;
    protected final Logger log;
    protected Producer() {
        this.counter = new AtomicInteger();
        this.log = Logger.getLogger(getClass());
    }

//    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
//    @OnOverflow(value = OnOverflow.Strategy.DROP)
//    @OnOverflow(value = OnOverflow.Strategy.UNBOUNDED_BUFFER)
//    @OnOverflow(value = OnOverflow.Strategy.FAIL)
//    @OnOverflow(value = OnOverflow.Strategy.FAIL)

    @Inject @Channel("from-producer-to-processor")
    MutinyEmitter<ClassA> emitter;

    public void periodicallySendMessage() {
        Runnable runnable = () -> {
            ClassA message = new ClassA("Hello " + counter.getAndIncrement());

            Uni.createFrom()
                    .item(message)
                    .onItem()
                    .transformToUni(m -> {
                        int c = m.getAndIncrement();
                        if (c > 0) {
                            log.info("re-emitting: " + message);
                        }
                        return emitter.send(m);
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
                .scheduleAtFixedRate(runnable, 1, 10, TimeUnit.MILLISECONDS);
    }
}
