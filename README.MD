    API rest con persistencia en bbdd H2

Proyecto Spring Boot con las dependencias:

* H2
* Spring Data JPA
* Spring Web
* Spring Boot dev tools

-Crear una clase HelloController que sea un controlador REST. 
    Dentro de la clase crear un método que retorne un saludo. 
    Retorna el saludo desde el navegador y desde Postman.


-Dentro de la misma app crear las clases necesarias para trabajar con "Laptops":

-Laptop (entidad) 

-LaptopRepository (repositorio)

-LaptopController (controlador)

-Desde LaptopController crear los métodos CRUD.

Funciona desde Postman. 
    Ver la colección de postman preconfigurada: ej_spring_rest.postman_collection.json

Swagger implementado en la url: http://localhost:8080/swagger-ui/

Implementación de tests unitarios básicos de la clase "LaptopController" (a modo de ejemplo) con Junit5
    Ver los resultados en el directorio: Resultados_Test
