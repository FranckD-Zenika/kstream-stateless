package com.zenika.languages.interfaces;

import com.zenika.models.EntitySentiment;
import com.zenika.models.Tweet;

import java.util.List;

public interface LanguageClient {
    Tweet translate(Tweet tweet, String targetedLanguage);
    List<EntitySentiment> entitySentiments(Tweet tweet);
}
