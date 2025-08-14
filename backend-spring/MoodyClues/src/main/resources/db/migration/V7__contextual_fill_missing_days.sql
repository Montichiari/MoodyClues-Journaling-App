-- V7__contextual_fill_missing_days.sql (MySQL-safe: no CTEs, no LATERAL)
-- Fills only the missing days (incl. today) for ALICE / BOB / CAROL
-- journal_entries: 3 per missing day (08:00, 13:00, 20:00)
-- habits_entries : 1 per missing day (22:00)
-- entry_emotions : backfill only for journal entries that have no emotion row

SET FOREIGN_KEY_CHECKS = 0;

-- Small helpers: inline number tables
-- seq_0_9  : 0..9
-- seq_0_399: 0..399  (enough headroom)
-- timeslots: 1..3
-- users    : resolve 3 users by email
-- NOTE: If a user email is missing, that row simply won't insert anything.

-- ============================
-- 1) JOURNAL ENTRIES (fill)
-- ============================
INSERT INTO journal_entries
(id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id)
SELECT
  UUID() AS id,
  b'0'   AS archived,
  TIMESTAMP(
    -- target_day
    DATE_ADD(
      COALESCE(
        DATE_ADD(DATE( (SELECT MAX(je1.created_at) FROM journal_entries je1 WHERE je1.user_id = x.id) ), INTERVAL 1 DAY),
        CURDATE()
      ),
      INTERVAL d.n DAY
    ),
    MAKETIME(CASE e.slot WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 0, 0)
  ) AS created_at,
  TIMESTAMP(
    DATE_ADD(
      COALESCE(
        DATE_ADD(DATE( (SELECT MAX(je2.created_at) FROM journal_entries je2 WHERE je2.user_id = x.id) ), INTERVAL 1 DAY),
        CURDATE()
      ),
      INTERVAL d.n DAY
    ),
    MAKETIME(CASE e.slot WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 15, 0)
  ) AS last_saved_at,
  CONCAT(
    CASE e.slot WHEN 1 THEN 'Morning reflection'
                WHEN 2 THEN 'Midday check-in'
                ELSE 'Evening recap' END,
    ' (', x.tag, '), ',
    DATE_FORMAT(
      DATE_ADD(
        COALESCE(
          DATE_ADD(DATE( (SELECT MAX(je3.created_at) FROM journal_entries je3 WHERE je3.user_id = x.id) ), INTERVAL 1 DAY),
          CURDATE()
        ),
        INTERVAL d.n DAY
      ),
      '%Y-%m-%d'
    )
  ) AS entry_text,
  CONCAT(x.tag, ' ', DATE_FORMAT(
           DATE_ADD(
             COALESCE(
               DATE_ADD(DATE( (SELECT MAX(je4.created_at) FROM journal_entries je4 WHERE je4.user_id = x.id) ), INTERVAL 1 DAY),
               CURDATE()
             ),
             INTERVAL d.n DAY
           ), 'D%Y%m%d'
         ), ' T', e.slot) AS entry_title,
  CASE x.tag
    WHEN 'ALICE' THEN
      CASE
        WHEN DAYOFWEEK(
          DATE_ADD(
            COALESCE(
              DATE_ADD(DATE( (SELECT MAX(je5.created_at) FROM journal_entries je5 WHERE je5.user_id = x.id) ), INTERVAL 1 DAY),
              CURDATE()
            ),
            INTERVAL d.n DAY
          )
        ) IN (1,7) THEN
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|W'))  % 100) < 55 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|W'))  % 100) < 85 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|W'))  % 100) < 97 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|W'))  % 100) < 99 THEN 2
            ELSE 1 END
        ELSE
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|WD')) % 100) < 40 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|WD')) % 100) < 75 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|WD')) % 100) < 95 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|A|WD')) % 100) < 99 THEN 2
            ELSE 1 END
      END
    WHEN 'BOB' THEN
      CASE
        WHEN DAYOFWEEK(
          DATE_ADD(
            COALESCE(
              DATE_ADD(DATE( (SELECT MAX(je6.created_at) FROM journal_entries je6 WHERE je6.user_id = x.id) ), INTERVAL 1 DAY),
              CURDATE()
            ),
            INTERVAL d.n DAY
          )
        ) IN (1,7) THEN
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|W'))  % 100) < 25 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|W'))  % 100) < 55 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|W'))  % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|W'))  % 100) < 95 THEN 2
            ELSE 1 END
        ELSE
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|WD')) % 100) < 20 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|WD')) % 100) < 50 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|WD')) % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|B|WD')) % 100) < 95 THEN 2
            ELSE 1 END
      END
    WHEN 'CAROL' THEN
      CASE
        WHEN DAYOFWEEK(
          DATE_ADD(
            COALESCE(
              DATE_ADD(DATE( (SELECT MAX(je7.created_at) FROM journal_entries je7 WHERE je7.user_id = x.id) ), INTERVAL 1 DAY),
              CURDATE()
            ),
            INTERVAL d.n DAY
          )
        ) IN (1,7) THEN
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|W'))  % 100) < 30 THEN 1
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|W'))  % 100) < 65 THEN 2
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|W'))  % 100) < 90 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|W'))  % 100) < 97 THEN 4
            ELSE 5 END
        ELSE
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|WD')) % 100) < 40 THEN 1
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|WD')) % 100) < 75 THEN 2
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|WD')) % 100) < 93 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.slot,'|C|WD')) % 100) < 98 THEN 4
            ELSE 5 END
      END
  END AS mood,
  x.id AS user_id
FROM
  -- users x(tag,id)
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
  -- seq_0_399 as d.n
  JOIN (
    SELECT (a.n + b.n*10 + c.n*100) AS n
    FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
    CROSS JOIN (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
    CROSS JOIN (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) c
  ) AS d
  -- timeslots 1..3
  JOIN (SELECT 1 AS slot UNION ALL SELECT 2 UNION ALL SELECT 3) AS e
WHERE
  -- clamp to today
  DATE_ADD(
    COALESCE(
      DATE_ADD(DATE( (SELECT MAX(je8.created_at) FROM journal_entries je8 WHERE je8.user_id = x.id) ), INTERVAL 1 DAY),
      CURDATE()
    ),
    INTERVAL d.n DAY
  ) <= CURDATE()
  -- ensure the day is wholly missing for this user
  AND NOT EXISTS (
    SELECT 1
    FROM journal_entries je9
    WHERE je9.user_id = x.id
      AND DATE(je9.created_at) = DATE_ADD(
            COALESCE(
              DATE_ADD(DATE( (SELECT MAX(je10.created_at) FROM journal_entries je10 WHERE je10.user_id = x.id) ), INTERVAL 1 DAY),
              CURDATE()
            ),
            INTERVAL d.n DAY
          )
  )
;

-- ============================
-- 2) HABITS ENTRIES (fill)
-- ============================
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(
    DATE_ADD(
      COALESCE(
        DATE_ADD(DATE( (SELECT MAX(he1.created_at) FROM habits_entries he1 WHERE he1.user_id = x.id) ), INTERVAL 1 DAY),
        CURDATE()
      ),
      INTERVAL d.n DAY
    ), MAKETIME(22,0,0)
  ) AS created_at,
  TIMESTAMP(
    DATE_ADD(
      COALESCE(
        DATE_ADD(DATE( (SELECT MAX(he2.created_at) FROM habits_entries he2 WHERE he2.user_id = x.id) ), INTERVAL 1 DAY),
        CURDATE()
      ),
      INTERVAL d.n DAY
    ), MAKETIME(22,10,0)
  ) AS last_saved_at,
  -- Sleep (hrs, 1dp), weekend +0.5
  ROUND(
    CASE x.tag WHEN 'ALICE' THEN 7.0 WHEN 'BOB' THEN 6.0 ELSE 5.0 END
    + CASE WHEN DAYOFWEEK(
              DATE_ADD(
                COALESCE(
                  DATE_ADD(DATE( (SELECT MAX(he3.created_at) FROM habits_entries he3 WHERE he3.user_id = x.id) ), INTERVAL 1 DAY),
                  CURDATE()
                ),
                INTERVAL d.n DAY
              )
            ) IN (1,7) THEN 0.5 ELSE 0 END
    + ((CRC32(CONCAT(x.id,'|sleep|',d.n)) % 16) / 10.0)
  , 1) AS sleep,
  -- Water (L, 1dp), weekend +0.2
  ROUND(
    CASE x.tag WHEN 'ALICE' THEN 2.2 WHEN 'BOB' THEN 1.8 ELSE 1.2 END
    + CASE WHEN DAYOFWEEK(
              DATE_ADD(
                COALESCE(
                  DATE_ADD(DATE( (SELECT MAX(he4.created_at) FROM habits_entries he4 WHERE he4.user_id = x.id) ), INTERVAL 1 DAY),
                  CURDATE()
                ),
                INTERVAL d.n DAY
              )
            ) IN (1,7) THEN 0.2 ELSE 0 END
    + ((CRC32(CONCAT(x.id,'|water|',d.n)) % 11) / 10.0)
  , 1) AS water,
  -- Work hours
  ROUND(
    CASE
      WHEN DAYOFWEEK(
        DATE_ADD(
          COALESCE(
            DATE_ADD(DATE( (SELECT MAX(he5.created_at) FROM habits_entries he5 WHERE he5.user_id = x.id) ), INTERVAL 1 DAY),
            CURDATE()
          ),
          INTERVAL d.n DAY
        )
      ) IN (1,7) THEN
        CASE x.tag
          WHEN 'ALICE' THEN 1.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 21) / 10.0) -- 1.0..3.0
          WHEN 'BOB'   THEN 2.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 21) / 10.0) -- 2.0..4.0
          ELSE               4.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 21) / 10.0) -- 4.0..6.0
        END
      ELSE
        CASE x.tag
          WHEN 'ALICE' THEN 7.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 16) / 10.0) -- 7.0..8.5
          WHEN 'BOB'   THEN 7.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 26) / 10.0) -- 7.0..9.5
          ELSE               9.0 + ((CRC32(CONCAT(x.id,'|work|',d.n)) % 21) / 10.0) -- 9.0..11.0
        END
    END
  , 1) AS work_hours,
  x.id AS user_id
FROM
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
  JOIN (
    SELECT (a.n + b.n*10 + c.n*100) AS n
    FROM (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
    CROSS JOIN (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
    CROSS JOIN (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) c
  ) AS d
WHERE
  DATE_ADD(
    COALESCE(
      DATE_ADD(DATE( (SELECT MAX(he6.created_at) FROM habits_entries he6 WHERE he6.user_id = x.id) ), INTERVAL 1 DAY),
      CURDATE()
    ),
    INTERVAL d.n DAY
  ) <= CURDATE()
  -- exactly 1 habit per day: skip if any exists that day
  AND NOT EXISTS (
    SELECT 1
    FROM habits_entries he7
    WHERE he7.user_id = x.id
      AND DATE(he7.created_at) = DATE_ADD(
            COALESCE(
              DATE_ADD(DATE( (SELECT MAX(he8.created_at) FROM habits_entries he8 WHERE he8.user_id = x.id) ), INTERVAL 1 DAY),
              CURDATE()
            ),
            INTERVAL d.n DAY
          )
  )
;

-- ============================
-- 3) ENTRY_EMOTIONS backfill
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
