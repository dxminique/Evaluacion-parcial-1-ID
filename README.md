# 🎫 Ticket Service — Microservicio de Gestión de Tickets

Microservicio desarrollado con **Spring Boot 4** y arquitectura hexagonal (Ports & Adapters) para la gestión de tickets de soporte. Este repositorio forma parte del pipeline DevOps construido en el curso **Ingeniería DevOps (DOY0101)**.

---

## 📋 Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Tecnologías](#tecnologías)
- [Estrategia de Ramificación](#estrategia-de-ramificación)
- [Convenciones de Commits](#convenciones-de-commits)
- [Naming de Ramas](#naming-de-ramas)
- [Flujo de Merge y Pull Requests](#flujo-de-merge-y-pull-requests)
- [Estrategia de Revisión de Código](#estrategia-de-revisión-de-código)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Cómo Ejecutar](#cómo-ejecutar)
- [GitHub Actions — CI/CD](#github-actions--cicd)
- [Uso de Inteligencia Artificial](#uso-de-inteligencia-artificial)

---

## Descripción del Proyecto

El **Ticket Service** permite registrar y consultar tickets de soporte mediante una API REST. Implementa arquitectura hexagonal separando claramente el dominio, los casos de uso y los adaptadores de infraestructura.

**Endpoints principales:**
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/tickets/db` | Registra un ticket en base de datos |
| POST | `/tickets/txt` | Registra un ticket en archivo de texto |
| GET | `/tickets/db/{id}/estado` | Consulta el estado de un ticket por ID |

---

## Tecnologías

- Java 17
- Spring Boot 4
- Spring Data JPA
- MySQL 8
- Lombok
- Docker & Docker Compose
- Maven
- GitHub Actions

---

## Estrategia de Ramificación

### Modelo elegido: **GitFlow**

Se eligió **GitFlow** porque el proyecto tiene un ciclo de desarrollo con fases claramente diferenciadas (desarrollo de features, corrección de bugs, releases), lo cual se alinea mejor con un flujo estructurado que separa el trabajo estable del trabajo en progreso.

**Comparación con alternativas:**

| Criterio | GitFlow | Trunk-Based Development |
|----------|---------|------------------------|
| Estructura | Múltiples ramas longevas | Una rama principal |
| Ideal para | Equipos con releases planificados | Equipos con deploy continuo |
| Trazabilidad | Alta (ramas por feature/hotfix) | Media (requiere feature flags) |
| Complejidad | Media-alta | Baja |

**GitFlow se adapta mejor a este proyecto** porque trabajamos en pareja con entregas por sprints, lo que requiere aislar features hasta que estén listas y poder corregir bugs críticos sin interrumpir el desarrollo activo.

### Ramas del repositorio

| Rama | Propósito | Vida útil |
|------|-----------|-----------|
| `main` | Código en producción, estable | Permanente |
| `develop` | Integración continua de features | Permanente |
| `feature/<nombre>` | Desarrollo de nuevas funcionalidades | Temporal |
| `hotfix/<nombre>` | Corrección urgente de bugs en producción | Temporal |

### Diagrama del flujo

```
main ──────────────────────────────────────────▶  (producción)
  │                                      ▲
  │ (crea develop)              (merge hotfix)
  ▼                                      │
develop ──────────────────────────────────▶  (integración)
  │         ▲         ▲
  │  (merge) │  (merge)│
  ▼          │         │
feature/    feature/  hotfix/
registro-  consulta-  fix-estado-
ticket     estado     nulo
```

---

## Convenciones de Commits

Se utiliza el estándar **Conventional Commits** para mantener un historial limpio y legible:

```
<tipo>(<alcance>): <descripción corta en imperativo>

[cuerpo opcional]

[footer opcional]
```

### Tipos permitidos

| Tipo | Cuándo usarlo |
|------|---------------|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de bug |
| `docs` | Cambios en documentación |
| `refactor` | Refactorización sin cambio de comportamiento |
| `test` | Agregar o modificar tests |
| `chore` | Tareas de mantenimiento (dependencias, config) |
| `ci` | Cambios en archivos de CI/CD |

### Ejemplos válidos

```bash
feat(ticket): agregar endpoint para registrar ticket en base de datos
fix(estado): corregir retorno nulo al consultar ticket inexistente
docs(readme): agregar sección de convenciones de commits
ci(actions): configurar workflow para push a develop
refactor(service): separar lógica de persistencia en adaptador propio
```

### Reglas

- Usar **imperativo** en la descripción: "agregar", no "agregué" ni "agrega"
- Máximo **72 caracteres** en la primera línea
- Sin punto final en la descripción
- El alcance (entre paréntesis) es opcional pero recomendado

---

## Naming de Ramas

### Formato

```
<tipo>/<descripcion-en-kebab-case>
```

### Ejemplos

```bash
feature/registro-ticket-db
feature/consulta-estado-ticket
feature/validacion-campos-request
hotfix/fix-npe-consultar-estado
hotfix/fix-conexion-mysql
```

### Reglas

- Usar **minúsculas** siempre
- Separar palabras con **guion** (`-`), no underscore ni espacios
- Nombres **descriptivos y cortos** (máximo 5 palabras)
- No incluir números de versión ni fechas en el nombre
- Eliminar la rama local y remota **después del merge**

---

## Flujo de Merge y Pull Requests

### Flujo para una Feature

```bash
# 1. Partir siempre desde develop actualizado
git checkout develop
git pull origin develop

# 2. Crear rama de feature
git checkout -b feature/nombre-de-la-feature

# 3. Desarrollar y commitear
git add .
git commit -m "feat(scope): descripción del cambio"

# 4. Subir la rama al repositorio remoto
git push origin feature/nombre-de-la-feature

# 5. Abrir Pull Request hacia develop en GitHub
# 6. Revisión por el compañero de equipo
# 7. Merge con squash (mantiene historial limpio)
# 8. Eliminar rama feature luego del merge
```

### Flujo para un Hotfix

```bash
# 1. Partir desde main (el bug está en producción)
git checkout main
git pull origin main

# 2. Crear rama de hotfix
git checkout -b hotfix/descripcion-del-fix

# 3. Corregir el bug y commitear
git commit -m "fix(scope): descripción de la corrección"

# 4. Merge a main Y a develop (para no perder el fix)
git checkout main
git merge hotfix/descripcion-del-fix
git checkout develop
git merge hotfix/descripcion-del-fix

# 5. Eliminar rama hotfix
git branch -d hotfix/descripcion-del-fix
```

### Reglas para Pull Requests

- Todo PR debe tener **título descriptivo** siguiendo Conventional Commits
- Incluir **descripción** con: qué se hizo, por qué y cómo probarlo
- Mínimo **1 aprobación** requerida antes de hacer merge
- No hacer merge si el **CI falla** (GitHub Actions en rojo)
- Usar **Squash and Merge** para features, **Merge Commit** para hotfixes

---

## Estrategia de Revisión de Código

### ¿Qué revisar en un PR?

1. **Funcionalidad**: ¿El código hace lo que dice el PR?
2. **Legibilidad**: ¿Es fácil de entender sin comentarios excesivos?
3. **Convenciones**: ¿Sigue el estilo del proyecto (naming, estructura)?
4. **Tests**: ¿Se agregaron o actualizaron pruebas?
5. **Seguridad**: ¿Hay credenciales hardcodeadas o datos sensibles expuestos?

### Etiquetas de revisión

| Etiqueta | Significado |
|----------|-------------|
| ✅ Approved | Listo para merge |
| 💬 Comment | Sugerencia menor, no bloquea |
| ❌ Request Changes | Cambio requerido antes del merge |

---

## Estructura del Proyecto

```
ticket/
├── src/
│   ├── main/
│   │   ├── java/com/example/ticket/
│   │   │   ├── application/          # Casos de uso (servicios)
│   │   │   │   └── TicketService.java
│   │   │   ├── domain/
│   │   │   │   ├── model/            # Entidades del dominio
│   │   │   │   └── port/
│   │   │   │       ├── in/           # Puertos de entrada (use cases)
│   │   │   │       └── out/          # Puertos de salida (repositorios)
│   │   │   └── infrastructure/
│   │   │       ├── adapter/
│   │   │       │   ├── in/web/       # Controladores REST
│   │   │       │   └── out/
│   │   │       │       ├── notification/
│   │   │       │       └── persistence/  # Adaptadores JPA y File
│   │   │       └── config/           # Configuración de beans
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Cómo Ejecutar

### Requisitos

- Java 17+
- Docker y Docker Compose

### Con Docker Compose (recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/<tu-usuario>/ticket.git
cd ticket

# Levantar el servicio y la base de datos
docker compose up --build
```

La API estará disponible en: `http://localhost:8081`

### Ejemplo de uso

```bash
# Registrar un ticket
curl -X POST http://localhost:8081/tickets/db \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Error en login", "descripcion": "El usuario no puede iniciar sesión"}'

# Consultar estado
curl http://localhost:8081/tickets/db/1/estado
```

---

## GitHub Actions — CI/CD

Se configuró un workflow de integración continua que se ejecuta automáticamente en cada `push` a `develop` y en cada `pull request` hacia `main`.

**¿Qué hace el pipeline?**
1. Compila el proyecto con Maven
2. Ejecuta los tests automáticos
3. Verifica que el build sea exitoso antes de permitir merge

El archivo de configuración se encuentra en `.github/workflows/ci.yml`.

---

## Uso de Inteligencia Artificial

Durante el desarrollo de este encargo se utilizó **Claude (Anthropic)** como herramienta de apoyo para:

- Revisión y mejora de redacción del README
- Consultas sobre convenciones de GitFlow
- Generación de la estructura base del workflow de GitHub Actions

Todas las decisiones técnicas, justificaciones y reflexiones fueron elaboradas por el equipo. El contenido generado por IA fue revisado, validado y adaptado al contexto del proyecto.

> Referencia de citación: https://bibliotecas.duoc.cl/ia

> ## Reflexiones Individuales

### Integrante 1 — Dominique Cofre
Aprendi a usar el gitflow para el trabjo en equipo, a ocupar distintas ramas para el desarrollo del proyecto,Lo más desafiante fue resolver los conflictos al realizar los pull requests entre ramas, ya que requería entender qué cambios conservar. También tuve dificultades configurando el workflow de GitHub Actions, principalmente con los permisos del archivo mvnw y la conexión con la base de datos, pero logré resolverlo aplicando los conocimientos del curso.

### Integrante 2 — Hector Peña
..
