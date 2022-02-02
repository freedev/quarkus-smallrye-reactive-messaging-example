package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

@ApplicationScoped
public class Processor1 {

    protected final Logger log;

    protected Processor1() {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-consumer1-to-processor")
    @Outgoing("from-processor-to-consumer2")
    public Multi<ClassB> consumeMulti2Multi(Multi<ClassA> stream) {
        return stream
                .group()
                .intoLists()
                .of(5)
                .flatMap(list -> {
                    log.info("processor receive group of " + list.size());
                    Stream<ClassB> manipulatedStream = list.stream().map(msg -> {
                        ClassB manipulated = ClassB
                                .builder()
                                .value(String.format("YYY %s", msg.getValue()))
                                .build();
                        log.infof("processor manipulated message %s", manipulated);
                        return manipulated;
                    });
                    return Multi.createFrom().items(manipulatedStream);
                });
    }

}
