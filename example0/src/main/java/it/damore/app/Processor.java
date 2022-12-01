package it.damore.app;

import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class Processor {

    protected final Logger log;

    protected Processor() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    @Blocking(value = "processor-custom-pool", ordered = false)
//    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    public Message<ClassB> message2message(Message<ClassA> msgClassA) {
        ClassB converted = new ClassB(String.format("YYY %s", msgClassA.getPayload().getValue()));
        longExecution();
        int i = new Random().nextInt();
        if (i % 7 == 0) {
            log.errorf("Processor nack %s", msgClassA.getPayload());
            msgClassA.nack(new InternalError());
        } else {
            log.infof("Processor converted %s", msgClassA.getPayload());
            msgClassA.ack();
        }
        return Message.of(converted);
    }

    public void longExecution() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}