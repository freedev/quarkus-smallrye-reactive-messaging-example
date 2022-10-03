package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class Consumer {

    protected final Logger log;

    protected Consumer() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-processor-to-consumer")
    public void consume(List<ClassB> list) {
        log.infof("Consumer received %s - %s", list.size(), list.get(0));
    }

}
