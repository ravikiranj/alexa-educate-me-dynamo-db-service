package com.alexa.educateme.util;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;

import java.util.HashMap;
import java.util.Map;

public final class DynamoDbHelper {

    private static final String TOPIC_NAME = "topic_name";
    private static final String TOPICS = "Topics";
    private static final String SERVICE_ENDPOINT = "https://dynamodb.us-west-2.amazonaws.com";
    private static final String US_WEST_2 = "us-west-2";

    private DynamoDbHelper() {

    }


    public static Map<String, AttributeValue> getDynamoDbResponse(String topic) {
        HashMap<String,AttributeValue> key = new HashMap<>();
        key.put(TOPIC_NAME, new AttributeValue(topic));

        GetItemRequest keyRequest = new GetItemRequest()
            .withKey(key)
            .withTableName(TOPICS);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                           .withEndpointConfiguration(new EndpointConfiguration(SERVICE_ENDPOINT, US_WEST_2))
                                                           .build();

        return client.getItem(keyRequest).getItem();
    }

}
