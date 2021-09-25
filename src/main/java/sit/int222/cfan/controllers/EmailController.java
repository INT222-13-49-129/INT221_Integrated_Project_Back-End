package sit.int222.cfan.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.services.EmailService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailController {
    @Autowired
    EmailService emailService;
    @Autowired
    private ResourceLoader resourceLoader;

    public void sendPinEmail(String email, String name, String pin) {
        String html;
        try {
            html = readEmailTemplate("email-send-pin.html");
        } catch (IOException e) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.EMAIL_TEMPLATE_NOT_FOUND, "EMAIL : Email template not found !!");
        }

        html = html.replace("${NAME}", name);
        html = html.replace("${EMAIL}", email);
        html = html.replace("${PIN}", pin);

        String subject = "Security code";

        emailService.send(email, subject, html);
    }

    private String readEmailTemplate(String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:email/" + filename);

        InputStream inputStream = resource.getInputStream();

        Assert.notNull(inputStream, "Could not load template resource!");

        String email = null;

        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            email = new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        return email;
    }
}
