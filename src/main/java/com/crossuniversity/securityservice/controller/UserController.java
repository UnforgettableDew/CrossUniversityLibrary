package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.UserProfileDTO;
import com.crossuniversity.securityservice.service.PasswordChangingService;
import com.crossuniversity.securityservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.crossuniversity.securityservice.utils.ResponseCode.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.NOT_FOUND_EXCEPTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
public class UserController {
    private final PasswordChangingService passwordChangingService;
    private final UserService userService;

    @Autowired
    public UserController(PasswordChangingService passwordChangingService,
                          UserService userService) {
        this.passwordChangingService = passwordChangingService;
        this.userService = userService;
    }

    @Operation(
            summary = "Retrieve the profile information of the authenticated user",
            description = "This API endpoint is accessible to authenticated users, allowing them to retrieve and view " +
                    "their own profile information. The profile information includes the user's ID, username, email, available " +
                    "space, associated university details, and lists of owned and subscribed libraries.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = USER_PROFILE_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDTO> myProfile() {
        return new ResponseEntity<>(userService.myProfile(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve the profile information of a user by email",
            description = "This API endpoint is accessible to authenticated users, allowing them to retrieve and view " +
                    "the profile information of another user based on their email. The profile information includes the " +
                    "user's ID, username, email, available space, associated university details, and lists of owned and " +
                    "subscribed libraries.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = USER_PROFILE_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/{email}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfileByEmail(
            @Parameter(description = "Email address of the user for whom the profile information is requested")
            @PathVariable String email) {
        return new ResponseEntity<>(userService.getUserProfileByEmail(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Confirm the old password before initiating the password-changing procedure",
            description = "This API endpoint is accessible to authenticated users, allowing them to confirm their " +
                    "old password before initiating the password-changing procedure. The user needs to provide their " +
                    "current password for confirmation. After successful confirmation, a secret code is sent to the " +
                    "user's email for further identification in the password-changing process.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/confirm-old-password")
    public ResponseEntity<?> confirmOldPassword(
            @Parameter(description = "Current password to be confirmed")
            @RequestParam String confirmedPassword) throws AccessException {
        passwordChangingService.confirmOldPassword(confirmedPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Change the password with confirming the identity using secret code",
            description = "This API endpoint is accessible to authenticated users, allowing them to change " +
                    "their password after confirming their identity with a copied secret code. Once the user has " +
                    "received the secret code via email and copied it, they provide the code along with the new password " +
                    "to this method for identity confirmation. After successful confirmation, the user can change " +
                    "their old password to the new one.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "Secret code copied from the email")
            @RequestParam String copiedSecretCode,
            @Parameter(description = "New password to be set")
            @RequestParam String newPassword) throws AccessException {
        passwordChangingService.changePassword(copiedSecretCode, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(
            summary = "Update the user profile by changing the username",
            description = "This API endpoint is accessible to authenticated users, allowing " +
                    "them to update their user profile by changing the username.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = USER_PROFILE_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @Parameter(description = "New username to be set")
            @RequestParam String userName) {
        return new ResponseEntity<>(userService.updateProfile(userName), HttpStatus.OK);
    }
}
