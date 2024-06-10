package com.ecommerce.user.serviceImpl;

import com.ecommerce.user.dto.LoginRequestDto;
import com.ecommerce.user.dto.LoginResponseDto;
import com.ecommerce.user.dto.UserCreateRequestDto;
import com.ecommerce.user.dto.UserResponseDto;
import com.ecommerce.user.entity.User;import com.ecommerce.user.exception.ResourceAlreadyExistsException;import com.ecommerce.user.exception.ResourceNotFoundException;import com.ecommerce.user.mapper.AuthMapper;import com.ecommerce.user.mapper.UserMapper;import com.ecommerce.user.repository.UserRepository;import com.ecommerce.user.service.UserService;import com.ecommerce.user.util.JwtUtil;import lombok.RequiredArgsConstructor;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto register(UserCreateRequestDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User Email Already Registered");
        }
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Invalid Credentials"));
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if(!matches) {
            throw new ResourceNotFoundException("Invalid Credentials");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRoles());
        return authMapper.toLoginResponse(user,token);
    }

    @Override
    public UserResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid User Id" + id));
        return userMapper.toDto(user);
    }
}
