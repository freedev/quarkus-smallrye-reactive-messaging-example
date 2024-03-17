package it.damore.app;

import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log = Logger.getLogger(getClass());

    protected Producer() {}

    @Outgoing("from-producer-to-processor")
    public Message<ClassA> produce() {
        ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
        Utils.longExecution();
//        log.info("Producer emitting " + classA);
        return Message.of(classA);
    }

}
