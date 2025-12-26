package com.zybo.parking.service;

import com.zybo.parking.dto.UserDTO;
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

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        userRepository.findByDocument(userDTO.getDocument()).ifPresent(u -> {
            throw new ConflictException("Document already exists");
        });
        userRepository.findByPhone(userDTO.getPhone()).ifPresent(u -> {
            throw new ConflictException("Phone already exists");
        });
        
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        userRepository.findByDocument(userDTO.getDocument())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) throw new ConflictException("Document already exists");
                });
        userRepository.findByPhone(userDTO.getPhone())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) throw new ConflictException("Phone already exists");
                });

        user.setName(userDTO.getName());
        user.setDocument(userDTO.getDocument());
        user.setPhone(userDTO.getPhone());
        
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
