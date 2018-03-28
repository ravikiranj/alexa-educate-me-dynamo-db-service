package com.alexa.educateme.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicIdResponse {
    private final String topic;
    private final String id;

    @JsonCreator
    public TopicIdResponse(@JsonProperty("topic") String topic, @JsonProperty("id") String id) {
        this.topic = topic;
        this.id = id;
    }

}
