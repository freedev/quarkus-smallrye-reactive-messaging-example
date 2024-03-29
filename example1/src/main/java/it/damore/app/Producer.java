package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    protected final Logger log = Logger.getLogger(getClass());

    protected Producer() {}

    @Outgoing("from-producer-to-processor")
    public Message<ClassA> producer() {
        ClassA classA = new ClassA("Hello " + Utils.counter.getAndIncrement());
        log.info("Producer emitting " + classA);
        return Message.of(classA);
    }
}
