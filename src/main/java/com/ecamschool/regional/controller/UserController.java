package com.ecamschool.regional.controller;

import com.ecamschool.regional.api.UsersApi;
import com.ecamschool.regional.dto.UserDTO;
import com.ecamschool.regional.dto.UserRequestDTO;
import com.ecamschool.regional.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserController implements UsersApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDTO> createUser(UserRequestDTO userRequest) {
        log.debug("REST request to create user: {}", userRequest.getUsername());
        UserDTO created = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Integer id) {
        log.debug("REST request to get user by id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id.longValue()));
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Integer id, UserRequestDTO userRequestDTO) {
        log.debug("REST request to update user: {}", id);
        UserDTO updated = userService.updateUser(id.longValue(), userRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        log.debug("REST request to delete user: {}", id);
        userService.deleteUser(id.longValue());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsersPost() {
        log.debug("POST request to get all users (for framework compatibility)");
        return ResponseEntity.ok(userService.getAllUsers());
    }
}