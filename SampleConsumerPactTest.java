

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleConsumerPactTest {
    private static final String PROVIDER = "sample-provider";
    private static final String CLIENT = "sample-consumer";
    private static final String PACT_BRKER = "HOST";

    public SampleConsumerPactTest() throws IOException{

    }
    ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(PROVIDER,this);

    Object[] response =
            objectMapper.readValue(new File(getClass().getClassLoader().getResource("sample-response.json").getFile()),
                    Object[].class);

    @Pact(consumer = CLIENT)
    public RequestResponsePact testPact(PactDslWithProvider builder) throws Exception {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return builder
                .given("testPact")
                .uponReceiving("Request To retrieve Response")
                .path("/testEndPoint")
                .query("id=123")
                .method(HttpMethod.GET.name())
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(headers)
                .body(objectMapper.writeValueAsString(response))
                .toPact();
    }

    @Test
    @PactVerification(value = PROVIDER, fragment = "testPact")
    public void testPactConsumer() throws Exception {
        ResponseEntity<Object[]> response = new RestTemplate()
                .getForEntity(mockProvider.getUrl() + "/testEndpoint?id=123", Object[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()[0].getValue()).isEqualTo("test");
    }

}

