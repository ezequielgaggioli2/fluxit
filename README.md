# Desafío técnico Flux IT

## Instrucciones para ejecutar proyecto
- **Descargar proyecto desde github**
En una consola ejecutar :
	> git clone https://github.com/ezequielgaggioli/fluxit.git
- **Instalar base de datos**
Una vez descargado el proyecto, en la raiz del mismo se encuentra un archivo dump **fluxit_db_dump.sql** para importar y generar la base de datos.
- **Configurar conexion a base de datos mysql**
Se debe ingresar a la carpeta src/main/resources que se encuentra en la raiz del proyecto. En ese lugar se encuentra un archivo llamado **aplication.properties**. Dentro de ese archivo se puede configurar la uri, usuario y contraseña de la base de datos.

- **Generar proyecto y ejecutar pruebas**
Se utilizo maven para la contrucción y configuración del proyecto. Para generar el archivo ejecutable y las pruebas unitarias, ejecutar en la raiz del proyecto descargado el siguiente comando :
	> mvn clean install
- **Ejecutar proyecto**
Luego del paso anterior, en la raiz del proyecto se genera una carpeta target donde se encuentra el ejecutable del proyecto. Dentro de la carpeta target ejecutar el siguiente comando :
	> java -jar springboot-fluxit-1.0.jar 
