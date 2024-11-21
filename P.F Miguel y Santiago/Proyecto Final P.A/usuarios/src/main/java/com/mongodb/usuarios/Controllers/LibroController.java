package com.mongodb.usuarios.Controllers;

import com.mongodb.usuarios.Models.Libro;
import com.mongodb.usuarios.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    @Autowired
    private LibroService libroService;

    
    @GetMapping
    public List<Libro> obtenerLibros() {
        return libroService.obtenerTodos();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable String id) {
        Optional<Libro> libro = libroService.obtenerPorId(id);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



  
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        try {
           
            if (libro.getCantidadStock() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            Libro nuevoLibro = libroService.crearLibro(libro);
            return ResponseEntity.ok(nuevoLibro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


   
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable String id, @RequestBody Libro libro) {
        try {
            Libro libroActualizado = libroService.actualizarLibro(id, libro);
            return ResponseEntity.ok(libroActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable String id) {
        try {
            libroService.eliminarLibro(id);  
            return ResponseEntity.noContent().build(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();  
        }
    }
}


