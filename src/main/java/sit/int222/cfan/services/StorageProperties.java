package sit.int222.cfan.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cfan.storage")
@Data
public class StorageProperties {
    private String location;
}
