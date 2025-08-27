package com.ioriocars.controller;

import com.ioriocars.domain.User;
import com.ioriocars.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public Optional<User> getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public User create(@RequestParam String email,
                       @RequestParam String password,
                       @RequestParam String role) {
        return userService.createUser(email, password, role);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @RequestParam String email,
                       @RequestParam(required = false) String password,
                       @RequestParam String role) {
        return userService.updateUser(id, email, password, role);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
