package br.com.avila.controllers.docs;

import br.com.avila.data.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticate user and return token")
    ResponseEntity<?> signin(AccountCredentialsDTO accountCredentialsDTO);

    @Operation(summary = "Refresh token for authenticated user and return a token")
    ResponseEntity<?> refreshToken( String username, String refreshToken);

    @Operation(summary = "Create a new user")
    AccountCredentialsDTO create(AccountCredentialsDTO credentialsDTO);
}
