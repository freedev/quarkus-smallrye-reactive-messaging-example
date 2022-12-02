package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureFailure;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.unchecked.Unchecked;
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
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    public Multi<ClassA> periodicallySendMessage() {

        return Multi.createFrom()
                .emitter(emitter -> {
                    Integer value = Integer.valueOf(counter.getAndIncrement());
                    while (true) {
                        try {
                            emitter.emit(value);
                            value = Integer.valueOf(counter.getAndIncrement());
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (BackPressureFailure e) {
                            log.info("retry....");
                        }
                    }
                }, BackPressureStrategy.ERROR)
                .onItem()
                .transform(t -> {
                    ClassA classA = new ClassA("Hello " + t);
                    log.info("Producer emitting " + classA);
                    return classA;
                })
                ;
    }
}
