package com.alexa.educateme;

import com.alexa.educateme.routes.HelloRoute;
import com.alexa.educateme.routes.GetTopicId;
import com.alexa.educateme.util.JsonResponseTransformer;
import spark.ResponseTransformer;

import java.util.logging.Logger;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.threadPool;

public class DynamoDbApiHandler {

    private static final ResponseTransformer JSON_RESP_TRANSFORMER = new JsonResponseTransformer();
    private static final int maxThreads = 8;
    private static final int minThreads = 2;
    private static final int timeOutMillis = 30000;

    private static final Logger LOG = Logger.getLogger("DynamoDbApiHandler");
    private static final int PORT = 80;

    public static void main(String[] args) {
        // Config
        port(PORT);
        threadPool(maxThreads, minThreads, timeOutMillis);

        // Endpoints
        get("/hello", new HelloRoute(), JSON_RESP_TRANSFORMER);
        get("/getTopicId", new GetTopicId(), JSON_RESP_TRANSFORMER);

        // Filters
        after(((request, response) -> {
            response.type("application/json");
        }));

        LOG.info("Started server at port = " + PORT);
    }

}
