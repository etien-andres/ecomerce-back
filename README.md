📚 DOCUMENTACIÓN API ECOMMERCE
📋 ÍNDICE
Introducción

Configuración Inicial

Autenticación

Endpoints Públicos

Endpoints de Usuario

Endpoints de Administración

Endpoints de Vendedor

Modelos de Datos

Códigos de Error

Ejemplos de Uso

1. INTRODUCCIÓN
1.1 Descripción General
API RESTful para sistema de ecommerce con autenticación JWT, manejo de productos, pedidos, pagos y panel de administración.

1.2 Base URL
text
http://localhost:8080/api
1.3 Formato de Respuesta
Todas las respuestas son en formato JSON.

1.4 Autenticación
La API utiliza JWT (JSON Web Tokens) para autenticación. Incluir el token en el header:

text
Authorization: Bearer {token}
1.5 Roles de Usuario
Rol	Descripción
CLIENTE	Usuario regular que puede comprar
VENDEDOR	Puede gestionar productos
ADMIN	Acceso total al sistema
2. CONFIGURACIÓN INICIAL
2.1 Requisitos Previos
Java 11 o superior

MySQL 8.0

Maven 3.6+

2.2 Configuración de Base de Datos
properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
2.3 Variables de Entorno
properties
jwt.secret=586E3272357538782F413F4428472B4B6250655368566B597033733676397924
jwt.expiration=86400000
jwt.refresh-expiration=604800000
3. AUTENTICACIÓN
3.1 Registro de Usuario
Endpoint: POST /auth/register

Request Body:

json
{
  "nombre": "Juan Pérez",
  "email": "juan@email.com",
  "password": "123456",
  "direccion": "Calle Principal 123",
  "telefono": "123456789"
}
Response 200 OK:

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "email": "juan@email.com",
  "nombre": "Juan Pérez",
  "rol": "CLIENTE",
  "expiraEn": 1700000000000
}
Validaciones:

nombre: 2-100 caracteres

email: Formato válido, único

password: Mínimo 6 caracteres

telefono: 9-15 dígitos (opcional)

3.2 Login
Endpoint: POST /auth/login

Request Body:

json
{
  "email": "juan@email.com",
  "password": "123456"
}
Response 200 OK:

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "email": "juan@email.com",
  "nombre": "Juan Pérez",
  "rol": "CLIENTE",
  "expiraEn": 1700000000000
}
3.3 Refresh Token
Endpoint: POST /auth/refresh

Request Body:

json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
Response 200 OK:

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "email": "juan@email.com",
  "nombre": "Juan Pérez",
  "rol": "CLIENTE",
  "expiraEn": 1700000000000
}
3.4 Logout
Endpoint: POST /auth/logout

Response 200 OK:

json
"Sesión cerrada exitosamente"
4. ENDPOINTS PÚBLICOS
4.1 Productos
Listar Productos
Endpoint: GET /productos

Response 200 OK:

json
[
  {
    "id": 1,
    "nombre": "Laptop HP",
    "descripcion": "16GB RAM, 512GB SSD",
    "precio": 799.99,
    "stock": 10,
    "categoria": {
      "id": 1,
      "nombre": "Electrónica",
      "activa": true
    }
  }
]
Query Params:

Parámetro	Tipo	Descripción
categoria	Long	Filtrar por categoría
precioMin	Double	Precio mínimo
precioMax	Double	Precio máximo
orden	String	asc/desc por precio
Buscar Productos
Endpoint: GET /productos/buscar

Query Params:

Parámetro	Tipo	Descripción
nombre	String	Búsqueda por nombre
Obtener Producto por ID
Endpoint: GET /productos/{id}

4.2 Categorías
Listar Categorías
Endpoint: GET /categorias

Response 200 OK:

json
[
  {
    "id": 1,
    "nombre": "Electrónica",
    "descripcion": "Productos electrónicos",
    "activa": true
  }
]
Listar Categorías Activas
Endpoint: GET /categorias/activas

5. ENDPOINTS DE USUARIO (Requiere Autenticación)
5.1 Perfil
Obtener Perfil
Endpoint: GET /usuarios/perfil?email=juan@email.com

Headers:

text
Authorization: Bearer {token}
Actualizar Perfil
Endpoint: PUT /usuarios/{id}

Request Body:

json
{
  "nombre": "Juan Pérez Actualizado",
  "direccion": "Nueva Dirección 456",
  "telefono": "987654321"
}
5.2 Carrito
Obtener Carrito
Endpoint: GET /carrito/usuario/{usuarioId}

Agregar Producto
Endpoint: POST /carrito/usuario/{usuarioId}/agregar

Request Body:

json
{
  "productoId": 1,
  "cantidad": 2
}
Actualizar Cantidad
Endpoint: PUT /carrito/usuario/{usuarioId}/producto/{productoId}?cantidad=3

Eliminar Producto
Endpoint: DELETE /carrito/usuario/{usuarioId}/producto/{productoId}

Limpiar Carrito
Endpoint: DELETE /carrito/usuario/{usuarioId}/limpiar

5.3 Pedidos
Crear Pedido
Endpoint: POST /pedidos

Request Body:

json
{
  "usuarioId": 1,
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ],
  "direccionEnvio": "Calle Principal 123"
}
Mis Pedidos
Endpoint: GET /pedidos/mis-pedidos?usuarioId=1

Obtener Pedido
Endpoint: GET /pedidos/{id}

5.4 Pagos
Procesar Pago
Endpoint: POST /pagos/procesar

Request Body:

json
{
  "pedidoId": 1,
  "monto": 1599.98,
  "metodoPago": "TARJETA_CREDITO",
  "numeroTarjeta": "4111111111111111",
  "fechaExpiracion": "12/25",
  "cvv": "123",
  "nombreTitular": "Juan Pérez",
  "cuotas": 1
}
5.5 Reseñas
Crear Reseña
Endpoint: POST /usuarios/{usuarioId}/productos/{productoId}/resenas

Request Body:

json
{
  "calificacion": 5,
  "titulo": "Excelente producto",
  "comentario": "Muy buena calidad, lo recomiendo"
}
Mis Reseñas
Endpoint: GET /usuarios/{usuarioId}/resenas
