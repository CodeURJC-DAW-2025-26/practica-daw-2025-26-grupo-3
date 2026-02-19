# Remarket+

## 👥 Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| Daniel Hernanz Corral | d.hernanz.2023@alumnos.urjc.es | DanielHernanz9 |
| Marcos Hernández Martín | m.hernandez.2023@alumnos.urjc.es | MarcosHM851 |
| Jorge Naranjo Ballesteros | j.naranjo.2023@alumnos.urjc.es | 2005JNB |

---

## 🎭 **Preparación 1: Definición del Proyecto**

### **Descripción del Tema**
ReMarket+ es una aplicación web de compra y venta online de productos nuevos, reacondicionados y de segunda mano. La plataforma permite a la empresa vender productos nuevos y reacondicionados, y a los usuarios publicar, comprar y vender productos de segunda mano.

### **Entidades**
Indicar las entidades principales que gestionará la aplicación y las relaciones entre ellas:

1. **Usuario**: Persona registrada que puede comprar, vender y valorar productos.
2. **Producto**: Artículo nuevo, reacondicionado o de segunda mano disponible en la plataforma.
3. **Pedido**: Compra realizada por un usuario que incluye uno o más productos.
4. **Valoracion**: Opinión de un usuario sobre un producto o vendedor tras una compra.

**Relaciones entre entidades:**
1. Usuario – Pedido: Un usuario puede realizar múltiples pedidos (0:N).
2. Pedido – Producto: Un pedido puede contener varios productos y un producto puede estar en varios pedidos (N:M).
3. Usuario – Producto: Un usuario puede publicar varios productos y cada producto pertenece a un único usuario (0:N).
4. Usuario – Valoración: Un usuario puede realizar varias valoraciones (0:N).
5. Producto – Valoración: Un producto puede tener varias valoraciones (0:N).

### **Permisos de los Usuarios**
Describir los permisos de cada tipo de usuario e indicar de qué entidades es dueño:

* **Usuario Anónimo**: 
  - Permisos: 
      1. Puede navegar por las categorias.
      2. Puede ver productos y valoraciones. 
      3. No puede comprar ni vender.
  - No es dueño de ninguna entidad

* **Usuario Registrado**: 
  - Permisos:
      1. Puede comprar productos.
      2. Puede publicar productos propios (solo productos de segunda mano)
      3. Puede gestionar su perfil, favoritos, mensajes y pedidos.
  - Es dueño de: 
      - Perfil de usuario.
      - Productos publicados.
      - Pedidos.
      - Valoraciones. 

* **Administrador**: 
  - Permisos:
      1. Puede gestionar usuarios, productos y pedidos.
      2. Puede aprobar, editar o eliminar productos.
      3. Puede gestionar valoraciones y categorias.
  - Es dueño de: 
      - Los productos nuevos y reacondicionados.
      - Las categorias.
      - La gestión global de pedidos y usuarios.

### **Imágenes**
Indicar qué entidades tendrán asociadas una o varias imágenes:

- **Usuario**: Imagen de avatar por usuario.
- **Producto**: Múltiples imagenes por producto.

### **Gráficos**
Indicar qué información se mostrará usando gráficos y de qué tipo serán:

- **Ventas mensuales**: Gráfico de barras.
- **Productos más vendidos**: Gráfico circular.
- **Usuarios registrados**: Gráfico de lineas.
- **Pedidos por tipo de producto**: Gráfico de barras.

### **Tecnología Complementaria**
Indicar qué tecnología complementaria se empleará:

- Envío de correos electrónicos automáticos para confirmaciones de pedidos y registros.
- Generación de facturas en PDF.

### **Algoritmo o Consulta Avanzada**
Indicar cuál será el algoritmo o consulta avanzada que se implementará:

- **Algoritmo/Consulta**: Detección de productos más comprados.
- **Descripción**: Cuenta las compras de cada tipo de producto y muestra más productos de ese tipo.
- **Alternativa**: Consulta que ordena productos por relevancia y precio.

---

## 🛠 **Preparación 2: Maquetación de páginas con HTML y CSS**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/Lk7OcS_-km8?si=G5FnzZO8USZZBnr-)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Diagrama de Navegación**
Diagrama que muestra cómo se navega entre las diferentes páginas de la aplicación:

![Diagrama de Navegación](practica25_26/src/main/resources/static/assets/nav_diagram.jpg)

> El flujo comienza en la página principal. El usuario puede iniciar sesión o registrarse, así como ver y buscar los productos publicados en la página (pero no ver su detalle). En caso de iniciar sesión con una cuenta de administrador, la aplicación le llevará a la web de control de administrador. En caso contrario, el usuario volverá a la página principal, pero como usuario registrado. Ahora, el usuario tiene permitido ver el detalle de los productos, ver su perfil, publicar productos de segunda mano, y ver sus pedidos, su resumen de productos publicados y su carrito de la compra. Por su parte, el administrador puede ver estadísticas globales de la aplicación, aprovar pedidos y productos, y bloquear y desbloquear usuarios.

### **Capturas de Pantalla y Descripción de Páginas**

> Se describen a continuación las diferentes pantallas de las que se compone nuestra aplicación web. En el caso de que una pantalla pueda ser accedida tanto para usuarios registrados como para usuarios no registrados, se asume que el navbar cambia para mostrar las opciones acordes al tipo de usuario que hace uso de la página.

#### **1. Página Principal / Home**
![Página Principal anonimo](practica25_26/src/main/resources/static/assets/index_anonymous.png)

![Página Principal usuario registrado](practica25_26/src/main/resources/static/assets/index_registered.png)

> Página de inicio de la aplicación. Incluye una cabecera con el logotipo y eslogan de la marca y una lista de productos destacados.

#### **2. Inicio de Sesión / Log In**
![log in](practica25_26/src/main/resources/static/assets/login.png)

> Formulario que permite a los usuarios registrados introducir su usuario y contraseña para inciar sesión y poder utilizar los servicios principales de la tienda.

#### **3. Registro de Usuario / Sign Up**
![sign up](practica25_26/src/main/resources/static/assets/signup.png)

> Formulario que permite a los usuarios no registrados registrarse para poder utilizar los servicios de la tienda como usuario identificado.

#### **4. Visualización de todos los productos / Búsqueda de productos**
![product search](practica25_26/src/main/resources/static/assets/products_search.png)

> Página que muestra todos los productos disponibles en la tienda actualmente. Incluye un formulario a la izquierda que permite buscar y filtar productos, para que el usuario pueda encontrar el producto que necesite comprar.

#### **5. Detalle de producto**
![product detail](practica25_26/src/main/resources/static/assets/product_detail.png)

> Página que muestra las principales características de un producto, así como sus imágenes. En caso de estar registrado el usuario, podrá añadirlo al carrito desde esta página. Además, si el producto es nuevo o reacondicionado, aparecerán reseñas del mismo. Como los productos de segunda mano no se producen ni distribuyen en masa, no tendrán reseñas al tener un único comprador.

#### **6. Publicación de nuevo producto**
![product publish](practica25_26/src/main/resources/static/assets/product_publish.png)

> Formulario que recoge los datos de los productos de segunda mano que los usuarios registrados quieren publicar para su venta. Observar que el estado indica directamente segunda mano: esto es así debido a que los usuarios registrados no pueden poner a la venta productos nuevos o reacondicionados, sólo pueden vender objetos de segunda mano. Los productos publicados por los usuarios registrados deben ser aprobados por un administrador.

![admin product publish](practica25_26/src/main/resources/static/assets/product_publish_admin.png)

> Existe un formulario diferente para administradores, los cuales pueden publicar artículos nuevos y reacondicionados.

#### **7. Carrito de la compra**
![shopping cart](practica25_26/src/main/resources/static/assets/shopping_cart.png)

> Página que muestra los productos que el usuario registrado acumula para su posterior compra en conjunto.

#### **8. Perfil de usuario registrado**
![user profile](practica25_26/src/main/resources/static/assets/profile.png)

> Página que muestra todos los datos del usuario registrado que está haciendo uso de la aplicación. Desde esta pantalla los usuarios registrados pueden acceder a todos los servicios que la aplicación ofrece para este tipo de usuario.

#### **9. Productos publicados de usuario registrado / Mis productos**
![my products](practica25_26/src/main/resources/static/assets/my_products.png)

> Página que muestra todos los productos que el usuario registrado ha puesto a la venta. Desde esta pantalla el usuario puede publicar, ver detalle, editar, y eliminar los productos que ha puesto a la venta.

#### **10. Resumen de pedidos de usuario registrado / Mis pedidos**
![my orders](practica25_26/src/main/resources/static/assets/my_orders.png)

> Página que muestra un resumen de todos los pedidos que el usuario registrado ha realizado.

#### **11. Panel de administrador**
![admin panel](practica25_26/src/main/resources/static/assets/admin_panel.png)

> Página que muestra diversas métricas útiles para la administración del negocio.

#### **12. Productos publicados / Admin**
![published admin products](practica25_26/src/main/resources/static/assets/admin_published_products.png)

> Página exclusiva de administradores en la que se pueden visualizar todos los productos publicados actualemente en la web.

#### **13. Aprobación de productos**
![admin approve](practica25_26/src/main/resources/static/assets/admin_approve.png)

> Página en que permite a los administradores aprobar (o denegar) las solicitudes de aprobación de los productos que los usuarios registrados publican.  

#### **14. Gestión de pedidos**
![admin orders](practica25_26/src/main/resources/static/assets/admin_orders.png)

> Página que permite a los administrados aprobar (o denegar) los pedidos que hacen los usuarios registrados. 

#### **15. Gestión de usuarios**
![admin users](practica25_26/src/main/resources/static/assets/admin_users.png)

> Página que permite a los administradores controlar los usuarios que tienen acceso a la aplicación web, pudiendo bloquearles en caso de incumplir las normas de publicación de ReMarket+.

#### **16. Perfil de administrador**
![Página Principal](practica25_26/src/main/resources/static/images/admin_users.png)

> Página que muestra los datos del administrador que se encuentra utilizando la aplicación.

---

## 🛠 **Práctica 1: Web con HTML generado en servidor y AJAX**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Navegación y Capturas de Pantalla**

#### **Diagrama de Navegación**

Solo si ha cambiado.

#### **Capturas de Pantalla Actualizadas**

Solo si han cambiado.

### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.8 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/[usuario]/[nombre-repositorio].git
   cd [nombre-repositorio]
   ```

2. **AQUÍ INDICAR LO SIGUIENTES PASOS**

#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin`, contraseña: `admin`
- **Usuario Registrado**: usuario: `user`, contraseña: `user`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

![Diagrama Entidad-Relación](practica25_26/src/main/resources/static/assets/database-diagram.png)

> [Descripción opcional: Ej: "El diagrama muestra las 4 entidades principales: Usuario, Producto, Pedido y Categoría, con sus respectivos atributos y relaciones 1:N y N:M."]

### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

![Diagrama de Clases](practica25_26/src/main/resources/static/assets/classes-diagram.png)

> [Descripción opcional del diagrama y relaciones principales]

### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 2: Incorporación de una API REST a la aplicación web, despliegue con Docker y despliegue remoto**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Documentación de la API REST**

#### **Especificación OpenAPI**
📄 **[Especificación OpenAPI (YAML)](/api-docs/api-docs.yaml)**

#### **Documentación HTML**
📖 **[Documentación API REST (HTML)](https://raw.githack.com/[usuario]/[repositorio]/main/api-docs/api-docs.html)**

> La documentación de la API REST se encuentra en la carpeta `/api-docs` del repositorio. Se ha generado automáticamente con SpringDoc a partir de las anotaciones en el código Java.

### **Diagrama de Clases y Templates Actualizado**

Diagrama actualizado incluyendo los @RestController y su relación con los @Service compartidos:

![Diagrama de Clases Actualizado](static/assets/complete-classes-diagram.png)

### **Instrucciones de Ejecución con Docker**

#### **Requisitos previos:**
- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 2.0 o superior)

#### **Pasos para ejecutar con docker-compose:**

1. **Clonar el repositorio** (si no lo has hecho ya):
   ```bash
   git clone https://github.com/[usuario]/[repositorio].git
   cd [repositorio]
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**:

### **Construcción de la Imagen Docker**

#### **Requisitos:**
- Docker instalado en el sistema

#### **Pasos para construir y publicar la imagen:**

1. **Navegar al directorio de Docker**:
   ```bash
   cd docker
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**

### **Despliegue en Máquina Virtual**

#### **Requisitos:**
- Acceso a la máquina virtual (SSH)
- Clave privada para autenticación
- Conexión a la red correspondiente o VPN configurada

#### **Pasos para desplegar:**

1. **Conectar a la máquina virtual**:
   ```bash
   ssh -i [ruta/a/clave.key] [usuario]@[IP-o-dominio-VM]
   ```
   
   Ejemplo:
   ```bash
   ssh -i ssh-keys/app.key vmuser@10.100.139.XXX
   ```

2. **AQUÍ LOS SIGUIENTES PASOS**:

### **URL de la Aplicación Desplegada**

🌐 **URL de acceso**: `https://[nombre-app].etsii.urjc.es:8443`

#### **Credenciales de Usuarios de Ejemplo**

| Rol | Usuario | Contraseña |
|:---|:---|:---|
| Administrador | admin | admin123 |
| Usuario Registrado | user1 | user123 |
| Usuario Registrado | user2 | user123 |

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 3: Implementación de la web con arquitectura SPA**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](URL_del_video)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Preparación del Entorno de Desarrollo**

#### **Requisitos Previos**
- **Node.js**: versión 18.x o superior
- **npm**: versión 9.x o superior (se instala con Node.js)
- **Git**: para clonar el repositorio

#### **Pasos para configurar el entorno de desarrollo**

1. **Instalar Node.js y npm**
   
   Descarga e instala Node.js desde [https://nodejs.org/](https://nodejs.org/)
   
   Verifica la instalación:
   ```bash
   node --version
   npm --version
   ```

2. **Clonar el repositorio** (si no lo has hecho ya)
   ```bash
   git clone https://github.com/[usuario]/[nombre-repositorio].git
   cd [nombre-repositorio]
   ```

3. **Navegar a la carpeta del proyecto React**
   ```bash
   cd frontend
   ```

4. **AQUÍ LOS SIGUIENTES PASOS**

### **Diagrama de Clases y Templates de la SPA**

Diagrama mostrando los componentes React, hooks personalizados, servicios y sus relaciones:

![Diagrama de Componentes React](static/assets/spa-classes-diagram.png)

### **Participación de Miembros en la Práctica 3**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

