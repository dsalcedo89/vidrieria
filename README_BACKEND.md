# Backend Vidriería (Java 17)

## Configuración
- Java 17.
- PostgreSQL: `vidrieria_db`.
- Variables de entorno recomendadas: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`.
- `JWT_SECRET` debe tener al menos 32 bytes y ser un secreto aleatorio. El valor por defecto de desarrollo solo permite arrancar localmente y debe cambiarse antes de desplegar.
- Mantener `spring.jpa.hibernate.ddl-auto: validate`; no usar `create`/`create-drop` en una base con datos.

## Rutas principales
- `POST /api/auth/login` público: `{"email":"...","password":"..."}`
- `GET /api/public/productos` público.
- `GET /api/public/productos/{id}` consulta producto.
- `POST /api/public/productos`, `PUT /api/public/productos/{id}` requieren rol ADMINISTRADOR o VENDEDOR.
- `DELETE /api/public/productos/{id}` requiere ADMINISTRADOR.
- `POST /api/cotizaciones` público.
- `GET /api/cotizaciones` y `GET /api/cotizaciones/{id}` requieren ADMINISTRADOR o VENDEDOR.
- `/api/usuarios`, `/api/roles`, `/api/auditoria` requieren ADMINISTRADOR.

Para endpoints protegidos, enviar `Authorization: Bearer <accessToken>`.

## Importante
1. Este paquete es una base de implementación y necesita compilarse y probarse contra tu PostgreSQL real.
2. El usuario inicial debe existir en `usuarios` con contraseña BCrypt y tener una fila correspondiente en `usuario_roles`. No guardar contraseñas en texto plano.
3. `AuditoriaService.registrar(...)` está disponible, pero debe invocarse explícitamente desde las operaciones de negocio que quieras auditar; la lectura del historial sí está implementada.
4. Configurar CORS con el origen real de Angular antes de conectar el frontend.
5. No se incluyen credenciales reales ni el directorio `.git` ni `target`.
