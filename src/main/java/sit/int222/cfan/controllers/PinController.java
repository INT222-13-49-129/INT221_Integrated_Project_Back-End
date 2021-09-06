package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Pin;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.PinRepository;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class PinController {
    @Autowired
    PinRepository pinRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailController emailController;

    public Pin createPin(User user, String email) {
        Pin opin = pinRepository.findByEmail(email);
        if (opin != null) {
            pinRepository.delete(opin);
        }

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000);
        String pinCode = String.format("%06d", num);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));
        calendar.add(Calendar.MINUTE, 10);
        Date expiresAt = calendar.getTime();

        Pin pin = new Pin();
        pin.setUser(user);
        pin.setEmail(email);
        pin.setExp(new Timestamp(expiresAt.getTime()));
        pin.setPincode(passwordEncoder.encode(pinCode));

        emailController.sendPinEmail(email, user.getUsername(), pinCode);
        return pinRepository.save(pin);
    }

    public User verify(String email, String pinCode) {
        Pin pin = pinRepository.findByEmail(email);
        if (pin == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.PIN_EMAIL_DOES_NOT_EXIST, "Pin : Email {" + email + "} does not exist !!");
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));
        Date date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());
        if (timestamp.compareTo(pin.getExp()) > 0) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.PIN_EXPIRED, "Pin : Pin expired !!");
        }

        if (!passwordEncoder.matches(pinCode, pin.getPincode())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.PIN_PINCODE_INCORRECT, "Pin : Pin code incorrect !!");
        }

        User user = pin.getUser();
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_IS_NULL, "User : User is null !!");
        }

        pinRepository.delete(pin);
        return user;
    }
}
