# Aplicación para gestión de almacenes

Aplicación basada en el framework Vaadin y lenguaje Java, que permite la gestión de almacenes de una empresa de ventas: permite la recepción de productos y gestión de proveedores, generación de pedidos de clientes, y visualización de datos en función del tipo de usuario. 

Tiene varias vistas: tanto para administradores, como trabajadores de la empresa, y cliente, y cada una mantiene una interfaz diferenciada: 
* Administradores: permiten la gestión de proveedores, usuarios y consulta total de información de la empresa
* Trabajadores: permite la modificación y gestión de artículos entrantes y salientes 
* Cliente: permite la compra de productos, y visualización de pedidos
  
La base de datos que sustenta el sistema, está orquestada en lenguaje PostgreSQL, y permite una total integración gracias al uso de Maven como gestor de dependencias del programa. 
  
# Ejecución

Para iniciar la aplicación, debemos ejecutar la aplicación como un Maven Project, usando como etiqueta goal: spring-boot:run. La aplicación queda desplegada de manera local en el puerto 8080 de manera predeterminada

