package com.example.SpringBoot3Log4j2AppSvcLogs;

import org.apache.commons.logging.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class SpringBoot3Log4j2AppSvcLogsApplication {

    private Log logger = LogFactory.getLog(SpringBoot3Log4j2AppSvcLogsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3Log4j2AppSvcLogsApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		String output = String.format("Java version: %s. Log date: %s", System.getProperty("java.version"), new java.util.Date());
		logger.info(output);
		return output;
	}

	@GetMapping("/warn")
	public String warn() {
		logger.warn("This is a WARNING from the /warn endpoint");
		return "Warning page!";
	}

	@GetMapping("/error")
	public String error() {
		throw new RuntimeException("This endpoint throws intentionally.");
	}

}
