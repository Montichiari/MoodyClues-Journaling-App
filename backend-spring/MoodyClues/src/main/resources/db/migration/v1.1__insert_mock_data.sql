-- V1.1__Insert_mock_data.sql
-- Flyway migration script to insert mock data into MoodyClues database

-- Disable foreign key checks temporarily for insertion
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- EMOTIONS TABLE (8 fixed entries)
-- ========================================
INSERT INTO emotions (id, emotion_label, icon_address) VALUES
(UUID(), 'angry', NULL),
(UUID(), 'sad', NULL),
(UUID(), 'anxious', NULL),
(UUID(), 'happy', NULL),
(UUID(), 'curious', NULL),
(UUID(), 'confused', NULL),
(UUID(), 'surprised', NULL),
(UUID(), 'neutral', NULL);

-- ========================================
-- JOURNAL_USERS TABLE (15 entries)
-- ========================================
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
(UUID(), b'0', 'olivia.rodriguez@email.com', 'Olivia', 'Rodriguez', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', b'0');

-- ========================================
-- COUNSELLOR_USER TABLE (15 entries)
-- ========================================
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

-- Continue with:
-- - journal_entries
-- - habits_entries
-- - link_request
-- - counsellor_client
-- - entry_emotions

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
