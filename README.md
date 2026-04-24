# biomedical-backend

Backend Spring Boot para la plataforma de gestión de residuos biomédicos.

## Admin CRUD (tablas/vistas)

Se expone un CRUD genérico para tablas/vistas del esquema bajo:

- `GET /api/admin/<tabla>?limit=100&offset=0`
- `GET /api/admin/<tabla>/{id}` (solo si hay PK única)
- `POST /api/admin/<tabla>` (solo tablas)
- `PUT /api/admin/<tabla>/{id}` (solo tablas y PK única)
- `DELETE /api/admin/<tabla>/{id}` (solo tablas y PK única)

Seguridad opcional:

- Si `ADMIN_API_KEY` está configurada, el cliente debe enviar `X-Admin-Key: <ADMIN_API_KEY>` para cualquier ruta `/api/admin/**`.

