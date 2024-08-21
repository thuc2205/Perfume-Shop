package com.example.thucbashop.services;

import com.example.thucbashop.dtos.UserDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.models.User;
import org.springframework.stereotype.Service;


public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password,Long roleId) throws DataNotFound;
}
