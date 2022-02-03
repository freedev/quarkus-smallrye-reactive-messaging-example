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
public class Processor1 {

    protected final Logger log;

    private static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Processor1.class.getName());

    protected Processor1() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-consumer1-to-processor")
    @Outgoing("pippo")
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
                                    ClassB manipulated = ClassB.builder()
                                                                .value(String.format("YYY %s", msg.getValue()))
                                                                .build();
                                    log.infof("processor manipulated message %s", manipulated);
                                    return manipulated;
                                });
                    return Multi.createFrom().items(manipulatedStream);
                });
    }

}
