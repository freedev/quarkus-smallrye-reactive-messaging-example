package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
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
    @Blocking(ordered = false)
    public Message<ClassA> periodicallySendMessage() {
        ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
        log.info("Producer emitting " + classA);
        return Message.of(classA);
    }
}
