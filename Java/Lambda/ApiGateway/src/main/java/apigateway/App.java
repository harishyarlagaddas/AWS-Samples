package apigateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {

    private static final String HTTP_METHOD_KEY = "httpMethod";
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    public Object handleRequest(final Object input, final Context context) {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        responseHeaders.put("X-Custom-Header", "application/json");

        if (!HashMap.class.isInstance(input)) {
            System.out.println("input is not hashmap. Doesn't know how to process. Input: "+input);
            return new GatewayResponse("{}", responseHeaders, 500);
        }

        HashMap<String, String> request = (HashMap)input;
        System.out.println(request);

        JsonObject result = null;
        if (request.containsKey(HTTP_METHOD_KEY)) {
            String method = request.get(HTTP_METHOD_KEY);
            System.out.println("HttpMethod: "+method);
            if (method.equalsIgnoreCase("GET")) {
                result = getGetRequestResponse();
            } else if (method.equalsIgnoreCase("POST")) {
                result = getPostRequestResponse();
            }
        }

        if (null == result) {
            System.out.println("Either httpMethod not present of it is not GET or POST.");
            return new GatewayResponse("{Only GET and POST are supported.}", responseHeaders, 500);
        }

        return new GatewayResponse(new Gson().toJson(result), responseHeaders, 200);
    }

    private JsonObject getGetRequestResponse() {
        JsonObject result = new JsonObject();
        result.addProperty("success",true);
        result.addProperty("method", "GET");
        result.addProperty("message", "Welcome to API Gateway and lambda integration - GET on /hello");
        result.addProperty("location", getPageContents("https://checkip.amazonaws.com"));
        return result;
    }

    private JsonObject getPostRequestResponse() {
        JsonObject result = new JsonObject();
        result.addProperty("success",true);
        result.addProperty("Method", "POST");
        result.addProperty("message", "Welcome to API Gateway and lambda integration - POST on /hello");
        result.addProperty("location", getPageContents("https://checkip.amazonaws.com"));
        return result;
    }

    private String getPageContents(String address) {
        try {
            URL url = new URL(address);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return br.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException ex) {
            System.out.println("Exception while making api call to checkip.amazonaws.com");
            System.out.println(ex);
            return "Error fetching the location";
        }
    }
}
