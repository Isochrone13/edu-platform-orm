# Edu Platform — учебная платформа на Spring Boot и PostgreSQL

## Кратко о проекте

Edu Platform — это монолитное Spring Boot-приложение, которое моделирует базовую функциональность учебной платформы:

- преподаватели создают **курсы**, разбивают их на **модули** и **уроки**;
- к урокам можно прикреплять **материалы** и **домашние задания**;
- студенты **записываются на курсы**, сдают **задания** и проходят **тесты (quizzes)**;
- система хранит **попытки прохождения тестов**, **оценки**, **отзывы** и **расписание занятий**.

Цель проекта — отработать навыки:

- проектирования доменной модели с большим количеством связей;
- грамотного использования JPA/Hibernate;
- написания REST-слоя с DTO и валидацией;
- создания интеграционных тестов с использованием **Testcontainers**.

---

## Технологический стек

**Язык и платформа**

- Java **23**
- Spring Boot **3.x**

**Persistence**

- Spring Data JPA
- Hibernate ORM
- PostgreSQL

**Тестирование**

- JUnit 5
- Spring Boot Test
- Testcontainers (контейнер с PostgreSQL поднимается автоматически для интеграционных тестов)

**Сборка**

- Maven

**Контейнеризация**

- Dockerfile (JRE на базе `eclipse-temurin:23-jre-alpine`)
- `docker-compose.yml` (служба с PostgreSQL)

---

## Структура проекта

```text
src/main/java/com/example/edu
├─ EduPlatformApplication.java    # Точка входа (Spring Boot)
│
├─ domain/                        # JPA-сущности
│  ├─ BaseEntity.java             # id, createdAt, updatedAt
│  ├─ DomainConstraints.java      # общие ограничения
│  ├─ course/                     # всё, что связано с курсами
│  ├─ quiz/                       # сущности тестов
│  ├─ user/                       # пользователи, профили, записи, отправки
│  └─ enums/                      # перечисления ролей, статусов и т.п.
│
├─ dto/                           # DTO для REST-слоя
│  ├─ CourseCreateDto.java
│  ├─ AssignmentCreateDto.java
│  ├─ EnrollmentRequestDto.java
│  ├─ QuestionCreateDto.java
│  ├─ AnswerOptionCreateDto.java
│  ├─ UserDto.java
│  └─ DtoConstraints.java
│
├─ repository/                    # Spring Data JPA репозитории
│  ├─ CourseRepository.java
│  ├─ UserRepository.java
│  ├─ EnrollmentRepository.java
│  ├─ AssignmentRepository.java
│  ├─ SubmissionRepository.java
│  ├─ QuizRepository.java
│  ├─ QuestionRepository.java
│  ├─ AnswerOptionRepository.java
│  └─ ...
│
├─ service/                       # Бизнес-логика и транзакции
│  ├─ CourseService.java
│  ├─ EnrollmentService.java
│  ├─ AssignmentService.java
│  ├─ SubmissionService.java
│  └─ QuizService.java
│
├─ rest/                          # REST-контроллеры
│  ├─ UserController.java
│  ├─ CourseController.java
│  ├─ EnrollmentController.java
│  ├─ AssignmentController.java
│  ├─ SubmissionController.java
│  └─ QuizController.java
│
└─ exception/
   └─ GlobalExceptionHandler.java # @ControllerAdvice с обработкой ошибок
```

Ресурсы:

```text
src/main/resources
├─ application.yml         # основная конфигурация
├─ application-test.yml    # конфиг для тестов
├─ data.sql                # демо-данные
└─ requests.http           # примеры HTTP-запросов
```

---

## Доменные сущности

Модель разбита на несколько пакетов:

### Пользователи и обучение

- `User` — базовая сущность пользователя (имя, email, роль: STUDENT / TEACHER / ADMIN).
- `Profile` — расширенная информация о пользователе (био, соцсети и т.п.).
- `Enrollment` — запись студента на курс, статус (`EnrollmentStatus`: ACTIVE, COMPLETED, DROPPED и т.д.), даты.
- `Submission` — отправка домашнего задания студентом, оценка и текстовый фидбек.
- `Notification` — простая модель уведомлений (можно расширять под реальные сценарии).

### Курсы и контент

Пакет `domain.course`:

- `Course` — основной объект курса: заголовок, описание, категория, преподаватель, теги, расписание и отзывы.
- `Category` — категория курса (Programming, Data Science, …).
- `CourseModule` — модуль курса (логический блок, объединяющий уроки).
- `Lesson` — конкретный урок внутри модуля.
- `Material` — связанный с уроком материал: ссылка, файл, видео и т.п. (`ResourceType` держит тип ресурса).
- `Assignment` — задание к уроку.
- `CourseReview` — отзыв студента о курсе (оценка + комментарий).
- `CourseSchedule` — расписание проведения курса: даты начала/окончания, день недели.
- `Tag` — тег для курсов (например, `spring`, `java`, `algorithms`).

### Тесты (Quizzes)

Пакет `domain.quiz`:

- `Quiz` — тест, привязанный к курсу или уроку.
- `Question` — вопрос (тип задаётся через `QuestionType`, например SINGLE_CHOICE / MULTIPLE_CHOICE / TEXT).
- `AnswerOption` — варианты ответов для вопросов с выбором.
- `QuizSubmission` — попытка прохождения теста студентом с результатом и датой.

### Общие сущности

- `BaseEntity` — базовый класс с id, `createdAt`, `updatedAt`.
- `DomainConstraints` — централизованные ограничения (на данный момент добавлены параметры для длины строк).
- `UserRole`, `EnrollmentStatus`, `QuestionType`, `ResourceType` — перечисления (enum) для доменной логики.

---

## Запуск проекта

### Вариант 1: локально + Docker для PostgreSQL

1. **Поднять PostgreSQL через Docker Compose**
   ```bash
   docker compose up -d
   ```

   Это поднимет контейнер `pg` с PostgreSQL и БД `edu_platform` (имя и креды можно переопределить через переменные окружения, см. ниже).

2. **Настроить подключение в приложении**

   Файл `src/main/resources/application.yml` использует env-переменные.
   C текущими настройками, приложение при старте само:

    - создаст/обновит схему БД (`ddl-auto: update`);
    - выполнит `data.sql` с демо-данными.

3. **Запустить приложение**
   ```bash
   mvn spring-boot:run
   ```
   После старта приложение будет доступно по адресу:

   ```text
   http://localhost:8080
   ```

4. **Проверить, что демо-данные подхватились набрав в строке браузера:**

    - `http://localhost:8080/api/users`
    - `http://localhost:8080/api/courses`

   Файл `src/main/resources/requests.http` содержит остальные примеры запросов.

---

### Переменные окружения

Можно переопределять настройки подключения к БД и порт сервера:

- `DB_HOST` (по умолчанию `localhost`)
- `DB_PORT` (по умолчанию `5432`)
- `DB_NAME` (по умолчанию `edu_platform`)
- `DB_USER` (по умолчанию `edu_platform`)
- `DB_PASSWORD` (по умолчанию `edu_platform`)
- `PORT` — порт HTTP-сервера Spring Boot (по умолчанию `8080`)

---

## REST API (основные сценарии)

Ниже — обзор ключевых endpoint’ов. Точные сигнатуры можно посмотреть в пакетах `rest` и DTO в `dto`.

### Пользователи

Контроллер: `UserController`

- `GET /api/users` — список всех пользователей.
- `POST /api/users` — создание пользователя.
- `GET /api/users/{id}` — карточка пользователя.

### Курсы и структура курса

Контроллер: `CourseController`

- `GET /api/courses` — список курсов (с базовой информацией).
- `GET /api/courses/{id}` — детальная информация о курсе.
- `POST /api/courses` — создание курса.
- `DELETE /api/courses/{id}` — удаление курса.

### Запись на курс (Enrollments)

Контроллер: `EnrollmentController`

- `POST /api/enrollments` — студент записывается на курс.
- `GET /api/enrollments/student/{id}` — список записей.
- `DELETE /api/enrollments/{id}` — отменить запись студента на курс.

### Домашние задания и решения

Контроллер: `AssignmentController`

- `POST /api/assignments` — создание задания к уроку.
- `POST /api/assignments/{assignmentId}/submit` — отправить решение по заданию.
- `PUT /api/assignments/grade/{submissionId}` — оценить решение.


Контроллер: `SubmissionController`
- `POST /api/submissions` — отправка решения студентом.
- `GET /api/submissions/{id}` — получить одно решение по ID.
- `GET /api/submissions/assignment/{assignmentId}` — получить все решения по конкретному заданию.
- `GET /api/submissions/student/{studentId}` — получить все решения конкретного студента.
- `PUT /api/submissions/{id}/grade` — получить одно решение по ID.
- `DELETE /api/submissions/{id}` — Удалить решение.

### Тесты (Quizzes)

Контроллер: `QuizController`

Примерный набор:

- `POST /api/quizzes` — создать тест.
- `POST /api/quizzes/question` — добавить вопрос.
- `POST /api/quizzes/option` — добавить вариант ответа.
- `POST /api/quizzes/{quizId}/submit` — отправить попытку прохождения теста студентом.

---

## Тестирование

Интеграционные тесты находятся в `src/test/java/com/example/edu/tests/CrudTests.java`.

Основные моменты:

- Используется **Testcontainers** для PostgreSQL — тесты не зависят от локально установленной БД.
- Поднимается Spring Boot контекст, гоняются реальные репозитории и JPA-модель.
- Проверяются CRUD-сценарии: создание курсов, записей, отправок, каскадное удаление и т.п.

Запуск:

```bash
mvn test
```
