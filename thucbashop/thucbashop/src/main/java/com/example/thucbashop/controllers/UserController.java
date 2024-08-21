package com.example.thucbashop.controllers;

import com.example.thucbashop.dtos.UserDTO;
import com.example.thucbashop.dtos.UserLoginDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.Role;
import com.example.thucbashop.models.User;
import com.example.thucbashop.responese.LoginResponse;
import com.example.thucbashop.responese.RegisterResponse;
import com.example.thucbashop.services.IUserService;
import com.example.thucbashop.components.LocalizationUtils;
import com.example.thucbashop.components.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO,
                                                       BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage =  result.getFieldErrors()
                        .stream()
                        .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Unknown error")
                        .toList();
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(String.join(", ", errorMessage))
                                .build()
                );
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassWord())){
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                        .message(localizationUtils.getLocalizationUtils(MessageKeys.PASSWORD_NOT_MATCH))
                        .build());
            }
           User user=  userService.createUser(userDTO);

            return ResponseEntity.ok(RegisterResponse.builder()
                    .user(user)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(RegisterResponse.builder()
                    .message(localizationUtils.getLocalizationUtils(e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String  token = userService.login(userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId() == null? 1 : userLoginDTO.getRoleId());

            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizationUtils(MessageKeys.MessageKeys_LOGIN_SUCCESSFULLY))
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizationUtils(MessageKeys.MessageKeys_LOGIN_FAILED, e.getMessage()))
                    .build());
        }

    }
}
