package com.zenika.serdes;

import com.zenika.models.EntitySentiment;
import io.apicurio.registry.serde.avro.AvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Map;

public class AvroSerdes {

    private static final Map<String, ?> SERDES_CONFIGURATION = Map.of(
            "apicurio.registry.url", ConfigProvider.getConfig().getValue("kafka.apicurio.registry.url", String.class),
            "apicurio.registry.auto-register", ConfigProvider.getConfig().getValue("kafka.apicurio.registry.auto-register", Boolean.class),
            "apicurio.registry.artifact-resolver-strategy", ConfigProvider.getConfig().getValue("kafka.apicurio.registry.artifact-resolver-strategy", String.class)
    );

    public static Serde<EntitySentiment> sentimentSerde() {
        var serde = new AvroSerde<EntitySentiment>();
        serde.configure(SERDES_CONFIGURATION, false);
        return serde;

    }

    private AvroSerdes() {}

}
