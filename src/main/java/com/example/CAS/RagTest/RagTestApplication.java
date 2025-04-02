package com.example.CAS.RagTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.standard.ShellComponent;

@ImportRuntimeHints(HintsRegistrar.class)
@ShellComponent
@CommandScan
@SpringBootApplication
public class RagTestApplication {

	private static final Logger log = LoggerFactory.getLogger(RagTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RagTestApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			log.info("Spring Assistant is running!");
		};
	}

}
