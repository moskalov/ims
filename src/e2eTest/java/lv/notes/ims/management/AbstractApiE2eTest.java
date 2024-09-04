package lv.notes.ims.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-e2e.properties")
@Sql(scripts = "/clean.sql", executionPhase = BEFORE_TEST_METHOD)
@Transactional(propagation = Propagation.NEVER)
public class AbstractApiE2eTest {
    protected String baseUri;

    @Autowired
    protected PlatformTransactionManager transactionManager;
    protected TransactionTemplate transaction;

    @Autowired
    protected ObjectMapper mapper;

    @LocalServerPort
    protected int port;

    public void setUp() {
        baseUri = "http://localhost:" + port;
        transaction = new TransactionTemplate(transactionManager);
    }

}
