package it.damore.app;

import io.smallrye.mutiny.Multi;
import it.damore.models.ClassA;
import it.damore.utils.Utils;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static mutiny.zero.ZeroPublisher.*;

@ApplicationScoped
public class Producer {
    @Outgoing("from-producer-to-processor")
    public Multi<ClassA> producer() {
        return Multi.createFrom()
                .publisher(fromStream(this::getInfinteStream));
//        return Multi.createFrom()
//                .publisher(fromIterable(this::getFiniteStream));
    }

    private Iterator<ClassA> getFiniteStream()
    {
        return IntStream.range(0, 1).mapToObj(i -> new ClassA("Hello " + i))
                .peek(c -> log.info("Producer emitting " + c))
                .peek(c -> Utils.longExecution()).toList().iterator();
    }

    private Stream<ClassA> getInfinteStream()
    {
        return Stream.generate(() -> {
                    ClassA classA = new ClassA("Hello " + Utils.counter.getAndIncrement());
                    log.infof("Producing %s", classA);
                    return classA;
                });
    }

    protected final Logger log  = Logger.getLogger(getClass());
}
