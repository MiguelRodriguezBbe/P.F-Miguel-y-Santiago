package com.mongodb.usuarios.Service;

import com.mongodb.libros.Utils.Constantes;
import com.mongodb.usuarios.Models.Libro;
import com.mongodb.usuarios.Repository.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

   
    public Optional<Libro> obtenerPorId(String id) {
        return libroRepository.findById(id);
    }

   
    public Libro crearLibro(Libro libro) {
        if (libro.getCantidadStock() == null || libro.getCantidadStock() <= 0 || libro.getCantidadStock() > 500) {
            throw new IllegalArgumentException("La cantidad en stock debe ser un número positivo y no mayor a 500");
        }

        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new IllegalArgumentException("El ISBN ya está registrado");
        }

        if (libro.getFechaPublicacion() != null && libro.getFechaPublicacion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser en el futuro");
        }

        if (!Constantes.GENEROS_VALIDOS.contains(libro.getGenero())) {
            throw new IllegalArgumentException("El género no es válido");
        }

        if (!Constantes.IDIOMAS_VALIDOS.contains(libro.getIdioma())) {
            throw new IllegalArgumentException("El idioma no es válido");
        }

        return libroRepository.save(libro);
    }

    public Libro actualizarLibro(String id, Libro libro) {
        Optional<Libro> libroExistente = libroRepository.findById(id);
        if (libroExistente.isEmpty()) {
            throw new IllegalArgumentException("El libro no existe");
        }

        if (libro.getCantidadStock() == null || libro.getCantidadStock() <= 0 || libro.getCantidadStock() > 500) {
            throw new IllegalArgumentException("La cantidad en stock debe ser un número positivo y no mayor a 500");
        }

        if (libroRepository.existsByIsbn(libro.getIsbn()) && !libro.getIsbn().equals(libroExistente.get().getIsbn())) {
            throw new IllegalArgumentException("El ISBN ya está registrado");
        }

        if (libro.getFechaPublicacion() != null && libro.getFechaPublicacion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser en el futuro");
        }

        if (!Constantes.GENEROS_VALIDOS.contains(libro.getGenero())) {
            throw new IllegalArgumentException("El género no es válido");
        }

        if (!Constantes.IDIOMAS_VALIDOS.contains(libro.getIdioma())) {
            throw new IllegalArgumentException("El idioma no es válido");
        }

        libro.setId(id);  
        return libroRepository.save(libro);
    }

    
    public void eliminarLibro(String id) {
        Optional<Libro> libro = libroRepository.findById(id);
        if (libro.isEmpty()) {
            throw new IllegalArgumentException("El libro no existe");
        }
        libroRepository.deleteById(id);
    }
}

