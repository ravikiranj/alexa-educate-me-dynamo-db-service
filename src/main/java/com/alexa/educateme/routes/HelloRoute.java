package com.alexa.educateme.routes;

import com.alexa.educateme.models.HelloWorldResponse;
import spark.Request;
import spark.Response;
import spark.Route;

public class HelloRoute implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        HelloWorldResponse resp = new HelloWorldResponse(1, "abc");
        return resp;
    }
}
