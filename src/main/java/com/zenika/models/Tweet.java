package com.zenika.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(builder = Tweet.Builder.class)
@SuppressWarnings("unused")
public interface Tweet {
    @JsonGetter("id") Long id();
    @JsonGetter("createdAt") Long createdAt();
    @JsonGetter("username") String username();
    @JsonGetter("lang") String lang();
    @JsonGetter("retweet") Boolean retweet();
    @JsonGetter("text") String text();
    @JsonGetter("translatedText") String translatedText();

    static Tweet from(Long id,
                      Long createdAt,
                      String username,
                      String lang,
                      Boolean retweet,
                      String text) {
        return from(id, createdAt, username, lang, retweet, text, null);
    }

    static Tweet from(Long id,
                      Long createdAt,
                      String username,
                      String lang,
                      Boolean retweet,
                      String text,
                      String translatedText) {
        return new Tweet() {
            @Override
            public Long id() {
                return id;
            }

            @Override
            public Long createdAt() {
                return createdAt;
            }

            @Override
            public String username() {
                return Optional.ofNullable(username).orElse("unknown");
            }

            @Override
            public String lang() {
                return lang;
            }

            @Override
            public Boolean retweet() {
                return retweet;
            }

            @Override
            public String text() {
                return text;
            }

            @Override
            public String translatedText() {
                return Optional.ofNullable(translatedText).orElse(text);
            }

            @Override
            public int hashCode() {
                return Objects.hash(id(),
                        createdAt(),
                        username(),
                        lang(),
                        retweet(),
                        text());
            }

            @Override
            public boolean equals(Object obj) {
                if(obj == this)
                    return true;
                if (!(obj instanceof Tweet))
                    return false;
                var that = (Tweet) obj;
                return Objects.equals(id(), that.id())
                    && Objects.equals(createdAt(), that.createdAt())
                    && Objects.equals(username(), that.username())
                    && Objects.equals(lang(), that.lang())
                    && Objects.equals(retweet(), that.retweet())
                    && Objects.equals(text(), that.text());

            }

            @Override
            public String toString() {
                return "{\n    \"id\":" + id() + "," +
                        "\n    \"createdAt\": " + createdAt() + "," +
                        "\n    \"username\": " + nullAsStringIfEmpty(username()) + "," +
                        "\n    \"lang\": " + nullAsStringIfEmpty(lang()) + "," +
                        "\n    \"retweet\": " + retweet() + "," +
                        "\n    \"text\": " + nullAsStringIfEmpty(text()) + "," +
                        "\n    \"translatedText\": " + nullAsStringIfEmpty(translatedText()) +
                        "\n}";
            }

            private String nullAsStringIfEmpty(String string) {
                return Optional.ofNullable(string)
                        .map(s -> "\"".concat(s).concat("\""))
                        .orElse("null");
            }
        };
    }

    default Tweet translated(String translatedText) {
        return from(id(), createdAt(), username(), lang(), retweet(), text(), translatedText);
    }

    @JsonPOJOBuilder(withPrefix = "")
    class Builder {
        private Long id;
        private Long createdAt;
        private String username;
        private String lang;
        private Boolean retweet;
        private String text;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder retweet(Boolean retweet) {
            this.retweet = retweet;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Tweet build() {
            return Tweet.from(id, createdAt, username, lang, retweet, text);
        }

    }
}
