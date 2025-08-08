-- V1.1__Insert_mock_data.sql
-- Flyway migration script to insert 15 mock entries for each table
-- Generated for MoodyClues database schema

-- Disable foreign key checks temporarily for easier insertion
SET FOREIGN_KEY_CHECKS = 0;

-- ===================================================================
-- EMOTIONS TABLE (8 entries)
-- ===================================================================
INSERT INTO emotions (id, emotion_label, icon_address) VALUES
(UUID(), b'0', 'olivia.rodriguez@email.com', 'Olivia', 'Rodriguez', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0');

-- ===================================================================
-- COUNSELLOR_USER TABLE (15 entries)
-- ===================================================================
INSERT INTO counsellor_user (id, archived, email, first_name, last_name, password) VALUES
(UUID(), b'0', 'dr.sarah.connor@clinic.com', 'Sarah', 'Connor', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.john.watson@clinic.com', 'John', 'Watson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.mary.poppins@clinic.com', 'Mary', 'Poppins', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.peter.parker@clinic.com', 'Peter', 'Parker', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.jane.doe@clinic.com', 'Jane', 'Doe', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.michael.scott@clinic.com', 'Michael', 'Scott', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.lisa.simpson@clinic.com', 'Lisa', 'Simpson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.bruce.banner@clinic.com', 'Bruce', 'Banner', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.diana.prince@clinic.com', 'Diana', 'Prince', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.clark.kent@clinic.com', 'Clark', 'Kent', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.natasha.romanoff@clinic.com', 'Natasha', 'Romanoff', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.tony.stark@clinic.com', 'Tony', 'Stark', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.steve.rogers@clinic.com', 'Steve', 'Rogers', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.wanda.maximoff@clinic.com', 'Wanda', 'Maximoff', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(UUID(), b'0', 'dr.stephen.strange@clinic.com', 'Stephen', 'Strange', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');

-- ===================================================================
-- JOURNAL_ENTRIES TABLE (15 entries)
-- Using existing journal_users
-- ===================================================================
INSERT INTO journal_entries (id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id) VALUES
(UUID(), b'0', '2024-01-15 09:00:00', '2024-01-15 09:15:00', 'Had a great start to the week. Feeling optimistic about upcoming projects.', 'Monday Motivation', 8, (SELECT id FROM journal_users WHERE email = 'alice.johnson@email.com')),
(UUID(), b'0', '2024-01-16 14:30:00', '2024-01-16 14:45:00', 'Work was challenging today but I managed to overcome the obstacles.', 'Overcoming Challenges', 6, (SELECT id FROM journal_users WHERE email = 'bob.smith@email.com')),
(UUID(), b'0', '2024-01-17 20:15:00', '2024-01-17 20:30:00', 'Spent quality time with family. These moments are precious.', 'Family Time', 9, (SELECT id FROM journal_users WHERE email = 'carol.davis@email.com')),
(UUID(), b'0', '2024-01-18 08:45:00', '2024-01-18 09:00:00', 'Feeling a bit anxious about the presentation tomorrow.', 'Pre-Presentation Jitters', 4, (SELECT id FROM journal_users WHERE email = 'david.wilson@email.com')),
(UUID(), b'0', '2024-01-19 16:20:00', '2024-01-19 16:35:00', 'Presentation went well! Feeling proud of my preparation.', 'Success!', 9, (SELECT id FROM journal_users WHERE email = 'emma.brown@email.com')),
(UUID(), b'0', '2024-01-20 11:10:00', '2024-01-20 11:25:00', 'Rainy day today. Perfect for reading and reflecting.', 'Rainy Day Reflections', 7, (SELECT id FROM journal_users WHERE email = 'frank.miller@email.com')),
(UUID(), b'0', '2024-01-21 19:30:00', '2024-01-21 19:45:00', 'Tried a new recipe today. Cooking is becoming therapeutic.', 'Culinary Adventures', 8, (SELECT id FROM journal_users WHERE email = 'grace.taylor@email.com')),
(UUID(), b'0', '2024-01-22 07:00:00', '2024-01-22 07:15:00', 'Early morning run felt invigorating. Great way to start Sunday.', 'Sunday Morning Run', 8, (SELECT id FROM journal_users WHERE email = 'henry.anderson@email.com')),
(UUID(), b'0', '2024-01-23 13:45:00', '2024-01-23 14:00:00', 'Meeting with old friends reminded me of simpler times.', 'Nostalgia', 7, (SELECT id FROM journal_users WHERE email = 'iris.thomas@email.com')),
(UUID(), b'0', '2024-01-24 21:00:00', '2024-01-24 21:15:00', 'Feeling overwhelmed with all the tasks ahead.', 'Overwhelmed', 3, (SELECT id FROM journal_users WHERE email = 'jack.jackson@email.com')),
(UUID(), b'0', '2024-01-25 10:30:00', '2024-01-25 10:45:00', 'Found a solution to yesterdays problem. Persistence pays off.', 'Problem Solved', 8, (SELECT id FROM journal_users WHERE email = 'kate.white@email.com')),
(UUID(), b'0', '2024-01-26 15:15:00', '2024-01-26 15:30:00', 'Attended a workshop that opened new perspectives.', 'Learning Day', 7, (SELECT id FROM journal_users WHERE email = 'liam.harris@email.com')),
(UUID(), b'0', '2024-01-27 18:00:00', '2024-01-27 18:15:00', 'Quiet evening at home. Sometimes solitude is exactly what I need.', 'Peaceful Evening', 6, (SELECT id FROM journal_users WHERE email = 'mia.martin@email.com')),
(UUID(), b'0', '2024-01-28 12:20:00', '2024-01-28 12:35:00', 'Volunteered at the local shelter. Giving back feels meaningful.', 'Giving Back', 9, (SELECT id FROM journal_users WHERE email = 'noah.garcia@email.com')),
(UUID(), b'0', '2024-01-29 17:40:00', '2024-01-29 17:55:00', 'Week ending on a high note. Ready for new adventures.', 'Week Wrap-up', 8, (SELECT id FROM journal_users WHERE email = 'olivia.rodriguez@email.com'));

-- ===================================================================
-- HABITS_ENTRIES TABLE (15 entries)
-- Using existing journal_users
-- ===================================================================
INSERT INTO habits_entries (id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id) VALUES
(UUID(), b'0', '2024-01-15 23:59:59', '2024-01-15 23:59:59', 7.5, 2.1, 8.0, (SELECT id FROM journal_users WHERE email = 'alice.johnson@email.com')),
(UUID(), b'0', '2024-01-16 23:59:59', '2024-01-16 23:59:59', 6.8, 1.8, 9.2, (SELECT id FROM journal_users WHERE email = 'bob.smith@email.com')),
(UUID(), b'0', '2024-01-17 23:59:59', '2024-01-17 23:59:59', 8.2, 2.5, 7.5, (SELECT id FROM journal_users WHERE email = 'carol.davis@email.com')),
(UUID(), b'0', '2024-01-18 23:59:59', '2024-01-18 23:59:59', 6.2, 1.9, 8.8, (SELECT id FROM journal_users WHERE email = 'david.wilson@email.com')),
(UUID(), b'0', '2024-01-19 23:59:59', '2024-01-19 23:59:59', 7.8, 2.3, 8.5, (SELECT id FROM journal_users WHERE email = 'emma.brown@email.com')),
(UUID(), b'0', '2024-01-20 23:59:59', '2024-01-20 23:59:59', 8.5, 2.0, 6.0, (SELECT id FROM journal_users WHERE email = 'frank.miller@email.com')),
(UUID(), b'0', '2024-01-21 23:59:59', '2024-01-21 23:59:59', 7.2, 2.4, 7.8, (SELECT id FROM journal_users WHERE email = 'grace.taylor@email.com')),
(UUID(), b'0', '2024-01-22 23:59:59', '2024-01-22 23:59:59', 8.0, 2.2, 5.5, (SELECT id FROM journal_users WHERE email = 'henry.anderson@email.com')),
(UUID(), b'0', '2024-01-23 23:59:59', '2024-01-23 23:59:59', 7.1, 1.7, 8.3, (SELECT id FROM journal_users WHERE email = 'iris.thomas@email.com')),
(UUID(), b'0', '2024-01-24 23:59:59', '2024-01-24 23:59:59', 6.5, 2.1, 10.2, (SELECT id FROM journal_users WHERE email = 'jack.jackson@email.com')),
(UUID(), b'0', '2024-01-25 23:59:59', '2024-01-25 23:59:59', 7.9, 2.6, 8.1, (SELECT id FROM journal_users WHERE email = 'kate.white@email.com')),
(UUID(), b'0', '2024-01-26 23:59:59', '2024-01-26 23:59:59', 8.3, 2.0, 7.2, (SELECT id FROM journal_users WHERE email = 'liam.harris@email.com')),
(UUID(), b'0', '2024-01-27 23:59:59', '2024-01-27 23:59:59', 7.6, 1.9, 8.7, (SELECT id FROM journal_users WHERE email = 'mia.martin@email.com')),
(UUID(), b'0', '2024-01-28 23:59:59', '2024-01-28 23:59:59', 8.1, 2.3, 6.8, (SELECT id FROM journal_users WHERE email = 'noah.garcia@email.com')),
(UUID(), b'0', '2024-01-29 23:59:59', '2024-01-29 23:59:59', 7.4, 2.2, 8.4, (SELECT id FROM journal_users WHERE email = 'olivia.rodriguez@email.com'));

-- ===================================================================
-- LINK_REQUEST TABLE (15 entries)
-- Linking counsellors with journal users
-- ===================================================================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id) VALUES
(UUID(), '2024-01-10 10:00:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.sarah.connor@clinic.com'), (SELECT id FROM journal_users WHERE email = 'alice.johnson@email.com')),
(UUID(), '2024-01-11 11:15:00', 2, (SELECT id FROM counsellor_user WHERE email = 'dr.john.watson@clinic.com'), (SELECT id FROM journal_users WHERE email = 'bob.smith@email.com')),
(UUID(), '2024-01-12 14:30:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.mary.poppins@clinic.com'), (SELECT id FROM journal_users WHERE email = 'carol.davis@email.com')),
(UUID(), '2024-01-13 09:45:00', 0, (SELECT id FROM counsellor_user WHERE email = 'dr.peter.parker@clinic.com'), (SELECT id FROM journal_users WHERE email = 'david.wilson@email.com')),
(UUID(), '2024-01-14 16:20:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.jane.doe@clinic.com'), (SELECT id FROM journal_users WHERE email = 'emma.brown@email.com')),
(UUID(), '2024-01-15 12:10:00', 2, (SELECT id FROM counsellor_user WHERE email = 'dr.michael.scott@clinic.com'), (SELECT id FROM journal_users WHERE email = 'frank.miller@email.com')),
(UUID(), '2024-01-16 13:25:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.lisa.simpson@clinic.com'), (SELECT id FROM journal_users WHERE email = 'grace.taylor@email.com')),
(UUID(), '2024-01-17 15:40:00', 0, (SELECT id FROM counsellor_user WHERE email = 'dr.bruce.banner@clinic.com'), (SELECT id FROM journal_users WHERE email = 'henry.anderson@email.com')),
(UUID(), '2024-01-18 08:55:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.diana.prince@clinic.com'), (SELECT id FROM journal_users WHERE email = 'iris.thomas@email.com')),
(UUID(), '2024-01-19 17:10:00', 2, (SELECT id FROM counsellor_user WHERE email = 'dr.clark.kent@clinic.com'), (SELECT id FROM journal_users WHERE email = 'jack.jackson@email.com')),
(UUID(), '2024-01-20 11:35:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.natasha.romanoff@clinic.com'), (SELECT id FROM journal_users WHERE email = 'kate.white@email.com')),
(UUID(), '2024-01-21 14:50:00', 0, (SELECT id FROM counsellor_user WHERE email = 'dr.tony.stark@clinic.com'), (SELECT id FROM journal_users WHERE email = 'liam.harris@email.com')),
(UUID(), '2024-01-22 10:05:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.steve.rogers@clinic.com'), (SELECT id FROM journal_users WHERE email = 'mia.martin@email.com')),
(UUID(), '2024-01-23 16:15:00', 2, (SELECT id FROM counsellor_user WHERE email = 'dr.wanda.maximoff@clinic.com'), (SELECT id FROM journal_users WHERE email = 'noah.garcia@email.com')),
(UUID(), '2024-01-24 12:45:00', 1, (SELECT id FROM counsellor_user WHERE email = 'dr.stephen.strange@clinic.com'), (SELECT id FROM journal_users WHERE email = 'olivia.rodriguez@email.com'));

-- ===================================================================
-- COUNSELLOR_CLIENT TABLE (15 entries)
-- Only creating relationships for accepted link requests (status = 1)
-- ===================================================================
INSERT INTO counsellor_client (counsellor_id, client_id) VALUES
((SELECT id FROM counsellor_user WHERE email = 'dr.sarah.connor@clinic.com'), (SELECT id FROM journal_users WHERE email = 'alice.johnson@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.mary.poppins@clinic.com'), (SELECT id FROM journal_users WHERE email = 'carol.davis@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.jane.doe@clinic.com'), (SELECT id FROM journal_users WHERE email = 'emma.brown@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.lisa.simpson@clinic.com'), (SELECT id FROM journal_users WHERE email = 'grace.taylor@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.diana.prince@clinic.com'), (SELECT id FROM journal_users WHERE email = 'iris.thomas@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.natasha.romanoff@clinic.com'), (SELECT id FROM journal_users WHERE email = 'kate.white@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.steve.rogers@clinic.com'), (SELECT id FROM journal_users WHERE email = 'mia.martin@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.stephen.strange@clinic.com'), (SELECT id FROM journal_users WHERE email = 'olivia.rodriguez@email.com')),
-- Additional entries to reach 15 (some counsellors can have multiple clients)
((SELECT id FROM counsellor_user WHERE email = 'dr.sarah.connor@clinic.com'), (SELECT id FROM journal_users WHERE email = 'bob.smith@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.john.watson@clinic.com'), (SELECT id FROM journal_users WHERE email = 'david.wilson@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.mary.poppins@clinic.com'), (SELECT id FROM journal_users WHERE email = 'frank.miller@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.peter.parker@clinic.com'), (SELECT id FROM journal_users WHERE email = 'henry.anderson@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.jane.doe@clinic.com'), (SELECT id FROM journal_users WHERE email = 'jack.jackson@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.michael.scott@clinic.com'), (SELECT id FROM journal_users WHERE email = 'liam.harris@email.com')),
((SELECT id FROM counsellor_user WHERE email = 'dr.lisa.simpson@clinic.com'), (SELECT id FROM journal_users WHERE email = 'noah.garcia@email.com'));

-- ===================================================================
-- ENTRY_EMOTIONS TABLE (15 entries)
-- Linking journal entries with emotions
-- ===================================================================
INSERT INTO entry_emotions (entry_id, emotion_id) VALUES
((SELECT id FROM journal_entries WHERE entry_title = 'Monday Motivation'), (SELECT id FROM emotions WHERE emotion_label = 'happy')),
((SELECT id FROM journal_entries WHERE entry_title = 'Overcoming Challenges'), (SELECT id FROM emotions WHERE emotion_label = 'curious')),
((SELECT id FROM journal_entries WHERE entry_title = 'Family Time'), (SELECT id FROM emotions WHERE emotion_label = 'happy')),
((SELECT id FROM journal_entries WHERE entry_title = 'Pre-Presentation Jitters'), (SELECT id FROM emotions WHERE emotion_label = 'anxious')),
((SELECT id FROM journal_entries WHERE entry_title = 'Success!'), (SELECT id FROM emotions WHERE emotion_label = 'surprised')),
((SELECT id FROM journal_entries WHERE entry_title = 'Rainy Day Reflections'), (SELECT id FROM emotions WHERE emotion_label = 'neutral')),
((SELECT id FROM journal_entries WHERE entry_title = 'Culinary Adventures'), (SELECT id FROM emotions WHERE emotion_label = 'curious')),
((SELECT id FROM journal_entries WHERE entry_title = 'Sunday Morning Run'), (SELECT id FROM emotions WHERE emotion_label = 'happy')),
((SELECT id FROM journal_entries WHERE entry_title = 'Nostalgia'), (SELECT id FROM emotions WHERE emotion_label = 'sad')),
((SELECT id FROM journal_entries WHERE entry_title = 'Overwhelmed'), (SELECT id FROM emotions WHERE emotion_label = 'confused')),
((SELECT id FROM journal_entries WHERE entry_title = 'Problem Solved'), (SELECT id FROM emotions WHERE emotion_label = 'happy')),
((SELECT id FROM journal_entries WHERE entry_title = 'Learning Day'), (SELECT id FROM emotions WHERE emotion_label = 'curious')),
((SELECT id FROM journal_entries WHERE entry_title = 'Peaceful Evening'), (SELECT id FROM emotions WHERE emotion_label = 'neutral')),
((SELECT id FROM journal_entries WHERE entry_title = 'Giving Back'), (SELECT id FROM emotions WHERE emotion_label = 'happy')),
((SELECT id FROM journal_entries WHERE entry_title = 'Week Wrap-up'), (SELECT id FROM emotions WHERE emotion_label = 'happy'));

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;(), 'angry', NULL),
(UUID(), 'sad', NULL),
(UUID(), 'anxious', NULL),
(UUID(), 'happy', NULL),
(UUID(), 'curious', NULL),
(UUID(), 'confused', NULL),
(UUID(), 'surprised', NULL),
(UUID(), 'neutral', NULL);

-- ===================================================================
-- JOURNAL_USERS TABLE (15 entries)
-- ===================================================================
INSERT INTO journal_users (id, archived, email, first_name, last_name, password, show_emotion) VALUES
(UUID(), b'0', 'alice.johnson@email.com', 'Alice', 'Johnson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'bob.smith@email.com', 'Bob', 'Smith', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'carol.davis@email.com', 'Carol', 'Davis', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0'),
(UUID(), b'0', 'david.wilson@email.com', 'David', 'Wilson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'emma.brown@email.com', 'Emma', 'Brown', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'frank.miller@email.com', 'Frank', 'Miller', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0'),
(UUID(), b'0', 'grace.taylor@email.com', 'Grace', 'Taylor', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'henry.anderson@email.com', 'Henry', 'Anderson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'iris.thomas@email.com', 'Iris', 'Thomas', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0'),
(UUID(), b'0', 'jack.jackson@email.com', 'Jack', 'Jackson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'kate.white@email.com', 'Kate', 'White', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'liam.harris@email.com', 'Liam', 'Harris', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0'),
(UUID(), b'0', 'mia.martin@email.com', 'Mia', 'Martin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID(), b'0', 'noah.garcia@email.com', 'Noah', 'Garcia', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'1'),
(UUID