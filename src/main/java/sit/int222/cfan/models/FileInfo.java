package sit.int222.cfan.models;

import lombok.Data;

@Data
public class FileInfo {
    private String fileName;
    private String url;

    public FileInfo(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
