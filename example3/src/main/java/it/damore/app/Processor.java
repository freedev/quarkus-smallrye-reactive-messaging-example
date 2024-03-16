package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Processor {
    protected final Logger log;
    protected Processor(
                    ) {
        this.log = Logger.getLogger(getClass());
    }

    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    public Multi<List<ClassB>> multi2multi(Multi<ClassA> stream) {
        return stream
                .emitOn(Utils.getPoolWithName("converter-pool"))
//                .emitOn(pool)
                .group()
                .intoLists()
                .of(10)
                .onItem()
                .transformToUni(l -> Uni.createFrom()
                            .deferred(() -> {
//                                log.infof("received %s", l.size());
                                return Uni.createFrom().item(l);
                            })
                            .onItem()
                            .transform(Unchecked.function(this::convert))
                            .onFailure()
                            .retry()
                            .indefinitely()
                )
                .merge()
;
    }

    public List<ClassB> convert(List<ClassA> msgList) throws Exception {
        Random r = new Random();
        int i = r.nextInt();
        if (i % 7 != 0) {
            String errMsg = "Random exception raised";
            log.error(errMsg);
            throw new Exception(errMsg);
        }
        Utils.longExecution();
        List<ClassB> classBList = msgList
                .stream()
                .map(msg -> new ClassB(String.format("YYY %s", msg.value)))
                .collect(Collectors.toList());
        return classBList;
    }
}
