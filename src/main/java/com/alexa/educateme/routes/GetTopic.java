package com.alexa.educateme.routes;

import com.alexa.educateme.models.DynamoDbErrorResponse;
import com.alexa.educateme.models.DynamoDbTopicResponse;
import com.alexa.educateme.models.TopicResponse;
import com.alexa.educateme.util.DynamoDbHelper;
import com.alexa.educateme.util.ObjectMapperHelper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;
import java.util.logging.Logger;

public class GetTopic implements Route {
    private static final Logger LOG = Logger.getLogger("GetTopic");

    private static final String TOPIC = "topic";
    private static final String INDEX = "index";
    private static final String DATA = "data";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String topic = request.queryParams(TOPIC);
        String index = request.queryParams(INDEX);

        if (StringUtils.isBlank(topic) || StringUtils.isBlank(index )) {
            response.status(HttpStatus.BAD_REQUEST_400);
            return new DynamoDbErrorResponse("topic/index cannot be empty");
        }

        Map<String, AttributeValue> returned_key = DynamoDbHelper.getDynamoDbResponse(topic);

        if (returned_key != null && returned_key.get(DATA) != null) {
            DynamoDbTopicResponse dynamoDbTopicResponse = ObjectMapperHelper.OBJECT_MAPPER.readValue(returned_key.get(DATA).getS(), DynamoDbTopicResponse.class);
            LOG.info("Value returned for topic = " + dynamoDbTopicResponse);

            String fact = dynamoDbTopicResponse.getItem(index );
            if (StringUtils.isNotBlank(fact)) {
                response.status(HttpStatus.OK_200);
                return new TopicResponse(topic, dynamoDbTopicResponse.getItemMetaData().getTopicId(), index , dynamoDbTopicResponse.getItem(index ));
            } else {
                response.status(HttpStatus.NOT_FOUND_404);
                return new DynamoDbErrorResponse("Could not find topic details for topic = " + topic + ", index = " + index);
            }
        } else {
            String error = "Could not find topic details for " + topic;
            LOG.info(error);
            response.status(HttpStatus.NOT_FOUND_404);
            return new DynamoDbErrorResponse(error);
        }
    }
}
