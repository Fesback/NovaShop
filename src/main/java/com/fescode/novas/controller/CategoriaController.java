package com.fescode.novas.controller;

import com.fescode.novas.entity.Categoria;
import com.fescode.novas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Metodo para listar todas las categorías
    @GetMapping
    public List<Categoria> listarCategorias(){
        return categoriaRepository.findAll();
    }

    // Crear una nueva categoria
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        if (categoria.getNombre_categoria() == null || categoria.getNombre_categoria().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Categoria mueva = categoriaRepository.save(categoria);
        return new ResponseEntity<>(mueva, HttpStatus.CREATED);
    }

    // Actualizar la categoría}
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Integer id,
            @RequestBody Categoria datos
    ){
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
        if(optionalCategoria.isPresent()) {
            Categoria categoria = optionalCategoria.get();
            categoria.setNombre_categoria(datos.getNombre_categoria());
            categoria.setDescripcion(datos.getDescripcion());
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        if(categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
