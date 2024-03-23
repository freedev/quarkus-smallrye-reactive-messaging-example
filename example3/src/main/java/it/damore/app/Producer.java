package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import static mutiny.zero.ZeroPublisher.fromStream;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

@ApplicationScoped
public class Producer {
    @Outgoing("from-producer-to-processor")
    public Multi<ClassA> producer() {
        return Multi.createFrom()
                .publisher(fromStream(this::getStream));
    }

    private Stream<ClassA> getStream()
    {
        return Stream.generate(() -> {
                    ClassA classA = new ClassA("Hello " + Utils.counter.getAndIncrement());
                    log.infof("Producing %s", classA);
                    return classA;
                });
    }

    protected final Logger log  = Logger.getLogger(getClass());
}
