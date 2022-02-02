package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.Executors;

@ApplicationScoped
public class Consumer1 {

    protected final Logger log;

    BroadcastProcessor<String> processor;

    protected Consumer1() {
        this.processor = BroadcastProcessor.create();
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
