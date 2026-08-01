# Justificación técnica

## ¿Por qué este stack?

Elegí Java porque es el lenguaje con el que ya tenía experiencia resolviendo la lógica de negocio (el CRUD original lo hice primero como aplicación de consola). Para exponerlo como interfaz web, usé únicamente lo que trae el propio JDK (`com.sun.net.httpserver.HttpServer`), sin frameworks como Spring, para mantener el proyecto simple, sin dependencias externas que instalar, y fácil de ejecutar en cualquier máquina que tenga un JDK.

## Organización de las carpetas

El proyecto sigue una separación por responsabilidades:

- **models/**: contiene la clase `Producto`, la entidad principal del sistema.
- **services/**: contiene `CrudProductos`, con toda la lógica de negocio (crear, listar, actualizar, eliminar) y las validaciones de los datos.
- **web/**: contiene la capa que expone esa lógica como API REST (`ProductoHandler`), el manejo de archivos estáticos (`StaticFileHandler`) y un parser simple de JSON (`JsonUtil`).
- **public/**: la interfaz visual (HTML, CSS y JS) que consume la API mediante `fetch`.

Esta estructura permite que la lógica de negocio (services) no dependa de cómo se expone al usuario (web), lo que facilita mantenerla o probarla por separado.

## Retos

El principal reto fue exponer la lógica de consola como una API web sin usar ningún framework: hubo que escribir a mano el enrutamiento por método y path (GET, POST, PUT, DELETE), el manejo de JSON de entrada y salida, y el servidor de archivos estáticos para la interfaz. También tuve que adaptar la generación del id, que en la versión de consola se pedía manualmente y en la web se genera automáticamente al crear un producto.

## Flujo de Git

El desarrollo se hizo en commits progresivos:

1. Clase Producto (modelo de datos).
2. Servicio CrudProductos con la lógica y validaciones.
3. Servidor HTTP y API REST (web/, Main.java).
4. Interfaz visual (public/).
5. Validaciones adicionales, desarrolladas en la rama feature/validaciones y luego integradas a main mediante merge.

## Dificultad

Fue cuesta arriba al principio, sobre todo la parte de manejar JSON sin librerías externas, pero una vez resuelto ese punto el resto fluyó bastante bien.
