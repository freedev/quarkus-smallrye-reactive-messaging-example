package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import it.damore.utils.CustomThreadPoolProducer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class Consumer1 {

    protected final Logger log;

    private static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Consumer1.class.getName());

    protected Consumer1() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-consumer1")
    @Outgoing("from-consumer1-to-processor")
    public Multi<ClassA> consume(Multi<ClassA> stream) {
        log.info("Consumer1 - assemblyTime ");
        return stream
                .emitOn(pool)
                .onItem()
                .invoke(message -> {
                   log.infof("consumer1 -> received message %s", message);
                });
    }

}
