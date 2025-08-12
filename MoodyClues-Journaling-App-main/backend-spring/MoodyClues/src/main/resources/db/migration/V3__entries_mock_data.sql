-- V3__insert_more_mock_entries.sql
-- Inserts mock data for: journal_entries, habits_entries, entry_emotions
-- Users: Alice(positive), Bob(balanced), Carol(struggling), David(slightly positive today), Emma(habit only today)

SET FOREIGN_KEY_CHECKS = 0;

-- =========================================================
-- JOURNAL ENTRIES: 15 days x 3/day for ALICE/BOB/CAROL
-- Times: 08:00, 13:00, 20:00
-- Mood via deterministic CRC32 per user (stable across runs)
-- =========================================================
INSERT INTO journal_entries
(id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id)
SELECT
  UUID() AS id,
  b'0'   AS archived,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY),
            MAKETIME(CASE e.n WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 0, 0)) AS created_at,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY),
            MAKETIME(CASE e.n WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 15, 0)) AS last_saved_at,
  CONCAT('Journal entry (', x.tag, ') on D-', d.n, ' slot ', e.n) AS entry_text,
  CONCAT(x.tag, ' D', LPAD(d.n,2,'0'), ' E', e.n) AS entry_title,
  CASE x.tag
    -- ALICE: 5:45%, 4:35%, 3:15%, 2:4%, 1:1%
    WHEN 'ALICE' THEN CASE
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|ALICE')) % 100) < 45 THEN 5
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|ALICE')) % 100) < 80 THEN 4
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|ALICE')) % 100) < 95 THEN 3
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|ALICE')) % 100) < 99 THEN 2
      ELSE 1 END
    -- BOB: 5:20%, 4:30%, 3:20%, 2:20%, 1:10%
    WHEN 'BOB' THEN CASE
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|BOB')) % 100) < 20 THEN 5
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|BOB')) % 100) < 50 THEN 4
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|BOB')) % 100) < 70 THEN 3
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|BOB')) % 100) < 90 THEN 2
      ELSE 1 END
    -- CAROL: 1:45%, 2:35%, 3:15%, 4:4%, 5:1%
    WHEN 'CAROL' THEN CASE
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|CAROL')) % 100) < 45 THEN 1
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|CAROL')) % 100) < 80 THEN 2
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|CAROL')) % 100) < 95 THEN 3
      WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|CAROL')) % 100) < 99 THEN 4
      ELSE 5 END
  END AS mood,
  x.id AS user_id
FROM
  /* seq_15: 0..14 */
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
   UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
   UNION ALL SELECT 13 UNION ALL SELECT 14) AS d
CROSS JOIN
  /* seq_3: 1..3 */
  (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3) AS e
JOIN
  /* u: the three 15-day users */
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
;

-- =========================================================
-- JOURNAL ENTRIES: DAVID today (5 entries, slightly positive)
-- Times: 09:00, 12:00, 15:00, 18:00, 21:00
-- Dist: 5:30%, 4:30%, 3:20%, 2:15%, 1:5%
-- =========================================================
INSERT INTO journal_entries
(id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(CURDATE(), MAKETIME(
    CASE s.n WHEN 1 THEN 9 WHEN 2 THEN 12 WHEN 3 THEN 15 WHEN 4 THEN 18 ELSE 21 END, 0, 0
  )),
  TIMESTAMP(CURDATE(), MAKETIME(
    CASE s.n WHEN 1 THEN 9 WHEN 2 THEN 12 WHEN 3 THEN 15 WHEN 4 THEN 18 ELSE 21 END, 10, 0
  )),
  CONCAT('David journal entry #', s.n, ' (today)'),
  CONCAT('DAVID TODAY E', s.n),
  CASE
    WHEN (CRC32(CONCAT(u.id,'|TODAY|',s.n)) % 100) < 30 THEN 5
    WHEN (CRC32(CONCAT(u.id,'|TODAY|',s.n)) % 100) < 60 THEN 4
    WHEN (CRC32(CONCAT(u.id,'|TODAY|',s.n)) % 100) < 80 THEN 3
    WHEN (CRC32(CONCAT(u.id,'|TODAY|',s.n)) % 100) < 95 THEN 2
    ELSE 1
  END AS mood,
  u.id
FROM
  (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) AS s
CROSS JOIN
  (SELECT id FROM journal_users WHERE email='david.wilson@email.com') AS u
;

-- =========================================================
-- HABITS (biased to profiles)
-- - ALICE/BOB/CAROL: last 15 days at 22:00
-- - DAVID: today 22:30
-- - EMMA: today 21:30
-- =========================================================

-- 15 days for ALICE/BOB/CAROL
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY), MAKETIME(22,0,0)),
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY), MAKETIME(22,15,0)),
  CASE x.tag
    WHEN 'ALICE' THEN 7 + (CRC32(CONCAT(x.id,'|sleep|',d.n)) % 3)   -- 7..9
    WHEN 'BOB'   THEN 6 + (CRC32(CONCAT(x.id,'|sleep|',d.n)) % 3)   -- 6..8
    WHEN 'CAROL' THEN 5 + (CRC32(CONCAT(x.id,'|sleep|',d.n)) % 2)   -- 5..6
  END AS sleep,
  CASE x.tag
    WHEN 'ALICE' THEN 7 + (CRC32(CONCAT(x.id,'|water|',d.n)) % 4)   -- 7..10
    WHEN 'BOB'   THEN 6 + (CRC32(CONCAT(x.id,'|water|',d.n)) % 3)   -- 6..8
    WHEN 'CAROL' THEN 4 + (CRC32(CONCAT(x.id,'|water|',d.n)) % 3)   -- 4..6
  END AS water,
  CASE x.tag
    WHEN 'ALICE' THEN 7 + (CRC32(CONCAT(x.id,'|work|',d.n)) % 2)    -- 7..8
    WHEN 'BOB'   THEN 7 + (CRC32(CONCAT(x.id,'|work|',d.n)) % 3)    -- 7..9
    WHEN 'CAROL' THEN 9 + (CRC32(CONCAT(x.id,'|work|',d.n)) % 3)    -- 9..11
  END AS work_hours,
  x.id
FROM
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
   UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
   UNION ALL SELECT 13 UNION ALL SELECT 14) AS d
JOIN
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
;

-- DAVID today
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(CURDATE(), MAKETIME(22,30,0)),
  TIMESTAMP(CURDATE(), MAKETIME(22,45,0)),
  7, 7, 8,
  id
FROM journal_users WHERE email='david.wilson@email.com'
;

-- EMMA today
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(CURDATE(), MAKETIME(21,30,0)),
  TIMESTAMP(CURDATE(), MAKETIME(21,40,0)),
  8, 6, 7,
  id
FROM journal_users WHERE email='emma.brown@email.com'
;

-- =========================================================
-- ENTRY_EMOTIONS: 1 per journal entry, aligned to mood bucket
-- (scalar subquery picks emotion_id based on CRC32)
-- =========================================================
INSERT INTO entry_emotions (entry_id, emotion_id)
SELECT
  je.id,
  (
    SELECT em.id
    FROM emotions em
    WHERE em.emotion_label =
      CASE je.mood
        WHEN 5 THEN CASE (CRC32(CONCAT(je.id,'|emo')) % 3)
                      WHEN 0 THEN 'happy'
                      WHEN 1 THEN 'surprised'
                      ELSE 'curious' END
        WHEN 4 THEN CASE (CRC32(CONCAT(je.id,'|emo')) % 2)
                      WHEN 0 THEN 'happy'
                      ELSE 'curious' END
        WHEN 3 THEN CASE (CRC32(CONCAT(je.id,'|emo')) % 2)
                      WHEN 0 THEN 'neutral'
                      ELSE 'confused' END
        WHEN 2 THEN CASE (CRC32(CONCAT(je.id,'|emo')) % 3)
                      WHEN 0 THEN 'sad'
                      WHEN 1 THEN 'anxious'
                      ELSE 'confused' END
        ELSE            CASE (CRC32(CONCAT(je.id,'|emo')) % 3)
                      WHEN 0 THEN 'angry'
                      WHEN 1 THEN 'sad'
                      ELSE 'anxious' END
      END
    LIMIT 1
  )
FROM journal_entries je
JOIN journal_users ju ON ju.id = je.user_id
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com',
  'david.wilson@email.com'
)
;

SET FOREIGN_KEY_CHECKS = 1;
