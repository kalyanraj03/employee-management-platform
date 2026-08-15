package com.emp.userservice.service.serviceImpl;

import com.emp.userservice.dto.CreateUserRequest;
import com.emp.userservice.dto.UpdateUserRequest;
import com.emp.userservice.dto.UserResponse;
import com.emp.userservice.entity.User;
import com.emp.userservice.exceptions.EmailAlreadyExistsException;
import com.emp.userservice.exceptions.ResourceNotFoundException;
import com.emp.userservice.repository.UserRepository;
import com.emp.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final KeycloakUserService keycloakUserService;

    @Transactional
    @Override
    public UserResponse registerUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "User already exists with email : " + request.email());
        }

        String keycloakUserId = null;

        try {

            // 1. Create user in Keycloak

            keycloakUserId = keycloakUserService.createUser(
                    request.email(),
                    request.email(),
                    request.firstName(),
                    request.lastName(),
                    request.password()
            );

            // 2. Assign USER role

            keycloakUserService.assignUserRole(keycloakUserId);

            // 3. Save application user in MySQL

            User user = User.builder()
                    .keycloakUserId(keycloakUserId)
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);

            return mapToUserResponse(savedUser);
        }catch (Exception e) {

            // Compensate Keycloak if DB operation fails
            if (keycloakUserId != null) {

                try {

                    keycloakUserService.deleteUser(keycloakUserId);

                } catch (Exception rollbackException) {

                    // Don't hide the original exception
                    log.error(
                            "Failed to rollback Keycloak user: {}",
                            keycloakUserId,
                            rollbackException
                    );
                }
            }

            throw e;
        }
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        return mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());

        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));

        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserBysessId(String sessId) {

        Optional<User> updatedUser = userRepository.findByKeycloakUserId(sessId);

        return mapToUserResponse(updatedUser.get());
    }

    private UserResponse mapToUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}