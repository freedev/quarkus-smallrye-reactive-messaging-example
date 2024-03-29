package it.damore.app;

import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Consumer {

    protected final Logger log = Logger.getLogger(getClass());

    protected Consumer() {}

    @Incoming("from-processor-to-consumer")
    public CompletionStage<Void> consume(Message<ClassB> msg) {
//        log.infof("Consumer received %s", msg);
        return msg.ack();
    }

}
