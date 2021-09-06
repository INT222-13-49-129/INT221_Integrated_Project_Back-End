package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.services.EmailService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class EmailController {
    @Autowired
    EmailService emailService;

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
        File file = ResourceUtils.getFile("classpath:email/" + filename);
        return FileCopyUtils.copyToString(new FileReader(file));
    }
}
