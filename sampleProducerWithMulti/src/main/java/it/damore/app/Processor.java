package it.damore.app;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.smallrye.reactive.messaging.annotations.Blocking;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import it.damore.utils.CustomThreadPoolProducer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ApplicationScoped
public class Processor {

    private Random random = new Random();
    protected final Logger log;

    private final static ExecutorService pool = CustomThreadPoolProducer.getPoolWithName(Processor.class.getName());

//    private Integer maxGroupSize = 100;
//    private Integer maxDelay = 10;
    private Integer maxRetry = 30;
    private Duration initialBackOff = Duration.ofSeconds(2);
    private Integer sleep = 5000;
    protected Processor() {
        this.log = Logger.getLogger(getClass());
    }

    //    @Blocking(value = "processor-custom-pool", ordered = false)
//    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1)
    @Incoming("from-producer-to-processor")
    @Outgoing("from-processor-to-consumer")
    public Multi<List<ClassB>> consumeMulti2Multi(Multi<ClassA> stream) {
        return stream
                .group()
                .intoLists()
                .of(100)
                .flatMap(l -> Multi
                            .createFrom()
                            .deferred(() -> Multi.createFrom().item(this.convert(l)))
                            .onFailure(t -> {
                                log.error(t.getMessage());
                                return true;
                            })
                            .retry()
                            .withBackOff(this.initialBackOff)
                            .withJitter(.4)
                            .atMost(this.maxRetry)
                );
    }

    public List<ClassB> deferred(final List<ClassA> msgList) throws Exception {
        return Uni.createFrom()
                .deferred(() -> msgList, i -> Uni.createFrom().item(this.convert(i)))
                .onFailure()
                .retry()
                .indefinitely()
                .subscribe()
                .asCompletionStage().get();
    }

    public List<ClassB> convert(List<ClassA> msgList) {
        int i = random.nextInt();
        if (i % 2 != 0) {
            String errMsg = String.format("Random Ex %s - %s ", i, msgList.get(0));
            throw new RuntimeException(errMsg);
        }
        longExecution();
        return msgList
                .stream()
                .map(msg -> new ClassB(String.format("YYY %s", msg.getValue())))
                .collect(Collectors.toList());
    }

    public void longExecution() {
        try {
            Thread.sleep(this.sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
