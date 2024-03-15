package it.damore.app;

import io.smallrye.mutiny.Uni;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Consumer {

    protected final Logger log;

    protected Consumer() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-processor-to-consumer")
    public Uni<Void> consume(Message<ClassB> msg) {
        log.infof("Consumer received %s", msg.getPayload());
        return Uni.createFrom().voidItem();
    }

}
