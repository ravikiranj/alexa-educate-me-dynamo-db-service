package com.alexa.educateme.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DynamoDbTopicResponse {
    @JsonProperty("metadata")
    private final ItemMetaData itemMetaData;

    @JsonProperty("facts")
    private final Map<String, String> items;

    @JsonCreator
    public DynamoDbTopicResponse(@JsonProperty("metadata") ItemMetaData itemMetaData, @JsonProperty("facts") Map<String, String> items) {
        this.itemMetaData = itemMetaData;
        this.items = items;
    }

    public ItemMetaData getItemMetaData() {
        return itemMetaData;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public String getItem(int index) {
        return items.get(String.valueOf(index));
    }

    public static class ItemMetaData {
        private final String topicId;

        @JsonCreator
        public ItemMetaData(@JsonProperty("topicId") String topicId) {
            this.topicId = topicId;
        }

        public String getTopicId() {
            return topicId;
        }
    }

}
