package com.example.thucbashop.services;

import com.example.thucbashop.components.JwtTokenUtils;
import com.example.thucbashop.dtos.UserDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.exceptions.PermissionDenyException;
import com.example.thucbashop.models.Role;
import com.example.thucbashop.models.User;
import com.example.thucbashop.repositories.RoleRepo;
import com.example.thucbashop.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepo.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("can not found phone number");
        }
        Role role= roleRepo.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFound("Role not found"));
//        if(role.getName().toUpperCase().equals(Role.ADMIN)){
//            throw new PermissionDenyException("Khong the dang ki tai khoan admin");
//        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .birth(userDTO.getBirth())
                .facebookId(userDTO.getFacebookAcountId())
                .googleId(userDTO.getGoogleId())
                .build();

        newUser.setRole(role);
        //nếu có accountId không yêu cầu mật khẩu
        if(userDTO.getFacebookAcountId() ==0  && userDTO.getGoogleId()==0){
            String pass = userDTO.getPassword();
            //spring security
            String enCodePassWord = passwordEncoder.encode(pass);
            newUser.setPassword(enCodePassWord);
        }
        return userRepo.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password,Long roleId) throws DataNotFound {
        //làm security
       Optional<User> optionalUser = userRepo.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFound("Invalid userName phoneNumber / password");
        }
        //tra va jwt token
        User existingUser = optionalUser.get();
        //check password
        if(existingUser.getFacebookId() ==0  && existingUser.getGoogleId()==0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone Number  or password");
            }
        }
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())){
            throw new DataNotFound("Khong co");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,password,
                existingUser.getAuthorities()
        );

        //xac thuc spring security
        authenticationManager.authenticate(authenticationToken);
        try {
            return jwtTokenUtil.generateToken(existingUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
