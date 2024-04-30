package com.aytronn.kibvet.service;

import com.aytronn.kibvet.config.security.JwtService;
import com.aytronn.kibvet.dao.LocalUser;
import com.aytronn.kibvet.dao.Token;
import com.aytronn.kibvet.dto.AuthRequest;
import com.aytronn.kibvet.dto.AuthenticationResponse;
import com.aytronn.kibvet.dto.CreateUserRequest;
import com.aytronn.kibvet.enums.Role;
import com.aytronn.kibvet.enums.TokenType;
import com.aytronn.kibvet.repository.TokenRepository;
import com.aytronn.kibvet.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(final UserRepository repository, final TokenRepository tokenRepository, final PasswordEncoder passwordEncoder, final JwtService jwtService, final AuthenticationManager authenticationManager) {
    this.repository = repository;
    this.tokenRepository = tokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthenticationResponse register(CreateUserRequest request) {
    var user = LocalUser.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .enabled(true)
            .role(Role.USER)
            .build();
    var savedUser = getRepository().save(user);
    var jwtToken = getJwtService().generateToken(user);
    var refreshToken = getJwtService().generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public String test(CreateUserRequest request) {
    return passwordEncoder.encode(request.password());
  }

  public AuthenticationResponse authenticate(AuthRequest request) {
    getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            )
    );
    var user = getRepository().findByUsername(request.username()).orElseThrow();
    var jwtToken = getJwtService().generateToken(user);
    var refreshToken = getJwtService().generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void saveUserToken(LocalUser user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    getTokenRepository().save(token);
  }

  private void revokeAllUserTokens(LocalUser user) {
    var validUserTokens = getTokenRepository().findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    getTokenRepository().saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = getJwtService().getUsername(refreshToken);
    if (userEmail != null) {
      var user = getRepository().findByUsername(userEmail).orElseThrow();

      if (getJwtService().isTokenValid(refreshToken, user)) {
        var accessToken = getJwtService().generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public UserRepository getRepository() {
    return this.repository;
  }

  public TokenRepository getTokenRepository() {
    return this.tokenRepository;
  }

  public PasswordEncoder getPasswordEncoder() {
    return this.passwordEncoder;
  }

  public JwtService getJwtService() {
    return this.jwtService;
  }

  public AuthenticationManager getAuthenticationManager() {
    return this.authenticationManager;
  }
}
