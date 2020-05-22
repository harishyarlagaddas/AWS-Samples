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

    public Object handleRequest(final Object input, final Context context) {
        System.out.print(input);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        JsonObject result = getGetRequestResponse();
        return new GatewayResponse(new Gson().toJson(result), headers, 200);
    }

    private JsonObject getGetRequestResponse() {
        JsonObject result = new JsonObject();
        result.addProperty("success",true);
        result.addProperty("method", "GET");
        result.addProperty("message", "Welcome to API Gateway and lambda integration - GET /hello");
        result.addProperty("location", getPageContents("https://checkip.amazonaws.com"));
        return result;
    }

    private JsonObject getPostRequestResponse() {
        JsonObject result = new JsonObject();
        result.addProperty("success",true);
        result.addProperty("Method", "POST");
        result.addProperty("message", "Welcome to API Gateway and lambda integration - POST /hello");
        result.addProperty("Location", getPageContents("https://checkip.amazonaws.com"));
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
