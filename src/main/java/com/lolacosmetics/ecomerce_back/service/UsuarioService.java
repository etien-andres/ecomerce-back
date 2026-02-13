package com.lolacosmetics.ecomerce_back.service;

import com.lolacosmetics.ecomerce_back.model.Usuario;
import com.lolacosmetics.ecomerce_back.model.Carrito;
import com.lolacosmetics.ecomerce_back.repository.UsuarioRepository;
import com.lolacosmetics.ecomerce_back.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

   // @Autowired
    //private PasswordEncoder passwordEncoder;
   @Autowired
   private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptar contraseña

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar usuario
        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        // Crear carrito para el usuario
        Carrito carrito = new Carrito();
        carrito.setUsuario(nuevoUsuario);
        carritoRepository.save(carrito);

        return nuevoUsuario;
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


}