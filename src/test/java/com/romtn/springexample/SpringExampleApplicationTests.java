package com.romtn.springexample;

import com.romtn.springexample.util.Timer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringExampleApplicationTests {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void contextLoads() {
		Timer timer = Timer.init();
		logger.info("contextLoads costs:{}ms", timer.mark().getCosts());
	}

}
