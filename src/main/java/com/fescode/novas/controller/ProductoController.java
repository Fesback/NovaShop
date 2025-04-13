package com.fescode.novas.controller;

import com.fescode.novas.entity.Producto;
import com.fescode.novas.repository.CategoriaRepository;
import com.fescode.novas.repository.ProductoRepository;
import com.fescode.novas.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Metodo para listar todos los productos
    @GetMapping
    public List<Producto> listar() {
        return productoService.listarTodos();
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        try{
            if(producto.getCategoria() == null || !categoriaRepository.existsById(producto.getCategoria().getId_categoria())){
                return ResponseEntity.badRequest().build();
            }

            producto.setFechaCreacion(LocalDateTime.now());
            producto.setActivo(true);

            Producto nuevoProducto = productoRepository.save(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    // Actualizar Producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id,@RequestBody Producto datosActualizados) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if(productoExistente.isPresent()){
            Producto producto = productoExistente.get();
            producto.setNombre(datosActualizados.getNombre());
            producto.setDescripcion(datosActualizados.getDescripcion());
            producto.setPrecio(datosActualizados.getPrecio());
            producto.setStock(datosActualizados.getStock());
            producto.setCategoria(datosActualizados.getCategoria());
            producto.setFechaCreacion(datosActualizados.getFechaCreacion());
            producto.setImagenUrl(datosActualizados.getImagenUrl());
            producto.setActivo(datosActualizados.getActivo());

            Producto actualizado = productoRepository.save(producto);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar Producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if(productoRepository.existsById(id)){
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
