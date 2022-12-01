package it.damore.app;

import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log;

    protected Producer() {
        this.log = Logger.getLogger(getClass());
    }

    @Outgoing("from-producer-to-processor")
    public Message<ClassA> periodicallySendMessage() {
        ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
        longExecution();
        log.info("Producer emitting " + classA);
        return Message.of(classA);
    }

    public void longExecution() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
