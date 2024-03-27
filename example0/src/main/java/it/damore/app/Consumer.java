package it.damore.app;

import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Consumer {

//    @Incoming("from-processor-to-consumer")
//    @Blocking()
    public void consume(ClassB classB) {
        log.infof("Consumer received %s", classB);
    }

    protected final Logger log = Logger.getLogger(getClass());
}
