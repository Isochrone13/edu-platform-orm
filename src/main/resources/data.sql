-- ========== USERS ============================================================
INSERT INTO users (id, name, email, role, created_at, updated_at)
VALUES
    (1, 'Ada Lovelace',  'ada@edu.local',   'TEACHER', now(), now()),
    (2, 'Alan Turing',   'alan@edu.local',  'TEACHER', now(), now()),
    (3, 'Grace Hopper',  'grace@edu.local', 'ADMIN',   now(), now()),
    (4, 'Bob Rivers',    'bob@edu.local',   'STUDENT', now(), now()),
    (5, 'Charlie Bloom', 'charlie@edu.local','STUDENT',now(), now()),
    (6, 'Diana Prince',  'diana@edu.local', 'STUDENT', now(), now()),
    (7, 'Eve Stone',     'eve@edu.local',   'STUDENT', now(), now()),
    (8, 'Frank West',    'frank@edu.local', 'STUDENT', now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== PROFILES =========================================================
INSERT INTO profiles (id, user_id, bio, avatar_url, created_at, updated_at)
VALUES
    (1, 1, 'Teaches Java & algorithms. Fan of clean code.', NULL, now(), now()),
    (2, 2, 'Distributed systems & microservices mentor.',    NULL, now(), now()),
    (3, 3, 'Keeps platform running.',                         NULL, now(), now()),
    (4, 4, 'Aspiring backend dev.',                           NULL, now(), now()),
    (5, 5, 'Enjoys coding puzzles.',                          NULL, now(), now()),
    (6, 6, 'Learning cloud-native.',                          NULL, now(), now()),
    (7, 7, 'Frontend→backend switcher.',                      NULL, now(), now()),
    (8, 8, 'Automation enthusiast.',                          NULL, now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== CATEGORIES =======================================================
INSERT INTO categories (id, name, created_at, updated_at)
VALUES
    (1, 'Programming',     now(), now()),
    (2, 'Data Engineering',now(), now()),
    (3, 'Design',          now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== TAGS =============================================================
INSERT INTO tags (id, name, created_at, updated_at)
VALUES
    (1, 'Java',       now(), now()),
    (2, 'Spring',     now(), now()),
    (3, 'Hibernate',  now(), now()),
    (4, 'Docker',     now(), now()),
    (5, 'PostgreSQL', now(), now()),
    (6, 'REST',       now(), now()),
    (7, 'Kafka',      now(), now()),
    (8, 'UI/UX',      now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== COURSES ==========================================================
INSERT INTO courses (id, title, description, category_id, teacher_id, created_at, updated_at)
VALUES
    (1, 'Java Fundamentals',          'A practical path into Java with real tasks.', 1, 1, now(), now()),
    (2, 'Spring Boot Microservices',  'From REST to production patterns.',            1, 2, now(), now()),
    (3, 'Data Pipelines with Kafka',  'Streams, topics, consumers, and ops.',        2, 2, now(), now())
    ON CONFLICT (id) DO NOTHING;

-- course_tags
INSERT INTO course_tags (course_id, tag_id) VALUES
                                                (1, 1), (1, 3),
                                                (2, 2), (2, 3), (2, 4), (2, 5), (2, 6),
                                                (3, 7), (3, 5), (3, 4)
    ON CONFLICT DO NOTHING;

-- ========== MODULES ==========================================================
INSERT INTO modules (id, course_id, title, order_index, created_at, updated_at)
VALUES
    (1, 1, 'Language Basics',            1, now(), now()),
    (2, 1, 'Object-Oriented Design',     2, now(), now()),
    (3, 1, 'Collections & Generics',     3, now(), now()),
    (4, 2, 'REST & JSON',                1, now(), now()),
    (5, 2, 'Data Access with JPA',       2, now(), now()),
    (6, 3, 'Kafka Core',                 1, now(), now()),
    (7, 3, 'Kafka Streams',              2, now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== LESSONS ==========================================================
INSERT INTO lessons (id, module_id, title, content, created_at, updated_at)
VALUES
    (1, 1, 'Variables',                  'Primitive vs reference types with gotchas.', now(), now()),
    (2, 1, 'Control Flow',               'if/else, switch, loops, and patterns.',      now(), now()),
    (3, 2, 'Classes & Objects',          'Encapsulation and constructors.',            now(), now()),
    (4, 3, 'Collections Overview',       'List, Set, Map — complexity & use-cases.',   now(), now()),
    (5, 4, 'Designing Endpoints',        'URI shape, versioning, idempotency.',        now(), now()),
    (6, 5, 'Mapping Entities',           'JPA mappings that don’t bite.',              now(), now()),
    (7, 6, 'Producers & Consumers',      'Delivery semantics and retries.',            now(), now()),
    (8, 7, 'Stream Processing',          'Windowing, joins, and aggregation.',         now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== MATERIALS ========================================================
INSERT INTO materials (id, lesson_id, type, title, url, description, created_at, updated_at)
VALUES
    (1, 1, 'VIDEO',   'Variables in 10 minutes', 'https://example.com/v/vars',  'Quick intro video', now(), now()),
    (2, 3, 'PDF',     'OOP Cheatsheet',          'https://example.com/pdf/oop', 'Key OOP concepts',  now(), now()),
    (3, 5, 'LINK',    'REST Guidelines',         'https://example.com/rest',    'Design best-practices', now(), now()),
    (4, 6, 'ARTICLE', 'JPA Pitfalls',            'https://example.com/jpa',     'Lazy loading explained', now(), now()),
    (5, 7, 'VIDEO',   'Kafka 101',               'https://example.com/kafka',   'Core ideas & demo', now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== ASSIGNMENTS ======================================================
INSERT INTO assignments (id, lesson_id, title, description, due_date, max_score, created_at, updated_at)
VALUES
    (1, 1, 'HW: Variables',     'Write a small REPL that echoes typed values and their types.', now() + interval '7 day', 100, now(), now()),
    (2, 3, 'HW: OOP Model',     'Model a tiny domain with 3 classes and relationships.',       now() + interval '10 day',100, now(), now()),
    (3, 5, 'HW: REST API',      'Design 5 endpoints and return ProblemDetails on errors.',     now() + interval '7 day', 100, now(), now()),
    (4, 7, 'HW: Kafka Producer','Publish JSON events with retries and keys.',                  now() + interval '14 day',100, now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== ENROLLMENTS ======================================================
INSERT INTO enrollments (id, student_id, course_id, status, enrolled_at, created_at, updated_at)
VALUES
    (1, 4, 1, 'ACTIVE',    now(), now(), now()),
    (2, 5, 1, 'ACTIVE',    now(), now(), now()),
    (3, 6, 1, 'COMPLETED', now(), now(), now()),
    (4, 7, 2, 'ACTIVE',    now(), now(), now()),
    (5, 8, 2, 'ACTIVE',    now(), now(), now()),
    (6, 4, 2, 'DROPPED',   now(), now(), now()),
    (7, 5, 3, 'ACTIVE',    now(), now(), now()),
    (8, 6, 3, 'ACTIVE',    now(), now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== COURSE SCHEDULES =================================================
INSERT INTO course_schedules (id, course_id, start_date, end_date, day_of_week, created_at, updated_at)
VALUES
    (1, 1, DATE '2025-01-15', DATE '2025-03-15', 'WEDNESDAY', now(), now()),
    (2, 1, DATE '2025-01-17', DATE '2025-03-17', 'FRIDAY',    now(), now()),
    (3, 2, DATE '2025-02-01', DATE '2025-04-01', 'MONDAY',    now(), now()),
    (4, 3, DATE '2025-03-01', DATE '2025-05-01', 'TUESDAY',   now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== QUIZZES ==========================================================
INSERT INTO quizzes (id, module_id, title, created_at, updated_at)
VALUES
    (1, 1, 'Java Basics Quiz',        now(), now()),
    (2, 4, 'REST Fundamentals Quiz',  now(), now()),
    (3, 6, 'Kafka Intro Quiz',        now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== QUESTIONS ========================================================
INSERT INTO questions (id, quiz_id, text, type, created_at, updated_at)
VALUES
    (1, 1, 'Which keyword defines a class in Java?',                 'SINGLE_CHOICE',   now(), now()),
    (2, 1, 'Select primitive types in Java:',                        'MULTIPLE_CHOICE', now(), now()),
    (3, 1, 'JVM stands for…',                                        'SINGLE_CHOICE',   now(), now()),
    (4, 2, 'Which HTTP methods are idempotent?',                     'MULTIPLE_CHOICE', now(), now()),
    (5, 2, 'HTTP status for resource created successfully is…',      'SINGLE_CHOICE',   now(), now()),
    (6, 3, 'Kafka stores messages in…',                              'SINGLE_CHOICE',   now(), now()),
    (7, 3, 'Select Kafka roles you work with:',                      'MULTIPLE_CHOICE', now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== ANSWER OPTIONS ===================================================
INSERT INTO answer_options (id, question_id, text, is_correct, created_at, updated_at)
VALUES
    -- Q1
    (1,  1, 'class',     true,  now(), now()),
    (2,  1, 'def',       false, now(), now()),
    (3,  1, 'function',  false, now(), now()),
    -- Q2
    (4,  2, 'int',       true,  now(), now()),
    (5,  2, 'boolean',   true,  now(), now()),
    (6,  2, 'String',    false, now(), now()),
    (7,  2, 'double',    true,  now(), now()),
    -- Q3
    (8,  3, 'Java Virtual Machine', true,  now(), now()),
    (9,  3, 'Just Very Massive',    false, now(), now()),
    (10, 3, 'Join Vendor Module',   false, now(), now()),
    -- Q4
    (11, 4, 'GET',   true,  now(), now()),
    (12, 4, 'PUT',   true,  now(), now()),
    (13, 4, 'POST',  false, now(), now()),
    (14, 4, 'DELETE',true,  now(), now()),
    -- Q5
    (15, 5, '201 Created', true,  now(), now()),
    (16, 5, '200 OK',      false, now(), now()),
    (17, 5, '204 No Content', false, now(), now()),
    -- Q6
    (18, 6, 'Topics/partitions (logs)', true,  now(), now()),
    (19, 6, 'RDBMS tables',            false, now(), now()),
    (20, 6, 'XML files',               false, now(), now()),
    -- Q7
    (21, 7, 'Broker',   true,  now(), now()),
    (22, 7, 'Producer', true,  now(), now()),
    (23, 7, 'Renderer', false, now(), now()),
    (24, 7, 'Consumer', true,  now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== QUIZ SUBMISSIONS ================================================
INSERT INTO quiz_submissions (id, quiz_id, student_id, score, taken_at, created_at, updated_at)
VALUES
    (1, 1, 4, 80, now(), now(), now()),
    (2, 1, 5, 60, now(), now(), now()),
    (3, 2, 4, 90, now(), now(), now()),
    (4, 2, 7, 70, now(), now(), now()),
    (5, 3, 5, 85, now(), now(), now()),
    (6, 3, 6, 75, now(), now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== SUBMISSIONS (ASSIGNMENTS) =======================================
INSERT INTO submissions (id, assignment_id, student_id, submitted_at, content, score, feedback, created_at, updated_at)
VALUES
    (1, 1, 4, now(), 'echo x:42, type:int',                  95, 'Well done',            now(), now()),
    (2, 2, 4, now(), 'UML for 3 classes + code skeleton',    88, 'Solid design',         now(), now()),
    (3, 3, 7, now(), 'OpenAPI + ProblemDetails responses',   92, 'Neat error handling',  now(), now()),
    (4, 4, 5, now(), 'Producer with keys + retries',         90, 'Good delivery logic',  now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== COURSE REVIEWS ===================================================
INSERT INTO course_reviews (id, course_id, student_id, rating, comment, created_at, updated_at)
VALUES
    (1, 1, 4, 5, 'Clear explanations and useful tasks.', now(), now()),
    (2, 1, 5, 4, 'Good pace, could add more quizzes.',   now(), now()),
    (3, 2, 7, 5, 'Loved microservices patterns block.',  now(), now()),
    (4, 3, 6, 4, 'Kafka labs are practical.',            now(), now()),
    (5, 2, 8, 3, 'Challenging, needed more examples.',   now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== NOTIFICATIONS ====================================================
INSERT INTO notifications (id, user_id, message, read, created_at, updated_at)
VALUES
    (1, 4, 'Your assignment #1 was graded.', false, now(), now()),
    (2, 7, 'New REST quiz is available.',    false, now(), now()),
    (3, 5, 'Kafka producer task deadline approaching.', false, now(), now())
    ON CONFLICT (id) DO NOTHING;

-- ========== SEQUENCE RESET ===================================================
SELECT setval('users_id_seq',           (SELECT COALESCE(MAX(id), 1) FROM users), true);
SELECT setval('profiles_id_seq',        (SELECT COALESCE(MAX(id), 1) FROM profiles), true);
SELECT setval('categories_id_seq',      (SELECT COALESCE(MAX(id), 1) FROM categories), true);
SELECT setval('tags_id_seq',            (SELECT COALESCE(MAX(id), 1) FROM tags), true);
SELECT setval('courses_id_seq',         (SELECT COALESCE(MAX(id), 1) FROM courses), true);
SELECT setval('modules_id_seq',         (SELECT COALESCE(MAX(id), 1) FROM modules), true);
SELECT setval('lessons_id_seq',         (SELECT COALESCE(MAX(id), 1) FROM lessons), true);
SELECT setval('materials_id_seq',       (SELECT COALESCE(MAX(id), 1) FROM materials), true);
SELECT setval('assignments_id_seq',     (SELECT COALESCE(MAX(id), 1) FROM assignments), true);
SELECT setval('enrollments_id_seq',     (SELECT COALESCE(MAX(id), 1) FROM enrollments), true);
SELECT setval('course_schedules_id_seq',(SELECT COALESCE(MAX(id), 1) FROM course_schedules), true);
SELECT setval('quizzes_id_seq',         (SELECT COALESCE(MAX(id), 1) FROM quizzes), true);
SELECT setval('questions_id_seq',       (SELECT COALESCE(MAX(id), 1) FROM questions), true);
SELECT setval('answer_options_id_seq',  (SELECT COALESCE(MAX(id), 1) FROM answer_options), true);
SELECT setval('quiz_submissions_id_seq',(SELECT COALESCE(MAX(id), 1) FROM quiz_submissions), true);
SELECT setval('submissions_id_seq',     (SELECT COALESCE(MAX(id), 1) FROM submissions), true);
SELECT setval('course_reviews_id_seq',  (SELECT COALESCE(MAX(id), 1) FROM course_reviews), true);
SELECT setval('notifications_id_seq',   (SELECT COALESCE(MAX(id), 1) FROM notifications), true);
