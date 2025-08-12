-- V5__seed_link_requests_and_links.sql
-- Seeds link_request rows and establishes successful counsellor_client links.
-- Enum ordinal mapping (per entity): 0=PENDING, 1=ACCEPTED, 2=DECLINED.
-- Timestamps: authored in Asia/Singapore, stored in UTC.

SET FOREIGN_KEY_CHECKS = 0;

-- Current time in Asia/Singapore (preferred: requires timezone tables)
SET @now_sg := CONVERT_TZ(UTC_TIMESTAMP(),'UTC','Asia/Singapore');
-- Fallback if timezone tables are not loaded (SG has no DST):
-- SET @now_sg := UTC_TIMESTAMP() + INTERVAL 8 HOUR;

-- Resolve counsellor IDs by email
SET @c_sarah := (SELECT id FROM counsellor_user WHERE email = 'dr.sarah.connor@clinic.com' LIMIT 1);
SET @c_lisa  := (SELECT id FROM counsellor_user WHERE email = 'dr.lisa.simpson@clinic.com'  LIMIT 1);
SET @c_bruce := (SELECT id FROM counsellor_user WHERE email = 'dr.bruce.banner@clinic.com'  LIMIT 1);

-- Resolve the five journal users used for seeded entries
SET @u_alice := (SELECT id FROM journal_users WHERE email = 'alice.johnson@email.com' LIMIT 1);
SET @u_bob   := (SELECT id FROM journal_users WHERE email = 'bob.smith@email.com'   LIMIT 1);
SET @u_carol := (SELECT id FROM journal_users WHERE email = 'carol.davis@email.com' LIMIT 1);
SET @u_david := (SELECT id FROM journal_users WHERE email = 'david.wilson@email.com' LIMIT 1);
SET @u_emma  := (SELECT id FROM journal_users WHERE email = 'emma.brown@email.com'  LIMIT 1);

-- Helper: convert an Asia/Singapore local timestamp to UTC for storage
-- Usage pattern below: CONVERT_TZ(@now_sg - INTERVAL n DAY,'Asia/Singapore','UTC')
-- Fallback if no tz tables:
--   (@now_sg - INTERVAL n DAY) - INTERVAL 8 HOUR

-- ===========================
-- Sarah ↔ Alice (ACCEPTED)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 4 DAY,'Asia/Singapore','UTC'),
       1, @c_sarah, @u_alice
FROM DUAL
WHERE @c_sarah IS NOT NULL AND @u_alice IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_sarah AND journal_user_id = @u_alice AND status = 1
  );

-- ===========================
-- Sarah ↔ Bob (ACCEPTED)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 3 DAY,'Asia/Singapore','UTC'),
       1, @c_sarah, @u_bob
FROM DUAL
WHERE @c_sarah IS NOT NULL AND @u_bob IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_sarah AND journal_user_id = @u_bob AND status = 1
  );

-- ===========================
-- Sarah ↔ Emma (PENDING)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 2 DAY,'Asia/Singapore','UTC'),
       0, @c_sarah, @u_emma
FROM DUAL
WHERE @c_sarah IS NOT NULL AND @u_emma IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_sarah AND journal_user_id = @u_emma AND status = 0
  );

-- ===========================
-- Lisa ↔ Carol (ACCEPTED)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 4 DAY,'Asia/Singapore','UTC'),
       1, @c_lisa, @u_carol
FROM DUAL
WHERE @c_lisa IS NOT NULL AND @u_carol IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_lisa AND journal_user_id = @u_carol AND status = 1
  );

-- ===========================
-- Lisa ↔ David (DECLINED)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 1 DAY,'Asia/Singapore','UTC'),
       2, @c_lisa, @u_david
FROM DUAL
WHERE @c_lisa IS NOT NULL AND @u_david IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_lisa AND journal_user_id = @u_david AND status = 2
  );

-- ===========================
-- Bruce ↔ David (ACCEPTED)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 3 DAY,'Asia/Singapore','UTC'),
       1, @c_bruce, @u_david
FROM DUAL
WHERE @c_bruce IS NOT NULL AND @u_david IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_bruce AND journal_user_id = @u_david AND status = 1
  );

-- ===========================
-- Bruce ↔ Carol (PENDING)
-- ===========================
INSERT INTO link_request (id, requested_at, status, counsellor_user_id, journal_user_id)
SELECT UUID(),
       CONVERT_TZ(@now_sg - INTERVAL 1 DAY,'Asia/Singapore','UTC'),
       0, @c_bruce, @u_carol
FROM DUAL
WHERE @c_bruce IS NOT NULL AND @u_carol IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM link_request
    WHERE counsellor_user_id = @c_bruce AND journal_user_id = @u_carol AND status = 0
  );

-- Create counsellor_client rows for the ACCEPTED requests above (idempotent).
INSERT INTO counsellor_client (counsellor_id, client_id)
SELECT lr.counsellor_user_id, lr.journal_user_id
FROM link_request lr
JOIN counsellor_user cu ON cu.id = lr.counsellor_user_id
WHERE lr.status = 1
  AND cu.email IN (
    'dr.sarah.connor@clinic.com',
    'dr.lisa.simpson@clinic.com',
    'dr.bruce.banner@clinic.com'
  )
  AND NOT EXISTS (
    SELECT 1
    FROM counsellor_client cc
    WHERE cc.counsellor_id = lr.counsellor_user_id
      AND cc.client_id      = lr.journal_user_id
  );

SET FOREIGN_KEY_CHECKS = 1;
