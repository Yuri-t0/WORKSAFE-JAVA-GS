package com.worksafe.api.controller;

import com.worksafe.api.entity.User;
import com.worksafe.api.entity.UserRole;
import com.worksafe.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

     @GetMapping
    public ResponseEntity<Page<User>> listUsers(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "role", required = false) String roleParam,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort
    ) {
         UserRole role = null;
        if (roleParam != null && !roleParam.trim().isEmpty()) {
            try {
                role = UserRole.valueOf(roleParam.toUpperCase());
            } catch (IllegalArgumentException ex) {
                 role = null;
            }
        }

         String[] parts = sort.split(",");
        String sortField = parts[0];
        Sort.Direction direction = parts.length > 1
                ? Sort.Direction.fromString(parts[1])
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<User> result = userService.listUsers(name, role, pageable);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

     @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        User result = userService.update(
                id,
                updatedUser.getName(),
                updatedUser.getRole()
        );
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
