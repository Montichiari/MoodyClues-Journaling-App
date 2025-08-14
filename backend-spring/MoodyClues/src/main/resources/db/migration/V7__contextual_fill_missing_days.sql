-- V7__contextual_fill_missing_days.sql
-- Fill only missing days (including today) for ALICE / BOB / CAROL
-- Adds 3 contextual journal entries per missing day and 1 habit entry per missing day
-- Also backfills entry_emotions for entries without an emotion row

SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------
-- Helper: generate up to 400 days of offsets
-- (enough headroom; we clamp by start_day..today)
-- ------------------------------------------
WITH RECURSIVE nums(n) AS (
  SELECT 0
  UNION ALL
  SELECT n + 1 FROM nums WHERE n < 400
),
-- ------------------------------------------
-- Resolve target users
-- ------------------------------------------
users AS (
  SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
  UNION ALL
  SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
  UNION ALL
  SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
),
-- ------------------------------------------
-- JOURNAL start day per user:
--   day after the latest existing journal entry date;
--   falls back to CURDATE() if none exist (so only today)
-- ------------------------------------------
j_start AS (
  SELECT
    u.tag,
    u.id,
    COALESCE(DATE_ADD(DATE(MAX(je.created_at)), INTERVAL 1 DAY), CURDATE()) AS start_day
  FROM users u
  LEFT JOIN journal_entries je ON je.user_id = u.id
  GROUP BY u.tag, u.id
),
-- ------------------------------------------
-- HABITS start day per user:
--   day after the latest existing habits entry date;
--   falls back to CURDATE() if none exist
-- ------------------------------------------
h_start AS (
  SELECT
    u.tag,
    u.id,
    COALESCE(DATE_ADD(DATE(MAX(he.created_at)), INTERVAL 1 DAY), CURDATE()) AS start_day
  FROM users u
  LEFT JOIN habits_entries he ON he.user_id = u.id
  GROUP BY u.tag, u.id
)

-- ============================
-- 1) JOURNAL ENTRIES (fill)
-- ============================
INSERT INTO journal_entries
(id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id)
SELECT
  UUID() AS id,
  b'0'   AS archived,
  TIMESTAMP(d.the_day,
            MAKETIME(CASE t.slot WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 0, 0)) AS created_at,
  TIMESTAMP(d.the_day,
            MAKETIME(CASE t.slot WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 15, 0)) AS last_saved_at,
  CONCAT(
    CASE t.slot WHEN 1 THEN 'Morning reflection'
                WHEN 2 THEN 'Midday check-in'
                ELSE 'Evening recap' END,
    ' (', u.tag, '), ', DATE_FORMAT(d.the_day, '%Y-%m-%d')
  ) AS entry_text,
  CONCAT(u.tag, ' ', DATE_FORMAT(d.the_day, 'D%Y%m%d'), ' T', t.slot) AS entry_title,
  CASE u.tag
    WHEN 'ALICE' THEN
      CASE
        WHEN DAYOFWEEK(d.the_day) IN (1,7) -- weekend
          THEN CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|W'))  % 100) < 55 THEN 5
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|W'))  % 100) < 85 THEN 4
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|W'))  % 100) < 97 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|W'))  % 100) < 99 THEN 2
            ELSE 1 END
        ELSE -- weekday
          CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|WD')) % 100) < 40 THEN 5
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|WD')) % 100) < 75 THEN 4
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|WD')) % 100) < 95 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|A|WD')) % 100) < 99 THEN 2
            ELSE 1 END
      END
    WHEN 'BOB' THEN
      CASE
        WHEN DAYOFWEEK(d.the_day) IN (1,7)
          THEN CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|W'))  % 100) < 25 THEN 5
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|W'))  % 100) < 55 THEN 4
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|W'))  % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|W'))  % 100) < 95 THEN 2
            ELSE 1 END
        ELSE
          CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|WD')) % 100) < 20 THEN 5
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|WD')) % 100) < 50 THEN 4
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|WD')) % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|B|WD')) % 100) < 95 THEN 2
            ELSE 1 END
      END
    WHEN 'CAROL' THEN
      CASE
        WHEN DAYOFWEEK(d.the_day) IN (1,7)
          THEN CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|W'))  % 100) < 30 THEN 1
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|W'))  % 100) < 65 THEN 2
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|W'))  % 100) < 90 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|W'))  % 100) < 97 THEN 4
            ELSE 5 END
        ELSE
          CASE
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|WD')) % 100) < 40 THEN 1
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|WD')) % 100) < 75 THEN 2
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|WD')) % 100) < 93 THEN 3
            WHEN (CRC32(CONCAT(u.id,'|',d.the_day,'|',t.slot,'|C|WD')) % 100) < 98 THEN 4
            ELSE 5 END
      END
  END AS mood,
  u.id AS user_id
FROM j_start js
JOIN users u ON u.id = js.id
JOIN nums n
JOIN (SELECT 1 AS slot UNION ALL SELECT 2 UNION ALL SELECT 3) AS t
CROSS JOIN LATERAL (SELECT DATE_ADD(js.start_day, INTERVAL n.n DAY) AS the_day) d
WHERE d.the_day <= CURDATE()
  -- Ensure we only fill whole-missing days (no any journal entry exists that day)
  AND NOT EXISTS (
    SELECT 1 FROM journal_entries je2
    WHERE je2.user_id = u.id AND DATE(je2.created_at) = d.the_day
  )
;

-- ============================
-- 2) HABITS ENTRIES (fill)
-- ============================
WITH RECURSIVE nums(n) AS (
  SELECT 0
  UNION ALL
  SELECT n + 1 FROM nums WHERE n < 400
),
users AS (
  SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
  UNION ALL
  SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
  UNION ALL
  SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
),
h_start AS (
  SELECT
    u.tag,
    u.id,
    COALESCE(DATE_ADD(DATE(MAX(he.created_at)), INTERVAL 1 DAY), CURDATE()) AS start_day
  FROM users u
  LEFT JOIN habits_entries he ON he.user_id = u.id
  GROUP BY u.tag, u.id
)
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(d.the_day, MAKETIME(22,0,0)) AS created_at,
  TIMESTAMP(d.the_day, MAKETIME(22,10,0)) AS last_saved_at,
  -- Sleep (hrs, 1dp), weekend +0.5
  ROUND(
    CASE u.tag WHEN 'ALICE' THEN 7.0 WHEN 'BOB' THEN 6.0 ELSE 5.0 END
    + CASE WHEN DAYOFWEEK(d.the_day) IN (1,7) THEN 0.5 ELSE 0 END
    + ((CRC32(CONCAT(u.id,'|sleep|',d.the_day)) % 16) / 10.0)  -- +0.0..+1.5
  , 1) AS sleep,
  -- Water (L, 1dp), weekend +0.2
  ROUND(
    CASE u.tag WHEN 'ALICE' THEN 2.2 WHEN 'BOB' THEN 1.8 ELSE 1.2 END
    + CASE WHEN DAYOFWEEK(d.the_day) IN (1,7) THEN 0.2 ELSE 0 END
    + ((CRC32(CONCAT(u.id,'|water|',d.the_day)) % 11) / 10.0)  -- +0.0..+1.0
  , 1) AS water,
  -- Work hours (weekday vs weekend)
  ROUND(
    CASE
      WHEN DAYOFWEEK(d.the_day) IN (1,7) THEN
        CASE u.tag
          WHEN 'ALICE' THEN 1.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 21) / 10.0) -- 1.0..3.0
          WHEN 'BOB'   THEN 2.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 21) / 10.0) -- 2.0..4.0
          ELSE               4.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 21) / 10.0) -- 4.0..6.0
        END
      ELSE
        CASE u.tag
          WHEN 'ALICE' THEN 7.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 16) / 10.0) -- 7.0..8.5
          WHEN 'BOB'   THEN 7.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 26) / 10.0) -- 7.0..9.5
          ELSE               9.0 + ((CRC32(CONCAT(u.id,'|work|',d.the_day)) % 21) / 10.0) -- 9.0..11.0
        END
    END
  , 1) AS work_hours,
  u.id
FROM h_start hs
JOIN users u ON u.id = hs.id
JOIN nums n
CROSS JOIN LATERAL (SELECT DATE_ADD(hs.start_day, INTERVAL n.n DAY) AS the_day) d
WHERE d.the_day <= CURDATE()
  -- Exactly one habit per day: skip if any exists for that day
  AND NOT EXISTS (
    SELECT 1 FROM habits_entries he2
    WHERE he2.user_id = u.id AND DATE(he2.created_at) = d.the_day
  )
;

-- ============================
-- 3) ENTRY_EMOTIONS backfill
--    Only for entries missing a mapping
-- ============================
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
        ELSE       CASE (CRC32(CONCAT(je.id,'|emo')) % 3)
                      WHEN 0 THEN 'angry'
                      WHEN 1 THEN 'sad'
                      ELSE 'anxious' END
      END
    LIMIT 1
  )
FROM journal_entries je
JOIN journal_users ju ON ju.id = je.user_id
LEFT JOIN entry_emotions ee ON ee.entry_id = je.id
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com'
)
AND ee.entry_id IS NULL
;

SET FOREIGN_KEY_CHECKS = 1;
