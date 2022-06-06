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

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
//    @Blocking("processor-custom-pool")
    public ClassB consumeMulti2Multi(ClassA classA) {
        log.infof("Processor - received %s", classA);
        ClassB manipulated = new ClassB(String.format("YYY %s", classA.getValue()));
        longExecution();
        return manipulated;
    }

    public void longExecution() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
