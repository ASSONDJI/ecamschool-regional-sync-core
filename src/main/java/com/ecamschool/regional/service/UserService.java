package com.ecamschool.regional.service;

import com.ecamschool.regional.dto.UserDTO;
import com.ecamschool.regional.dto.UserRequestDTO;
import com.ecamschool.regional.entity.User;
import com.ecamschool.regional.exception.NotFoundException;
import com.ecamschool.regional.exception.ConflictException;
import com.ecamschool.regional.mapper.UserMapper;
import com.ecamschool.regional.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Request to get all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Request to get user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("Request to get user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        log.debug("Request to get user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntityByEmail(String email) {
        log.debug("Request to get user entity by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    public UserDTO createUser(UserRequestDTO request) {
        log.debug("Request to create user: {}", request.getUsername());


        String username = cleanString(request.getUsername());
        String email = cleanString(request.getEmail());
        String password = request.getPassword();
        String fullName = cleanString(request.getFullName());
        String role = cleanString(request.getRole());
        String delegationRegion = cleanString(request.getDelegationRegion());
        String department = cleanString(request.getDepartment());

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setRole(role);
        user.setDelegationRegion(delegationRegion);
        user.setDepartment(department);
        user.setIsActive(true);

        User saved = userRepository.save(user);
        log.debug("User created with id: {}", saved.getId());
        return userMapper.toDto(saved);
    }


    private String cleanString(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.replaceAll("^\"+|\"+$", "");

        cleaned = cleaned.replace("\\\"", "\"");
        return cleaned;
    }

    public UserDTO updateUser(Long id, UserRequestDTO request) {
        log.debug("Request to update user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));


        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setDelegationRegion(request.getDelegationRegion());
        user.setDepartment(request.getDepartment());

        User updated = userRepository.save(user);
        log.debug("User updated with id: {}", updated.getId());
        return userMapper.toDto(updated);
    }

    public void deleteUser(Long id) {
        log.debug("Request to delete user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        log.debug("User deleted with id: {}", id);
    }
}