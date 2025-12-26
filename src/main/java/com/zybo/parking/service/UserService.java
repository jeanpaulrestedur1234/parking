package com.zybo.parking.service;

import com.zybo.parking.dto.UserDTO;
import com.zybo.parking.dto.UserRequest;
import com.zybo.parking.entity.User;
import com.zybo.parking.exception.ConflictException;
import com.zybo.parking.exception.ResourceNotFoundException;
import com.zybo.parking.mapper.UserMapper;
import com.zybo.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @SuppressWarnings("null")
    @Transactional
    public UserDTO createUser(UserRequest userRequest) {
        userRepository.findByDocument(userRequest.getDocument()).ifPresent(u -> {
            throw new ConflictException("Document already exists");
        });
        userRepository.findByPhone(userRequest.getPhone()).ifPresent(u -> {
            throw new ConflictException("Phone already exists");
        });

        User user = userMapper.toEntity(userRequest);
        return userMapper.toDto(userRepository.save(user));
    }

    @SuppressWarnings("null")
    public UserDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @SuppressWarnings("null")
    @Transactional
    public UserDTO updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.findByDocument(userRequest.getDocument())
                .ifPresent(u -> {
                    if (!u.getId().equals(id))
                        throw new ConflictException("Document already exists");
                });
        userRepository.findByPhone(userRequest.getPhone())
                .ifPresent(u -> {
                    if (!u.getId().equals(id))
                        throw new ConflictException("Phone already exists");
                });

        user.setName(userRequest.getName());
        user.setDocument(userRequest.getDocument());
        user.setPhone(userRequest.getPhone());

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
