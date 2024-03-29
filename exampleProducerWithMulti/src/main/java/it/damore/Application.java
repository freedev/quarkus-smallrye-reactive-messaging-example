package it.damore;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import it.damore.app.Producer;

import jakarta.inject.Inject;

@QuarkusMain
public class Application implements QuarkusApplication {

    @Inject
    Producer producer;
    @Override
    public int run(String... args) throws Exception {
//        producer.periodicallySendMessage();
        Quarkus.waitForExit();
        return 0;
    }
}
