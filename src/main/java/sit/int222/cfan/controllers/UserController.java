package sit.int222.cfan.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sit.int222.cfan.entities.Jwtblacklist;
import sit.int222.cfan.entities.Pin;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.models.*;
import sit.int222.cfan.repositories.JwtblacklistRepository;
import sit.int222.cfan.repositories.UserRepository;
import sit.int222.cfan.services.PinService;
import sit.int222.cfan.services.StorageService;
import sit.int222.cfan.services.TokenService;
import sit.int222.cfan.util.SecurityUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtblacklistRepository jwtblacklistRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenService tokenService;
    @Autowired
    StorageService storageService;
    @Autowired
    PinService pinService;

    public List<User> getUserAll() {
        return userRepository.findAll();
    }

    public Page<User> getUserPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long userid) {
        if (userid == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_INCORRECT_ID, "User : id null !!");
        }
        User user = userRepository.findById(userid).orElse(null);
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_DOES_NOT_EXIST, "User : id {" + userid + "} does not exist !!");
        }
        return user;
    }

    public User getUser() {
        Long userid = SecurityUtil.getCurrentUserId();
        if (userid == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_UNAUTHORIZED, "User : unauthorized !!");
        }
        return getUserById(userid);
    }

    public Map<String, Object> createPin(User user, String email) {
        Pin pin = pinService.createPin(user, email);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("email", pin.getEmail());
        return map;
    }

    public Map<String, Object> register(RegisterModel registerModel) {
        if (userRepository.existsByEmail(registerModel.getEmail())) {
            User ouser = userRepository.findByEmail(registerModel.getEmail());
            if (ouser.getStatus().equals(User.Status.TBC)) {
                if(registerModel.isRepeat()){
                    userRepository.delete(ouser);
                } else {
                throw new BaseException(ExceptionResponse.ERROR_CODE.USER_ACCOUNT_NOT_VERIFIED, "User : Email {" + registerModel.getEmail() + "} in process verified !!");
                }
            } else {
                throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_ALREADY_EXIST, "User : Email {" + registerModel.getEmail() + "} already exist !!");
            }
        }
        if (userRepository.existsByUsername(registerModel.getUsername())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_USERNAME_ALREADY_EXIST, "User : Username {" + registerModel.getUsername() + "} already exist !!");
        }

        User user = new User();
        user.setUsername(registerModel.getUsername());
        user.setEmail(registerModel.getEmail());
        user.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        user.setFirstname(registerModel.getFirstname());
        user.setLastname(registerModel.getLastname());
        user.setDoB(registerModel.getDoB());
        user.setGender(registerModel.getGender());
        user.setWeight(registerModel.getWeight());
        user.setHeight(registerModel.getHeight());
        user.setStatus(User.Status.TBC);

        user = userRepository.save(user);

        try {
            return createPin(user, user.getEmail());
        } catch (Exception e) {
            userRepository.delete(user);
        }
        return (Map<String, Object>) new HashMap<>().put("success", false);
    }

    public LoginResponseModel verifypin(PinModel pinModel) {
        User user = userRepository.findByEmail(pinModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + pinModel.getEmail() + "} does not exist !!");
        }
        pinService.verify(pinModel.getEmail(), pinModel.getPin());
        user.setStatus(User.Status.NORMAL);
        user = userRepository.save(user);
        String token = tokenService.tokenize(user);
        return new LoginResponseModel(user, true, token);
    }

    public Map<String, Object> pinresend(PinModel pinModel) {
        User user = userRepository.findByEmail(pinModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + pinModel.getEmail() + "} does not exist !!");
        }
        if (!user.getStatus().equals(User.Status.TBC)) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_ACCOUNT_VERIFIED, "User : account verified !!");
        }
        return createPin(user, user.getEmail());
    }

    public Map<String, Object> pinforgotpass(PinModel pinModel) {
        User user = userRepository.findByEmail(pinModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + pinModel.getEmail() + "} does not exist !!");
        }
        if (user.getStatus().equals(User.Status.TBC)) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_ACCOUNT_NOT_VERIFIED, "User : account not verified !!");
        }
        return createPin(user, user.getEmail());
    }

    public LoginResponseModel verifypinforgotpass(PinModel pinModel) {
        User user = userRepository.findByEmail(pinModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + pinModel.getEmail() + "} does not exist !!");
        }
        pinService.verify(pinModel.getEmail(), pinModel.getPin());
        if (pinModel.getPassword() == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_IS_NULL, "User : password is null !!");
        }
        user.setPassword(passwordEncoder.encode(pinModel.getPassword()));
        user = userRepository.save(user);
        String token = tokenService.tokenize(user);
        return new LoginResponseModel(user, true, token);
    }

    public LoginResponseModel login(LoginModel loginModel) {
        User user = userRepository.findByEmail(loginModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + loginModel.getEmail() + "} does not exist !!");
        }
        if (!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        if (user.getStatus().equals(User.Status.TBC)) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_ACCOUNT_NOT_VERIFIED, "User : account not verified !!");
        }
        String token = tokenService.tokenize(user);
        return new LoginResponseModel(user, true, token);
    }

    public Map<String, Boolean> logout() {
        User user = getUser();
        String token = SecurityUtil.getToken();

        if (token == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_UNAUTHORIZED, "User : unauthorized !!");
        }

        DecodedJWT decoded = tokenService.verify(token);
        if (decoded == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_UNAUTHORIZED, "User : unauthorized !!");
        }
        Date date = decoded.getClaim("exp").asDate();
        Timestamp exp = new Timestamp(date.getTime());
        if (decoded == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_UNAUTHORIZED, "User : unauthorized !!");
        }
        Jwtblacklist jwtblacklist = new Jwtblacklist();
        jwtblacklist.setToken(token);
        jwtblacklist.setExp(exp);
        jwtblacklist.setUser(user);
        jwtblacklistRepository.save(jwtblacklist);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public Map<String, Object> addImgProfile(MultipartFile fileImg) {
        User user = getUser();
        try {
            if (user.getImage() != null) {
                storageService.delete(user.getImage());
            }
            String s = "UP-";
            user.setImage(storageService.store(fileImg, s.concat(String.valueOf(user.getUserid()))));
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_SAVE, "File : file cannot be saved !!");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("imge", user.getImage());
        return map;
    }

    public Resource getImgProfile(User user) {
        if (user.getImage() == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_NO_PROFILE_IMAGE, "User : id {" + user.getUserid() + "}  does not have a profile picture !!");
        }
        try {
            return storageService.loadAsResource(user.getImage());
        } catch (Exception e) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_NOT_FOUND, "File : name {" + user.getImage() + "} not found !!");
        }
    }

    public User updateUser(User user, UserUpdateModel userupdate) {
        if (user.getUserid() != userupdate.getUserid()) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_INCORRECT_ID, "User : id {" + userupdate.getUserid() + "}  incorrect user id !!");
        }
        if (userRepository.existsByUsername(userupdate.getUsername()) && !userupdate.getUsername().equals(user.getUsername())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_USERNAME_ALREADY_EXIST, "User : Username {" + userupdate.getUsername() + "} already exist !!");
        }
        user.setUsername(userupdate.getUsername());
        user.setFirstname(userupdate.getFirstname());
        user.setLastname(userupdate.getLastname());
        user.setDoB(userupdate.getDoB());
        user.setGender(userupdate.getGender());
        user.setWeight(userupdate.getWeight());
        user.setHeight(userupdate.getHeight());
        return userRepository.save(user);
    }

    public Map<String, Boolean> updateUserPassword(UserUpdatePasswordModel userpsw) {
        User user = getUser();
        if (user.getUserid() != userpsw.getUserid()) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_INCORRECT_ID, "User : id {" + userpsw.getUserid() + "}  incorrect user id !!");
        }
        if (!passwordEncoder.matches(userpsw.getOldpassword(), user.getPassword())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        user.setPassword(passwordEncoder.encode(userpsw.getNewpassword()));
        userRepository.save(user);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public Map<String, Object> updateUserEmail(UserUpdateEmailModel useremail) {
        User user = getUser();
        if (user.getUserid() != useremail.getUserid()) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_INCORRECT_ID, "User : id {" + useremail.getUserid() + "}  incorrect user id !!");
        }
        if (userRepository.existsByEmail(useremail.getEmail())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_ALREADY_EXIST, "User : Email {" + useremail.getEmail() + "} already exist !!");
        }
        if (!passwordEncoder.matches(useremail.getPassword(), user.getPassword())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        return createPin(user, useremail.getEmail());
    }

    public User verifypinEmail(PinModel pinModel) {
        User user = getUser();
        pinService.verify(pinModel.getEmail(), pinModel.getPin());
        user.setEmail(pinModel.getEmail());
        return userRepository.save(user);
    }

    public Map<String, Boolean> deleteUser(DeleteUserModel deleteuser) {
        User user = getUser();
        if (user.getUserid() != deleteuser.getUserid()) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_INCORRECT_ID, "User : id {" + deleteuser.getUserid() + "}  incorrect user id !!");
        }
        if (!user.getEmail().equals(deleteuser.getEmail())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_INCORRECT, "User : Email {" + deleteuser.getEmail() + "} incorrect!!");
        }
        if (!user.getUsername().equals(deleteuser.getUsername())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_USERNAME_INCORRECT, "User : Username {" + deleteuser.getUsername() + "} incorrect!!");
        }
        if (!passwordEncoder.matches(deleteuser.getPassword(), user.getPassword())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        if (user.getImage() != null) {
            try {
                storageService.delete(user.getImage());
            } catch (IOException e) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_DELETE, "File : file cannot delete !!");
            }
        }
        userRepository.delete(user);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public User changestatus(User user, User.Status status) {
        user.setStatus(status);
        return userRepository.save(user);
    }
}
