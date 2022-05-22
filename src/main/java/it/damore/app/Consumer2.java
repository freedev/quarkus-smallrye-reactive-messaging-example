package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Consumer2 {

    protected final Logger log;

    protected Consumer2() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-processor-to-consumer2")
    public void consume(ClassB msg) {
        log.infof("consumer 3 received %s", msg);
    }

}
