import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
@RunWith(PactRunner.class)
@Provider("sample-provider")
//need to connect to borker to fetch contracts for testing them
@PactBroker(host = "localhost", port = "80", tags ="latest")
public class SampleProviderTest {

    public EventDetailsProviderTest() throws Exception{ }
    @InjectMocks
    SampleController sampleController;

    @Mock
    SampleService sampleService;

  //run app in local
    @TestTarget
    public final Target target = new HttpTarget("localhost", 8080);

    @Before
    public void setUp() {
        SpringApplication.run(Application.java);
    }

  //map the state value to execute the contract test
    @State("testPact")
    public void testEventDetails() {
       
    }
}
