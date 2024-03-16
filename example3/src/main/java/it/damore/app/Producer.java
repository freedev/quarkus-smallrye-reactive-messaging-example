package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import static mutiny.zero.ZeroPublisher.fromStream;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@ApplicationScoped
public class Producer {
    private AtomicInteger counter = new AtomicInteger();
    protected final Logger log  = Logger.getLogger(getClass());
    @Outgoing("from-producer-to-processor")
    public Multi<ClassA> producer() {
        return Multi.createFrom()
                .publisher(fromStream(this::getStream));
    }
    private Stream<ClassA> getStream()
    {
        return Stream.generate(() -> {
                    ClassA classA = new ClassA("Hello " + counter.getAndIncrement());
//                    log.infof("Producing %s", classA);
                    return classA;
                });
    }
}
