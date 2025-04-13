package com.fescode.novas.controller;

import com.fescode.novas.entity.Usuario;
import com.fescode.novas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ESTO PARA ENCRIPTAR LA CONTRASEÑA, MAYOR SEGURIDAD

    //LISTAMOS TODDOS LOS USUARIOSS
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    //CREAMOS TODOS LOS USAURIOS
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario.getEmail() == null || usuario.getContrasena() == null) {
                return ResponseEntity.badRequest().build();
            }

            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setActivo(true);

            Usuario nuevoUsuario = usuarioRepository.save(usuario);

            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ACTUALIZAMOS LOS USUARIOS
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody Usuario datosActualizados
    ) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            usuario.setNombre(datosActualizados.getNombre());
            usuario.setApellido(datosActualizados.getApellido());
            usuario.setEmail(datosActualizados.getEmail());

            if (datosActualizados.getContrasena() != null) {
                usuario.setContrasena(passwordEncoder.encode(datosActualizados.getContrasena()));
            }

            usuario.setDireccion(datosActualizados.getDireccion());
            usuario.setTelefono(datosActualizados.getTelefono());

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //ELIMINAMOS LOS USUARIOS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
