package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.request.LoginRequest;
import com.ecommerce.user_service.dto.request.RefreshTokenRequest;
import com.ecommerce.user_service.dto.request.RegisterRequest;
import com.ecommerce.user_service.dto.response.AuthResponse;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.enums.Role;
import com.ecommerce.user_service.exception.UserAlreadyExistsException;
import com.ecommerce.user_service.exception.UserNotFoundException;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserMapper userMapper;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Register request: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created: {}", savedUser.getId());

        return generateAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Login request: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(0L));

        redisTemplate.delete(REFRESH_TOKEN_PREFIX + user.getEmail());

        return generateAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String email = jwtService.extractEmail(refreshToken);

        String storedToken = redisTemplate.opsForValue()
                .get(REFRESH_TOKEN_PREFIX + email);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
            throw new RuntimeException("Refresh token expired.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(0L));

        return generateAuthResponse(user);
    }

    public void logout(RefreshTokenRequest request) {
        String email = jwtService.extractEmail(request.getRefreshToken());
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
        log.info("User logged out: {}", email);
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getRole().name()
        );
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + user.getEmail(),
                refreshToken,
                refreshExpiration,
                TimeUnit.MILLISECONDS
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(refreshExpiration)
                .user(userMapper.toResponse(user))
                .build();
    }
}