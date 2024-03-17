package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class Consumer {

    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log = Logger.getLogger(getClass());

    protected Consumer() {}

    @Incoming("from-processor-to-consumer")
    public void consume(ClassB classB) {
        var currentValue = counter.incrementAndGet();
        if (currentValue % 100 == 0) {
            log.infof("Consumer received %s messages", currentValue);
        }
//        log.infof("Consumer received %s - %s", list.size(), list.get(0));
//        log.infof("Consumer received %s", classB);
    }

}
