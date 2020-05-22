package apigateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

public class AppTest {
  @Test
  public void successfulResponse() {
    App app = new App();
    HashMap<String, String> request = new HashMap<>();
    request.put("httpMethod", "POST");
    GatewayResponse result = (GatewayResponse) app.handleRequest(request, null);
    assertEquals(result.getStatusCode(), 200);
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("message"));
    assertTrue(content.contains("location"));
  }
}
