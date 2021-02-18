package mfasample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import mfasample.config.WebConfig;

@SpringBootApplication
@Import(WebConfig.class)
public class MFASampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MFASampleApplication.class, args);
	}

}
