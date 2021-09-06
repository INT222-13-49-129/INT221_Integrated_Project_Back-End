package sit.int222.cfan.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface StorageService {
    void init();

    String store(MultipartFile file) throws Exception;

    String store(MultipartFile file, String fileName) throws Exception;

    Stream<Path> loadAll() throws IOException;

    Path load(String fileName) throws IOException;

    Resource loadAsResource(String fileName) throws IOException, URISyntaxException;

    void deleteAll() throws IOException;

    void delete(String fileName) throws IOException;
}
