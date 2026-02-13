package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.dto.*;
import com.lolacosmetics.ecomerce_back.model.Usuario;
import com.lolacosmetics.ecomerce_back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;


@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    public UserDetails authenticate(LoginRequestDTO loginRequestDTO) throws Exception {

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authObject = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            authentication = authenticationManager.authenticate(authObject);
        }
        catch (BadCredentialsException e) {
            System.out.println("[AuthenticationService][authenticate][Error bad credentials: " + e.toString() + "]");
            throw e;
        }


        return (UserDetails) authentication.getPrincipal();
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(usuario.getRol())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Validar email existente
        Usuario usr =usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        if (usuarioRepository.existsById(usr.getId())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setRol("CLIENTE");


        usuarioRepository.save(usuario);

        // Generar tokens
        UserDetails userDetails = loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .expiraEn(System.currentTimeMillis() + 86400000)
                .build();
    }

    public AuthResponseDTO refreshToken(TokenRefreshRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.validateToken(refreshToken, userDetails)) {
            throw new RuntimeException("Refresh token inválido");
        }

        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newToken = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .expiraEn(System.currentTimeMillis() + 86400000)
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Obtener usuario
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .expiraEn(System.currentTimeMillis() + 86400000)
                .build();
    }
}
