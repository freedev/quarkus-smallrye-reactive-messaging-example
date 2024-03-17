package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class Consumer {

    protected final Logger log = Logger.getLogger(getClass());

    protected Consumer() {}

    @Incoming("from-processor-to-consumer")
    public void consume(ClassB classB) {
        log.infof("Consumer received %s", classB);
//        log.infof("Consumer received %s", msgList.size());
//        msgList.forEach(classB -> {
//            log.infof("Consumer received %s", classB);
//        });
    }

}
