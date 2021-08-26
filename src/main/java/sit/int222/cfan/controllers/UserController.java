package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.models.LoginModel;
import sit.int222.cfan.models.LoginResponseModel;
import sit.int222.cfan.models.RegisterModel;
import sit.int222.cfan.repositories.UserRepository;
import sit.int222.cfan.services.TokenService;

@Service
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenService tokenService;

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
        return new LoginResponseModel(userregis,true,token);
    }

    public LoginResponseModel login(LoginModel loginModel) {
        User user = userRepository.findByEmail(loginModel.getEmail());
        if(user == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_EMAIL_DOES_NOT_EXIST, "User : Email {" + loginModel.getEmail() + "} does not exist !!");
        }
        if(!passwordEncoder.matches(loginModel.getPassword(),user.getPassword())){
            throw new BaseException(ExceptionResponse.ERROR_CODE.USER_PASSWORD_INCORRECT, "User : password incorrect !!");
        }
        String token = tokenService.tokenize(user);
        return new LoginResponseModel(user,true,token);
    }
}
