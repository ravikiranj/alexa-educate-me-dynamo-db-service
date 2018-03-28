package com.alexa.educateme.routes;

import com.alexa.educateme.models.DynamoDbErrorResponse;
import com.alexa.educateme.models.DynamoDbTopicResponse;
import com.alexa.educateme.models.HelloWorldResponse;
import com.alexa.educateme.models.TopicIdResponse;
import com.alexa.educateme.util.ObjectMapperHelper;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class GetTopicId implements Route {

    private static final Logger LOG = Logger.getLogger("GetTopicId");

    private static final String TOPIC = "topic";
    private static final String DATA = "data";
    private static final String TOPIC_NAME = "topic_name";
    private static final String TOPICS = "Topics";
    private static final String SERVICE_ENDPOINT = "https://dynamodb.us-west-2.amazonaws.com";
    private static final String US_WEST_2 = "us-west-2";

    public Object handle(Request request, Response response) throws Exception {
        String topic = request.queryParams(TOPIC);

        if (StringUtils.isBlank(topic)) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return new DynamoDbErrorResponse("topic cannot be empty");
        }

        HashMap<String,AttributeValue> key = new HashMap<>();
        key.put(TOPIC_NAME, new AttributeValue(topic));

        GetItemRequest keyRequest = new GetItemRequest()
            .withKey(key)
            .withTableName(TOPICS);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                           .withEndpointConfiguration(new EndpointConfiguration(SERVICE_ENDPOINT, US_WEST_2))
                                                           .build();

        Map<String, AttributeValue> returned_key = client.getItem(keyRequest).getItem();

        if (returned_key != null && returned_key.get(DATA) != null) {
            DynamoDbTopicResponse dynamoDbTopicResponse = ObjectMapperHelper.OBJECT_MAPPER.readValue(returned_key.get(DATA).getS(), DynamoDbTopicResponse.class);
            LOG.info("Value returned for topic = " + dynamoDbTopicResponse);

            response.status(HttpStatus.OK_200);
            return new TopicIdResponse(topic, dynamoDbTopicResponse.getItemMetaData().getTopicId());
        } else {
            String error = "Could not find topic details for " + topic;
            LOG.info(error);
            response.status(HttpStatus.NOT_FOUND_404);
            return new DynamoDbErrorResponse(error);
        }
    }
}
