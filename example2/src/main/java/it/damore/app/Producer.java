package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.BackPressureFailure;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {
    protected final Logger log;

    private AtomicInteger counter = new AtomicInteger();

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }


    @Outgoing("from-producer-to-processor")
    public Uni<ClassA> producer() {
        ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
        log.info("Producer emitting " + classA);
        Utils.longExecution();
        return Uni.createFrom().item(classA);
    }
}
