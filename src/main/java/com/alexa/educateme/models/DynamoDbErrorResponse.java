package com.alexa.educateme.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DynamoDbErrorResponse {
    private final String error;

    @JsonCreator
    public DynamoDbErrorResponse(@JsonProperty("error") String error) {
        this.error = error;
    }
}
