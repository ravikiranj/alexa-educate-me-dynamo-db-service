package com.alexa.educateme.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TopicResponse {
    private final String topic;
    private final String topicId;
    private final String id;
    private final String fact;

    public TopicResponse(String topic, String topicId, String id, String fact) {
        this.topic = topic;
        this.topicId = topicId;
        this.id = id;
        this.fact = fact;

    }
}
