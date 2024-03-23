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

@ApplicationScoped
public class Processor {

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
//    @Blocking()
    @Blocking(value = "processor-custom-pool", ordered = false)
    public ClassB process(ClassA classA) {
        ClassB classB = new ClassB(String.format("%s-ish", classA.value));
        Utils.longExecution();
        log.info("Processor converting " + classB);
        return classB;
    }

    protected final Logger log = Logger.getLogger(getClass());
}
