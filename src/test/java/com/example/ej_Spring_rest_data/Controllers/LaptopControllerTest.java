package com.example.ej_Spring_rest_data.Controllers;

import com.example.ej_Spring_rest_data.Entities.Laptop;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //Es necesario indicar el orden, para que no me lo cambie el IDE
class LaptopControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    @DisplayName("Comprobar Create, desde controladores Spring Rest")
    @Order(1)
    void create() {
        HttpHeaders headers = new HttpHeaders(); //Creamos cabeceras para poder trabajar con ellas
        headers.setContentType(MediaType.APPLICATION_JSON); //setContentType para indicarle la aplicación es json
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); //setAccept para indicarle que tipo de elementos acepta

        //Laptop de ejemplo en código JSON
        String json = """ 
            {
                "hdd": 256,
                "manofacturer": "fabricante de prueba",
                "model": "modelo de ejemplo",
                "price": 595.99,
                "ram": 16
            }
            """;
        //Creamos una request de tipo http que envíe el String json creado arriba
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        //Comprobamos cosas con la request y si funciona, le asignamos el nuevo laptop a response
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptop", HttpMethod.POST, request, Laptop.class);

        //Creamos una clase Laptop que se va a llamar result y le asignamos el cuerpo de response
        Laptop result = response.getBody();

        //Realizamos los assertEquals
        assertEquals(1L, result.getId()); // comprobamos que el laptop creado tiene la id 1
        assertEquals("modelo de ejemplo", result.getModel()); // comprobamos que el modelo corresponde con el
        // el que hemos indicado en el String json

    }

    @Test
    @DisplayName("Comprobar findAll, desde controladores Spring Rest")
    @Order(2)
    void findAll() {
        // Creamos una ResponseEntity <con array de laptops> llamada response, y le asignamos el resultado de la llamada
        // a /api/laptops que devuelve un array de laptops (.class le resuelve el tipo de elemento que es laptop[])
        ResponseEntity<Laptop[]> response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode()); //Compruebo que devuelve un OK 200
    }

    @Test
    @DisplayName("Comprobar findOneById, desde controladores Spring Rest")
    @Order(3)
    void findOneById() {
        //--------------Descomentar create(); si vas a ejecutar el test de manera aislada--------
//        create(); //Invoco al método del test create() para que cree un laptop de ejemplo y comprobar que lo encuentra

        //---Comprobamos con un laptop existente para el if "Si existe, lo devuelve + OK"
        //Creo una ResponseEntity<Laptop> para que almacene el resultado de la entidad
        //getForEntity, lanza una petición http a la url: Localhost:puerto/api/laptops/{id} con la id 1
        ResponseEntity<Laptop> response = testRestTemplate.getForEntity("/api/laptop/1", Laptop.class);
        assertEquals(1L, response.getBody().getId()); //compruebo que el laptop que recibe tiene la id indicada
        assertEquals(HttpStatus.OK, response.getStatusCode()); //Compruebe que recibe un estado OK

        //--Comprobamos si buscamos un laptop inexistente, para el else "si no existe devuelve un Not Found"
        response = testRestTemplate.getForEntity("/api/laptop/999", Laptop.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); //Compruebo que recibe el Not Found
    }

    @Test
    @DisplayName("Comprobar update, desde controladores Spring Rest")
    @Order(4)
    void update() {
        //--------------Descomentar create(); si vas a ejecutar el test de manera aislada--------
//        create(); //Invoco al método del test create() para que cree un laptop de ejemplo y comprobar que lo actualiza

        HttpHeaders headers = new HttpHeaders(); //Creamos cabeceras para poder trabajar con ellas (pasarle el json)
        headers.setContentType(MediaType.APPLICATION_JSON); //setContentType para indicarle la aplicación es json
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); //setAccept para indicarle que tipo de elementos acepta

        //---Para comprobar el resultado si la laptop existe y ya está en nuestra
        //Laptop de ejemplo MODIFICADA en código JSON
        String jsonModificado = """ 
            {
                "hdd": 128,
                "id": 1,
                "manofacturer": "fabricante--Modificado",
                "model": "modelo---Modificado",
                "price": 355.99,
                "ram": 8
            }
            """;
        //Creamos una request de tipo http que envíe el String jsonModificado creado arriba
        HttpEntity<String> request = new HttpEntity<>(jsonModificado, headers);
        //ACTUALIZAMOS el laptop con los valores modidicados
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptop", HttpMethod.PUT, request, Laptop.class);
        //Creamos una clase Laptop que se va a llamar result y le asignamos el cuerpo de response
        Laptop result = response.getBody();
        //Realizamos los assertEquals
        assertEquals(1L, result.getId()); // comprobamos que el laptop creado sigue teniendo la id 1
        assertEquals("modelo---Modificado", result.getModel()); // comprobamos que el modelo corresponde con el
        // el que hemos modificado mediante el String json


        //---Para 1º if del método update "si no tiene id es una creación"
        //Laptop de ejemplo sin "id"
        String jsonSinId = """ 
            {
            }
            """;
        //modifico la request
        request = new HttpEntity<>(jsonSinId, headers);
        //Intentamos actualizar laptop sin "id"
        response = testRestTemplate.exchange("/api/laptop", HttpMethod.PUT, request, Laptop.class);
        //Creamos una clase Laptop que se va a llamar result y le asignamos el cuerpo de response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); //Compruebo que devuelve el estado Bad Request

        //---Para el 2º if del método update "si el libro no existe"
        String jsonLaptopInexistente = """ 
            {
                "hdd": 128,
                "id": 999,
                "manofacturer": "fabricante--Modificado",
                "model": "modelo---Modificado",
                "price": 355.99,
                "ram": 8
            }
            """;
        //modifico la request
        request = new HttpEntity<>(jsonLaptopInexistente, headers);
        //Intentamos actualizar laptop sin "id"
        response = testRestTemplate.exchange("/api/laptop", HttpMethod.PUT, request, Laptop.class);
        //Creamos una clase Laptop que se va a llamar result y le asignamos el cuerpo de response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); //Compruebo que devuelve el estado Not Found


    }

    @Test
    @DisplayName("Comprobar deleteById, desde controladores Spring Rest")
    @Order(5)
    void deleteById() {
        //--------------Descomentar create(); si vas a ejecutar el test de manera aislada--------
//        create(); //Invoco al método del test create() para que cree un laptop de ejemplo y comprobar que lo borra
        //No necesito saber que se crea correctamente, puesto que lo he comprobado en el test create()

        //Creamos una request de tipo http que envíe el String jsonModificado creado arriba
        HttpEntity<String> request = new HttpEntity<>("");
        ResponseEntity<Laptop> response;
        response = testRestTemplate.exchange("/api/laptop/1", HttpMethod.DELETE, request, Laptop.class);

        //compruebo que al borrar el laptop con id 1 devuelve status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Lanzo de nuevo la request para comprobar que al no existir devuelve un Not Found
        response = testRestTemplate.exchange("/api/laptop/1", HttpMethod.DELETE, request, Laptop.class);

        //compruebo que al intentar borrar el laptop con id 1 que no existe, devuelve Status Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    @DisplayName("Comprobar deleteAll, desde controladores Spring Rest")
    @Order(6)
    void deleteAll() {
        //--------------Descomentar create(); si vas a ejecutar el test de manera aislada--------
//        create(); //Invoco al método del test create() para que cree un laptop de ejemplo y comprobar que lo borra
        //No necesito saber que se crea correctamente, puesto que lo he comprobado en el test create()

        //Lanzo una request Delete a la url  que corresponde a deleteAll que deberá eliminar todos los elementos
        testRestTemplate.delete("/api/laptops");

        //---Para el if "si devuelve que no hay elementos en la bbdd después haberlo vaciado"
        //--Obtengo todos los elementos
        // Creamos una ResponseEntity <con array de laptops> llamada response, y le asignamos el resultado de la llamada
        // a /api/laptops que devuelve un array de laptops (.class le resuelve el tipo de elemento que es laptop[])
        ResponseEntity<Laptop[]> response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        // Compruebo que devuelve 0 elementos
        assertEquals(0, response.getBody().length);

        //---Todo Para el else "Si no se han borrado correctamente"
//      --Para conseguir que entre en el else, habría que forzar un error en el método de la bbdd
//      --Por ahora no se cómo conseguirlo.


    }
}