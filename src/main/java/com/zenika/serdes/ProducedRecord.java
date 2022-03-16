package com.zenika.serdes;

import com.zenika.models.EntitySentiment;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Produced;

public interface ProducedRecord {

    static Produced<String, EntitySentiment> cryptoSentiment() {
        return Produced.with(Serdes.String(), AvroSerdes.sentimentSerde());
    }

}
