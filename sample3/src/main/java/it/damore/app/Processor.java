package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.CustomThreadPoolProducer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

@ApplicationScoped
public class Processor {

    protected final Logger log;

    private final static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Processor.class.getName());

    protected Processor() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    public Multi<ClassB> consumeMulti2Multi(Multi<ClassA> stream) {
        return stream
                .emitOn(pool)
                .group()
                .intoLists()
                .of(5)
                .flatMap(list -> {
                    log.info("processor receive group of " + list.size());
                    Stream<ClassB> manipulatedStream = list.stream()
                            .map(msg -> {
                                    ClassB manipulated = new ClassB(String.format("YYY %s", msg.getValue()));
//                                    log.infof("processor manipulated message %s", manipulated);
                                    return manipulated;
                                });
                    longExecution();
                    return Multi.createFrom().items(manipulatedStream);
                });
    }

    public void longExecution() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
