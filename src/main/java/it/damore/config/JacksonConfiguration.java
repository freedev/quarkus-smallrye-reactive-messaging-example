package it.damore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.Locale;

/**
 * Jackson Configuration
 * Further details https://quarkus.io/guides/rest-json#configuring-json-support
 */
@Singleton
public class JacksonConfiguration implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper mapper) {
        mapper
            .setLocale(Locale.getDefault());
    }

    @Produces
//    @ApplicationScoped
    public ObjectMapper objectMapper(Instance<ObjectMapperCustomizer> customizers){
        ObjectMapper objectMapper = new ObjectMapper();
        for (ObjectMapperCustomizer customizer : customizers){
            customizer.customize(objectMapper);
        }
        return objectMapper;
    }

}
