package the_t.mainproject;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SpilltheTeaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpilltheTeaApplication.class, args);
	}

}
