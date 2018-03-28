package com.alexa.educateme.routes;

import com.alexa.educateme.models.DynamoDbErrorResponse;
import com.alexa.educateme.models.DynamoDbTopicResponse;
import com.alexa.educateme.models.HelloWorldResponse;
import com.alexa.educateme.models.TopicIdResponse;
import com.alexa.educateme.util.DynamoDbHelper;
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

    public Object handle(Request request, Response response) throws Exception {
        String topic = request.queryParams(TOPIC);

        if (StringUtils.isBlank(topic)) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return new DynamoDbErrorResponse("topic cannot be empty");
        }

        Map<String, AttributeValue> returned_key = DynamoDbHelper.getDynamoDbResponse(topic);

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
