package sit.int222.cfan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sit.int222.cfan.services.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
public class CfanApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfanApplication.class, args);
	}

}
