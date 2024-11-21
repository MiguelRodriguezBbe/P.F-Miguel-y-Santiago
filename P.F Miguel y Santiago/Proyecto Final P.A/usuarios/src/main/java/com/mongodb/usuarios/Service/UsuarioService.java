package com.mongodb.usuarios.Service;

import com.mongodb.usuarios.Models.Usuario;
import com.mongodb.usuarios.Repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    public Usuario crearUsuario(@Valid Usuario usuario) {
        String passwordEncoded = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncoded); 
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        return usuarioRepository.save(usuario); 
    }

    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    
    public Optional<Usuario> obtenerPorId(String id) {
        return usuarioRepository.findById(id);
    }

    
    public Usuario actualizarUsuario(String id, @Valid Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

    
        if (!usuarioExistente.get().getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

       
        if (!usuario.getPassword().equals(usuarioExistente.get().getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Encriptamos la nueva contraseña
        }

        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

   
    public void eliminarUsuario(String id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}

