
# Desafío técnico Flux IT

## Instrucciones para ejecutar proyecto
1. **Descargar proyecto desde GitHub**

	Ejecutar en terminal :
	> git clone https://github.com/ezequielgaggioli/fluxit.git

2. **Instalar base de datos**

	Una vez descargado el proyecto, en la raíz del mismo se encuentra un archivo dump `fluxit_db_dump.sql` para importar y generar la base de datos. La base de datos se inicializa con dos usuarios y un conjunto de candidatos.
	
3. **Configurar conexión a base de datos MySql**

	Se debe ingresar a la carpeta `src/main/resources` que se encuentra en la raíz del proyecto. En ese lugar se encuentra un archivo llamado `aplication.properties`. Dentro de ese archivo se puede configurar la uri, usuario y contraseña de la base de datos segun su servidor Mysql.

4. **Generar proyecto y ejecutar pruebas**

	Se utilizo Maven para la construcción y configuración del proyecto. Para generar el archivo ejecutable y las pruebas unitarias, ejecutar en la raíz del proyecto descargado el siguiente comando :
	> mvn clean install
	
5. **Ejecutar proyecto**

	Luego del paso anterior, en la raiz del proyecto se genera una carpeta target donde se encuentra el ejecutable del proyecto. Dentro de la carpeta target ejecutar el siguiente comando para iniciar servidor SpringBoot :
	> java -jar springboot-fluxit-1.0.jar 

## Anexo

En la raiz del proyecto descargado se encuentra el archivo `fluxit.postman_collection.json` para realizar algunas pruebas por postman. 

## Consideraciones

- El desarrollo se realizó dirigido por pruebas (TDD).
- Para las pruebas unitarias se utilizó el framework Mockito.
- Para la generación de los tokens de autenticación se utilizo el standard JWT. Pero al generar los mismos puse una fecha fija de creación y expiración para que no sea tan dinámico (solo cambia por el subject) y funcionen las pruebas unitarias y por postman
