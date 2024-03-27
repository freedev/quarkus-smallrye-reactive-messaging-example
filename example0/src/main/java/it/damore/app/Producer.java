package it.damore.app;

import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Producer {

//    @Outgoing("from-producer-to-processor")
//    @Blocking()
    public ClassA produce() {
        ClassA classA = new ClassA("Hello " + Utils.counter.getAndIncrement());
//        Utils.longExecution();
        log.info("Producer emitting " + classA);
        return classA;
    }

    protected final Logger log = Logger.getLogger(getClass());
}
