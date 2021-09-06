package sit.int222.cfan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService implements StorageService {
    final Path rootLocation;

    @Autowired
    public FileStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String store(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        int extPos = fileName.lastIndexOf(".");
        if (extPos > 0) {
            fileName = file.getOriginalFilename().substring(0, extPos);
        }
        return store(file, fileName);
    }

    @Override
    public String store(MultipartFile file, String fileName) throws Exception {
        int extPos = file.getOriginalFilename().lastIndexOf(".");
        if (extPos > 0) {
            fileName += generateString() + file.getOriginalFilename().substring(extPos).toLowerCase();
        }
        try {
            if (file.isEmpty()) {
                throw new IOException("file : " + fileName + " is Empty");
            }
            if (fileName.contains("..")) {
                throw new IOException("fileName : " + fileName + " is relative path");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
        return fileName;
    }

    @Override
    public Stream<Path> loadAll() throws IOException {
        Stream<Path> stream = Files.list(this.rootLocation);
        return stream;
    }

    @Override
    public Path load(String fileName) throws IOException {
        Path path = this.rootLocation.resolve(fileName);
        File file = new File(String.valueOf(path.toString()));
        if (!file.isFile()) {
            throw new IOException("file : " + fileName + " is does not exist!!");
        }
        return path;
    }

    @Override
    public Resource loadAsResource(String fileName) throws IOException {
        try {
            Resource resource = new UrlResource(load(fileName).toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    @Override
    public void deleteAll() throws IOException {
        loadAll().map(path -> {
            File file = new File(String.valueOf(path.toString()));
            return file.delete();
        });
    }

    @Override
    public void delete(String fileName) throws IOException {
        load(fileName).toFile().delete();
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return "-" + uuid;
    }
}
