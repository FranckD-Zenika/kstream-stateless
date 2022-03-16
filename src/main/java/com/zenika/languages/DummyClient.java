package com.zenika.languages;

import com.zenika.languages.interfaces.LanguageClient;
import com.zenika.models.EntitySentiment;
import com.zenika.models.Tweet;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class DummyClient implements LanguageClient {

    private static final String TRANSLATE_FORMAT = "Translated from : %s to : %s => %s";

    @Override
    public Tweet translate(Tweet tweet, String targetedLanguage) {
        return tweet.translated(String.format(TRANSLATE_FORMAT, tweet.lang(), targetedLanguage, tweet.text()));
    }

    @Override
    public List<EntitySentiment> entitySentiments(Tweet tweet) {
        return Stream.of(tweet.translatedText().toLowerCase().replace("#", "").split(" "))
                .map(entity -> EntitySentiment.newBuilder()
                        .setCreatedAt(tweet.createdAt())
                        .setId(tweet.id())
                        .setEntity(entity)
                        .setText(tweet.translatedText())
                        .setSalience(randomDouble())
                        .setSentimentScore(randomDouble())
                        .setSentimentMagnitude(randomDouble())
                        .build())
                .collect(Collectors.toList());
    }

    private   Double randomDouble() {
        return ThreadLocalRandom.current().nextDouble(0, 1);
    }

}
