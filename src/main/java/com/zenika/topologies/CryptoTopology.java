package com.zenika.topologies;

import com.zenika.languages.interfaces.LanguageClient;
import com.zenika.models.EntitySentiment;
import com.zenika.models.Tweet;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Predicate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.List;

import static com.zenika.serdes.ProducedRecord.cryptoSentiment;
import static com.zenika.serdes.TweetSerde.tweetSerdes;

@ApplicationScoped
public class CryptoTopology {

    private static final List<String> CURRENCIES = List.of("bitcoin", "ethereum");
    private static final java.util.function.Predicate<EntitySentiment> IS_NOT_TARGETED_CURRENCY = entitySentiment -> !CURRENCIES.contains(entitySentiment.getEntity());
    private static final Predicate<? super Object, ? super Object> NOT_NULL = (key, value) -> value != null;
    private static final Predicate<? super Object, Tweet> RETWEET = (key, tweet) -> tweet.retweet();
    private static final Predicate<? super Object, Tweet> ENGLISH_TWEET = (key, tweet) -> tweet.lang().equals("en");


    private final LanguageClient languageClient;

    @Inject
    CryptoTopology(LanguageClient languageClient) {
        this.languageClient = languageClient;
    }

    @Produces
    public Topology build() {
        var builder = new StreamsBuilder();
        var stream = builder.stream("tweets", Consumed.with(Serdes.String(), tweetSerdes()))
                .filter(NOT_NULL)
                .filterNot(RETWEET);
        var translatedStream = stream.filterNot(ENGLISH_TWEET)
                .map((s, tweet) -> KeyValue.pair(tweet.username(), languageClient.translate(tweet, "en")));
        stream.filter(ENGLISH_TWEET)
                .merge(translatedStream)
                .flatMapValues(this::enrich)
                .to("crypto-sentiment", cryptoSentiment());
        return builder.build();
    }

    private List<EntitySentiment> enrich(Tweet tweet) {
        var result = languageClient.entitySentiments(tweet);
        result.removeIf(IS_NOT_TARGETED_CURRENCY);
        return result;
    }

}
