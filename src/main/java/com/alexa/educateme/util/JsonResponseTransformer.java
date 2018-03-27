package com.alexa.educateme.util;

import spark.ResponseTransformer;

public class JsonResponseTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) throws Exception {
        return ObjectMapperHelper.toJson(model);
    }
}
