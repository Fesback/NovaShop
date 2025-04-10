package com.fescode.novas.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;
    private String nombre;
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;
    private String contrasena;
    private String direccion;
    private String telefono;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    private Boolean activo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuariorol",joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();
}
