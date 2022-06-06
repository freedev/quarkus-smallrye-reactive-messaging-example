package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Consumer {

    protected final Logger log;

    protected Consumer() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("app2-from-processor-to-consumer")
    public void consume(ClassB msg) {
        log.infof("Consumer received %s", msg);
    }

}
