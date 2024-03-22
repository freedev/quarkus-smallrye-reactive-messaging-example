package it.damore.app;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class Processor {

    protected final Logger log = Logger.getLogger(getClass());

    protected Processor() {}

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    @Blocking("processor-custom-pool")
    public Uni<ClassB> processor(Message<ClassA> msg) {
        return Uni.createFrom()
                .item(msg)
                .onItem()
                .transform(classA -> {
                    Utils.longExecution();
                    return new ClassB(String.format("%s-ish", classA.getPayload().value));
                });
    }
}
