package it.damore.app;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class Processor {

    protected final Logger log;

    protected Processor() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    public Uni<ClassB> processor(Message<ClassA> msg) {
        return Uni.createFrom()
                .item(msg)
                .onItem()
                .transform(classA -> {
                    longExecution();
                    ClassB converted = new ClassB(String.format("YYY %s", classA.getPayload().value));
                    return converted;
                });
    }

    public void longExecution() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
