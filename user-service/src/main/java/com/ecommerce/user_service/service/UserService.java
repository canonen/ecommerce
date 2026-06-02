package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.response.UserResponse;
import com.ecommerce.user_service.exception.UserNotFoundException;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}