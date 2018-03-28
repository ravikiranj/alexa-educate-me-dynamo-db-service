package com.alexa.educateme.routes;

import com.alexa.educateme.models.HelloWorldResponse;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class getid implements Route {

    public Object handle(Request request, Response response) throws Exception {

        String table_name = "Topics";
        String name = "Wolves";

        String output = "dummy";

        System.out.format("Retrieving item \"%s\" from \"%s\"\n",
            name, table_name);

        HashMap<String,AttributeValue> key = new HashMap<String,AttributeValue>();
        key.put("topic_name", new AttributeValue(name));

        GetItemRequest keyRequest = new GetItemRequest()
            .withKey(key)
            .withTableName(table_name);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new EndpointConfiguration("https://dynamodb.us-west-2.amazonaws.com", "us-west-2"))
                                                           .build();

        Map<String,AttributeValue> returned_key = client.getItem(keyRequest).getItem();

        if (returned_key != null) {
            Set<String> keys = returned_key.keySet();
            for (String k : keys) {
                System.out.format("%s: %s\n",
                    k, returned_key.get(k).toString());
            }
            } else {
                System.out.format("No item found with the key %s!\n", name);
            }


        HelloWorldResponse resp = new HelloWorldResponse(1, output);
        return resp;
    }
}
