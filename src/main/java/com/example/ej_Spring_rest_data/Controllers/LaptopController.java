package com.example.ej_Spring_rest_data.Controllers;

import com.example.ej_Spring_rest_data.Entities.Laptop;
import com.example.ej_Spring_rest_data.Repositories.LaptopRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

@RestController
public class LaptopController {
    //Atributos
    private LaptopRepository laptopRepository;

    //Constructores
    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    //CRUD

    //Crear
    @PostMapping("/api/laptop")
    @ApiOperation("Crear una Laptop pasándole un json sin la id")
    public Laptop create(@RequestBody Laptop laptop){
        return laptopRepository.save(laptop);
    }

    //leer todos
    @GetMapping("/api/laptops")
    @ApiOperation("Leer todos las Laptop")
    public List<Laptop> findAll(){
        return laptopRepository.findAll();
    }

    //leer por id
    @GetMapping("/api/laptop/{id}")
    @ApiOperation("Leer un Laptop por la clave primaria id tipo Long")
    public ResponseEntity<Laptop> findOneById(@ApiParam("Clave primaria tipo Long") @PathVariable Long id){
        Optional<Laptop> laptopOpt = laptopRepository.findById(id);

        if (laptopOpt.isPresent()){ //Si existe, lo devuelve + OK
            return ResponseEntity.ok(laptopOpt.get());
        }else{ // si no existe devuelve un Not Found
            return ResponseEntity.notFound().build();
        }
    }

    //Actualizar
    @PutMapping("/api/laptop")
    @ApiOperation("Actualizar una Laptop")
    public ResponseEntity<Laptop> update(@RequestBody Laptop laptop){

        if (laptop.getId() == null){ //si no tiene id es una creación
            return ResponseEntity.badRequest().build(); //devolvemos error 400
        }else if (!laptopRepository.existsById(laptop.getId())){ //si el libro no existe
            return ResponseEntity.notFound().build(); //devolvemos error 404
        }else{ //si existe y ya está en nuestro repositorio
            return ResponseEntity.ok(laptopRepository.save(laptop)); //lo guardamos y devolvemos el libro actualizado
        }

    }

    //Eliminar por id
    @DeleteMapping("/api/laptop/{id}")
    @ApiOperation("Borrar una Laptop por clave primaria id tipo Long")
    public ResponseEntity<Laptop> deleteById(@PathVariable Long id){
        Optional<Laptop> laptopOpt = laptopRepository.findById(id);

        if (laptopOpt.isPresent()){ //Comprobamos si existe
            laptopRepository.deleteById(id); //Boramos
            return ResponseEntity.ok(laptopOpt.get()); //devolvemos ok + el libro
        }else{ //Si no exixte
            return ResponseEntity.notFound().build(); // Devolvemos not found 404
        }
    }

    //Eliminar todos
//    @ApiIgnore //para que el método deleteAll no aparezca en la documentación por seguridad
//    --->dejar de comentar en producción
    @DeleteMapping("/api/laptops")
    public ResponseEntity<Laptop> deleteAll(){
        laptopRepository.deleteAll(); //Borramos el repositorio
        if (laptopRepository.count() == 0){ //si devuelve que no hay elementos en la bbdd después haberlo vaciado
            System.out.println("Borrados todos los Laptops");   //Lo mostramos por consola
            return ResponseEntity.noContent().build(); //Devolvemos un estado no content 204
        }else{ //Si no se han borrado correctamente
            System.out.println("Error al intentar borrar los laptops en bbdd");
            return ResponseEntity.internalServerError().build(); //devolvemos fallo del servidor 500
        }

    }
}
