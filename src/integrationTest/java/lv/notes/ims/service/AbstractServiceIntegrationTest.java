package lv.notes.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.context.annotation.FilterType.REGEX;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-integration.properties")
@Sql(scripts = "/clean.sql", executionPhase = BEFORE_TEST_METHOD)
@Transactional(propagation = Propagation.NEVER)
@DataJpaTest(includeFilters = {
        @Filter(type = REGEX, pattern = "lv.notes.ims.service.*"),
        @Filter(type = REGEX, pattern = "lv.notes.ims.mapper.*"),
})
public abstract class AbstractServiceIntegrationTest {
    @Autowired
    protected PlatformTransactionManager transactionManager;
}
