package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Consumer1 {

    protected final Logger log;

    protected Consumer1() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-consumer1")
    @Outgoing("from-consumer1-to-processor")
    public Multi<ClassA> consume(Multi<ClassA> stream) {
        log.info("Consumer1 - assemblyTime ");
        return stream
                .onItem()
                .invoke(m -> {
                   log.infof("consumer1 -> received message %s", m);
                });
    }

}
