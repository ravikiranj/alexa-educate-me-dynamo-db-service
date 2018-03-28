package com.alexa.educateme.routes;

import com.alexa.educateme.models.DynamoDbErrorResponse;
import com.alexa.educateme.models.DynamoDbTopicResponse;
import com.alexa.educateme.models.TopicIdResponse;
import com.alexa.educateme.util.DynamoDbHelper;
import com.alexa.educateme.util.ObjectMapperHelper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sun.deploy.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
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
            TopicIdResponse resp = getResponseFromWikipediaScraper(topic);
            if (resp != null) {
                return resp;
            }

            // We failed, just send 404
            String error = "Could not find topic details for " + topic;
            LOG.info(error);
            response.status(HttpStatus.NOT_FOUND_404);
            return new DynamoDbErrorResponse(error);
        }
    }

    private TopicIdResponse getResponseFromWikipediaScraper(String topic) {
        try {
            String url = String.format("http://ec2-54-218-93-249.us-west-2.compute.amazonaws.com/topic/%s/", URLEncoder.encode(topic, "UTF-8"));
            LOG.info("Making an API call to " + url + ", since we didn't find it in dynamo db");
            try (InputStream is = new URL(url).openStream()){
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                return ObjectMapperHelper.OBJECT_MAPPER.readValue(jsonText, TopicIdResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info("Encountered exception = " + e);
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
