package com.springheaven.gom.canary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainCanaryRunner {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext=SpringApplication.run(MainCanaryRunner.class, args);
		String overallResult= applicationContext.getBean(Canary.class).runAllTests();
		System.out.println(overallResult);
		applicationContext.close();

	}

}
