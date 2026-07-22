package com.ProjectExperience.api.web;

import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @GetMapping
    public ResponseEntity<User> findDataUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        User user = userService.findUser(authenticatedUser.getUser().getId());

        return ResponseEntity.ok(
                userService.findDataUser(user)
        );
    }









    }

