package com.zenika.serdes;

import com.zenika.models.Tweet;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TweetSerde implements Serde<Tweet> {

    private static final TweetSerde SERDES = new TweetSerde();

    @Override
    public Serializer<Tweet> serializer() {
        return (s, tweet) -> Optional.ofNullable(tweet)
                .map(SerdesUtil::writeValueAsByte)
                .orElse(null);
    }

    @Override
    public Deserializer<Tweet> deserializer() {
        return (s, bytes) -> Optional.ofNullable(bytes)
                .map(b -> SerdesUtil.readValue(b, Tweet.class))
                .orElse(null);
    }

    public static TweetSerde tweetSerdes() {
        return SERDES;
    }

    private TweetSerde() {}

}
