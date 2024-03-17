package it.damore.app;

import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Random;

import static it.damore.utils.Utils.longExecution;

@ApplicationScoped
public class Processor {

    protected final Logger log = Logger.getLogger(getClass());

    protected Processor() {}

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
//    @Blocking(value = "processor-custom-pool", ordered = false)
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    public Message<ClassB> message2message(Message<ClassA> classA) {
        ClassB manipulated = new ClassB(String.format("YYY %s", classA.getPayload().value));
        Utils.longExecution();
        int i = new Random().nextInt();
        if (i % 7 == 0)
           classA.nack(new InternalError(classA.getPayload().toString()));
        else
           classA.ack();
        log.infof("Processor received %s", classA.getPayload());
        return Message.of(manipulated);
    }

}
