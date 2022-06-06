package it.damore.app;

import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Processor {

    protected final Logger log;

    protected Processor() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("app0-from-producer-to-processor")
    @Outgoing("app0-from-processor-to-consumer")
//    @Blocking("app0-processor-custom-pool")
    public ClassB consumeMulti2Multi(ClassA classA) {
        log.infof("Processor - received %s", classA);
        ClassB manipulated = ClassB.builder()
                .value(String.format("YYY %s", classA.getValue()))
                .build();
        longExecution();
        return manipulated;
    }

    public void longExecution() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
