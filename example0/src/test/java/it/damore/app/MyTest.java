package it.damore.app;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.component.QuarkusComponentTest;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import it.damore.models.ClassA;
import it.damore.models.ClassB;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.*;

import java.util.List;

@QuarkusTest
@QuarkusComponentTest
@Disabled
public class MyTest {

    // 1. Switch the channels to the in-memory connector:
    @BeforeAll
    public static void switchMyChannels() {
//        InMemoryConnector.switchOutgoingChannelsToInMemory("from-producer-to-processor");

        InMemoryConnector.switchIncomingChannelsToInMemory("from-producer-to-processor");
        InMemoryConnector.switchOutgoingChannelsToInMemory("from-processor-to-consumer");

//        InMemoryConnector.switchIncomingChannelsToInMemory("from-processor-to-consumer");
    }

    // 2. Don't forget to reset the channel after the tests:
    @AfterAll
    public static void revertMyChannels() {
        InMemoryConnector.clear();
    }

    // 3. Inject the in-memory connector in your test,
    // or use the bean manager to retrieve the instance
    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void test() throws InterruptedException {


        // 4. Retrieves the in-memory source to send message
        InMemorySource<ClassA> sourced = connector.source("from-producer-to-processor");
        // 5. Retrieves the in-memory sink to check what is received
        InMemorySink<ClassB> results = connector.sink("from-processor-to-consumer");

        // 6. Send fake messages:
        sourced.send(new ClassA("Hello 1"));
        sourced.send(new ClassA("Hello 2"));
        sourced.send(new ClassA("Hello 3"));
        sourced.complete();
        Thread.sleep(1000);
        List<? extends Message<ClassB>> list = results.received().stream().toList();
        int size = results.received().size();

        // 7. Check you have receives the expected messages
        Assertions.assertEquals(3, size);
    }
}