package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.dto.UserRegisterDto;
import com.ecommerce.backend.dto.UserResponseDto;
import com.ecommerce.backend.dto.UserUpdateDto;

public interface UserService {

    UserResponseDto register(UserRegisterDto userRegisterDto);

    UserResponseDto getByUsername(String username);

    UserResponseDto getById(Long id);

    UserResponseDto updateProfile(String username, UserUpdateDto updateDto);

    List<UserResponseDto> getAllUsers(int page, int size);

    void delete(Long id);

}
