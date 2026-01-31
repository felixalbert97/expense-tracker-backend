package de.felixalbert.expensetracker.security.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.felixalbert.expensetracker.security.exception.InvalidRefreshTokenException;
import de.felixalbert.expensetracker.security.model.AuthResponse;
import de.felixalbert.expensetracker.security.model.CustomUserDetails;
import de.felixalbert.expensetracker.security.model.LoginRequest;
import de.felixalbert.expensetracker.security.model.RefreshToken;
import de.felixalbert.expensetracker.security.model.RegisterRequest;
import de.felixalbert.expensetracker.security.model.RegisterResponse;
import de.felixalbert.expensetracker.security.service.JwtService;
import de.felixalbert.expensetracker.security.service.RefreshTokenService;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        UserService userService,
        RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        userService.createUser(
            request.email(),
            request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(request.email()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody LoginRequest request
    ) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        User user = userService
            .getById(principal.getId());

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken =
            refreshTokenService.create(user);

        ResponseCookie cookie = ResponseCookie.from(
                "refreshToken",
                refreshToken.getToken()
            )
            .httpOnly(true)
            .secure(true) 
            .sameSite("Strict")
            .path("/auth")
            .maxAge(Duration.ofDays(30))
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new AuthResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken token = refreshTokenService.validate(refreshToken);

        User user = token.getUser();

        String newAccessToken = jwtService.generateToken(user);

        RefreshToken newRefreshToken = refreshTokenService.rotate(token);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/auth")
            .maxAge(Duration.ofDays(30))
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new AuthResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            refreshTokenService.revokeByToken(refreshToken);
        }

        ResponseCookie deleteCookie = ResponseCookie.from(
                "refreshToken", ""
            )
            .httpOnly(true)
            .secure(true) 
            .sameSite("Strict")
            .path("/auth")
            .maxAge(0)
            .build();

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
            .build();
    }
}
