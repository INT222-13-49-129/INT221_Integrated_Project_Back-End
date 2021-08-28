package sit.int222.cfan.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Jwtblacklist;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.models.LoginModel;
import sit.int222.cfan.models.LoginResponseModel;
import sit.int222.cfan.models.RegisterModel;
import sit.int222.cfan.repositories.JwtblacklistRepository;
import sit.int222.cfan.repositories.UserRepository;
import sit.int222.cfan.services.TokenService;
import sit.int222.cfan.util.SecurityUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
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

    public User getUser() {
        Long userid = SecurityUtil.getCurrentUserId();
        if (userid == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_UNAUTHORIZED, "User : unauthorized !!");
        }
        User user = userRepository.findById(userid).orElse(null);
        if (user==null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_DOES_NOT_EXIST, "User : Email {"+ userid +"} does not exist !!");
        }
        return user;
    }

    public LoginResponseModel register(RegisterModel registerModel) {
        if (userRepository.existsByEmail(registerModel.getEmail())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_ALREADY_EXIST, "User : Email {" + registerModel.getEmail() + "} already exist !!");
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
        user.setStatus(User.Status.NORMAL);

        User userregis = userRepository.save(user);
        String token = tokenService.tokenize(userregis);
        return new LoginResponseModel(userregis, true, token);
    }

    public LoginResponseModel login(LoginModel loginModel) {
        User user = userRepository.findByEmail(loginModel.getEmail());
        if (user == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + loginModel.getEmail() + "} does not exist !!");
        }
        if (!passwordEncoder.matches(loginModel.getPassword(), user.getPassword())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        String token = tokenService.tokenize(user);
        return new LoginResponseModel(user, true, token);
    }

    public Map<String, Boolean> logout(){
        User user  = getUser();
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
}
