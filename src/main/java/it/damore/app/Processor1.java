package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.CustomThreadPoolProducer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class Processor1 {

    protected final Logger log;

    private static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Processor1.class.getName());

    protected Processor1() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    @Blocking
//    @OnOverflow(value = OnOverflow.Strategy.FAIL)
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
