package com.emp.userservice.service.serviceImpl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String password) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();

        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Response response = keycloak
                .realm(realm)
                .users()
                .create(user);

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {

            throw new RuntimeException(
                    "Failed to create Keycloak user. Status: "
                            + response.getStatus()
            );
        }

        String location = response.getHeaderString("Location");

        return location.substring(
                location.lastIndexOf("/") + 1
        );
    }

    public void deleteUser(String keycloakUserId) {

        keycloak
                .realm(realm)
                .users()
                .delete(keycloakUserId);
    }


    public void assignUserRole(String keycloakUserId) {

        RoleRepresentation userRole;

        try {
            userRole = keycloak
                    .realm(realm)
                    .roles()
                    .get("USER")
                    .toRepresentation();

        } catch (Exception e) {
            throw new RuntimeException(
                    "USER realm role does not exist or cannot be accessed",
                    e
            );
        }

        try {
            keycloak
                    .realm(realm)
                    .users()
                    .get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .add(List.of(userRole));

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to assign USER role to Keycloak user: "
                            + keycloakUserId,
                    e
            );
        }
    }



}