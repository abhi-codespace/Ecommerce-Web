package com.ecommerce.backend.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.dto.UserRegisterDto;
import com.ecommerce.backend.dto.UserResponseDto;
import com.ecommerce.backend.dto.UserUpdateDto;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = modelMapper.map(userRegisterDto, User.class);

        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDto.class);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto updateProfile(String username, UserUpdateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (updateDto.getEmail() != null && !updateDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        user.setEmail(updateDto.getEmail());
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(int page, int size) {
        org.springframework.data.domain.Pageable pageable = PageRequest.of(page, size); // ✅ FIXED: Correct import

        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .getContent();
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
