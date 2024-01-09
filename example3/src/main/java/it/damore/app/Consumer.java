package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class Consumer {

    protected final Logger log;

    protected Consumer() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-processor-to-consumer")
    public void consume(List<ClassB> msgList) {
        log.infof("Consumer received %s", msgList.size());
    }

}
