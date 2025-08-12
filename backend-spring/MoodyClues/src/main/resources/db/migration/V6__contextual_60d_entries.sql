-- V6__contextual_60d_entries.sql
-- Contextual mock data for last 60 days (Alice positive, Bob balanced, Carol struggling)
-- Inserts for: journal_entries (3/day), habits_entries (1/day), entry_emotions (1 per entry)
-- Notes:
--  * Uses deterministic CRC32 so values are stable across runs
--  * Weekends slightly boost moods/sleep and reduce work hours
--  * Water uses realistic litres; sleep/work in hours

SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------
-- Resolve 3 users (ALICE/BOB/CAROL)
-- -----------------------------
-- We build a small inline table (x) with {tag,id}
-- NOTE: If any email is missing, those rows will just not insert.
-- -----------------------------

-- -----------------------------
-- 0) Clean previous last-60-day data for these users (to avoid duplicates)
-- -----------------------------
-- Window: [CURDATE() - 59 days .. CURDATE()] inclusive
-- Delete entry_emotions first (FK), then journal_entries, then habits_entries

DELETE ee
FROM entry_emotions ee
JOIN journal_entries je ON ee.entry_id = je.id
JOIN journal_users ju   ON ju.id = je.user_id
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com'
)
AND je.created_at >= TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 59 DAY), '00:00:00')
AND je.created_at <  TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '00:00:00')
;

DELETE je
FROM journal_entries je
JOIN journal_users ju ON ju.id = je.user_id
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com'
)
AND je.created_at >= TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 59 DAY), '00:00:00')
AND je.created_at <  TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '00:00:00')
;

DELETE he
FROM habits_entries he
JOIN journal_users ju ON ju.id = he.user_id
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com'
)
AND he.created_at >= TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 59 DAY), '00:00:00')
AND he.created_at <  TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '00:00:00')
;

-- -----------------------------
-- 1) JOURNAL ENTRIES: 60 days x 3/day (08:00, 13:00, 20:00)
-- Mood distributions (weekday vs weekend):
--   ALICE weekday: 5:40,4:35,3:20,2:4,1:1 | weekend: 5:55,4:30,3:12,2:2,1:1
--   BOB   weekday: 5:20,4:30,3:30,2:15,1:5 | weekend: 5:25,4:30,3:25,2:15,1:5
--   CAROL weekday: 1:40,2:35,3:18,4:5,5:2 | weekend: 1:30,2:35,3:25,4:7,5:3
-- -----------------------------
INSERT INTO journal_entries
(id, archived, created_at, last_saved_at, entry_text, entry_title, mood, user_id)
SELECT
  UUID() AS id,
  b'0'   AS archived,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY),
            MAKETIME(CASE e.n WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 0, 0)) AS created_at,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY),
            MAKETIME(CASE e.n WHEN 1 THEN 8 WHEN 2 THEN 13 ELSE 20 END, 15, 0)) AS last_saved_at,
  CONCAT(
    CASE e.n WHEN 1 THEN 'Morning reflection'
             WHEN 2 THEN 'Midday check-in'
             ELSE 'Evening recap' END,
    ' (', x.tag, '), D-', d.n
  ) AS entry_text,
  CONCAT(x.tag, ' D', LPAD(d.n,2,'0'), ' T', e.n) AS entry_title,
  CASE x.tag
    WHEN 'ALICE' THEN
      CASE
        WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7)
        THEN -- weekend
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|W')) % 100) < 55 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|W')) % 100) < 85 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|W')) % 100) < 97 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|W')) % 100) < 99 THEN 2
            ELSE 1 END
        ELSE -- weekday
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|WD')) % 100) < 40 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|WD')) % 100) < 75 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|WD')) % 100) < 95 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|A|WD')) % 100) < 99 THEN 2
            ELSE 1 END
      END
    WHEN 'BOB' THEN
      CASE
        WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7)
        THEN -- weekend
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|W')) % 100) < 25 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|W')) % 100) < 55 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|W')) % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|W')) % 100) < 95 THEN 2
            ELSE 1 END
        ELSE -- weekday
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|WD')) % 100) < 20 THEN 5
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|WD')) % 100) < 50 THEN 4
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|WD')) % 100) < 80 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|B|WD')) % 100) < 95 THEN 2
            ELSE 1 END
      END
    WHEN 'CAROL' THEN
      CASE
        WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7)
        THEN -- weekend
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|W')) % 100) < 30 THEN 1
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|W')) % 100) < 65 THEN 2
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|W')) % 100) < 90 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|W')) % 100) < 97 THEN 4
            ELSE 5 END
        ELSE -- weekday
          CASE
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|WD')) % 100) < 40 THEN 1
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|WD')) % 100) < 75 THEN 2
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|WD')) % 100) < 93 THEN 3
            WHEN (CRC32(CONCAT(x.id,'|',d.n,'|',e.n,'|C|WD')) % 100) < 98 THEN 4
            ELSE 5 END
      END
  END AS mood,
  x.id AS user_id
FROM
  /* seq_60: 0..59 */
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
   UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
   UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16
   UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
   UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24
   UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28
   UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32
   UNION ALL SELECT 33 UNION ALL SELECT 34 UNION ALL SELECT 35 UNION ALL SELECT 36
   UNION ALL SELECT 37 UNION ALL SELECT 38 UNION ALL SELECT 39 UNION ALL SELECT 40
   UNION ALL SELECT 41 UNION ALL SELECT 42 UNION ALL SELECT 43 UNION ALL SELECT 44
   UNION ALL SELECT 45 UNION ALL SELECT 46 UNION ALL SELECT 47 UNION ALL SELECT 48
   UNION ALL SELECT 49 UNION ALL SELECT 50 UNION ALL SELECT 51 UNION ALL SELECT 52
   UNION ALL SELECT 53 UNION ALL SELECT 54 UNION ALL SELECT 55 UNION ALL SELECT 56
   UNION ALL SELECT 57 UNION ALL SELECT 58 UNION ALL SELECT 59) AS d
CROSS JOIN
  /* timeslots: 1..3 */
  (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3) AS e
JOIN
  /* users */
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
;

-- -----------------------------
-- 2) HABITS ENTRIES: 60 days x 1/day at 22:00
-- Weekday vs weekend adjustments:
--   Sleep (hrs): ALICE 7.0–9.0 (wknd +0.5), BOB 6.0–8.5 (wknd +0.5), CAROL 5.0–7.5 (wknd +0.5)
--   Water (L)  : ALICE 2.2–3.2, BOB 1.8–2.6, CAROL 1.2–2.0  (wknd +0.2 bump)
--   Work (hrs) : Weekday ALICE 7–8.5, BOB 7–9.5, CAROL 9–11 | Weekend ALICE 1–3, BOB 2–4, CAROL 4–6
-- -----------------------------
INSERT INTO habits_entries
(id, archived, created_at, last_saved_at, sleep, water, work_hours, user_id)
SELECT
  UUID(), b'0',
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY), MAKETIME(22,0,0)) AS created_at,
  TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL d.n DAY), MAKETIME(22,10,0)) AS last_saved_at,
  -- SLEEP (hrs, 1 decimal, weekend +0.5)
  ROUND(
    CASE x.tag
      WHEN 'ALICE' THEN 7.0
      WHEN 'BOB'   THEN 6.0
      ELSE              5.0
    END
    + CASE WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7) THEN 0.5 ELSE 0 END
    + ((CRC32(CONCAT(x.id,'|sleep|',d.n)) % 16) / 10.0) -- +0.0..+1.5
  , 1) AS sleep,
  -- WATER (L, 1 decimal, weekend +0.2)
  ROUND(
    CASE x.tag
      WHEN 'ALICE' THEN 2.2
      WHEN 'BOB'   THEN 1.8
      ELSE              1.2
    END
    + CASE WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7) THEN 0.2 ELSE 0 END
    + ((CRC32(CONCAT(x.id,'|water|',d.n)) % 11) / 10.0) -- +0.0..+1.0
  , 1) AS water,
  -- WORK HOURS
  ROUND(
    CASE
      WHEN DAYOFWEEK(DATE_SUB(CURDATE(), INTERVAL d.n DAY)) IN (1,7) THEN
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
  /* seq_60: 0..59 */
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
   UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
   UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16
   UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
   UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24
   UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28
   UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32
   UNION ALL SELECT 33 UNION ALL SELECT 34 UNION ALL SELECT 35 UNION ALL SELECT 36
   UNION ALL SELECT 37 UNION ALL SELECT 38 UNION ALL SELECT 39 UNION ALL SELECT 40
   UNION ALL SELECT 41 UNION ALL SELECT 42 UNION ALL SELECT 43 UNION ALL SELECT 44
   UNION ALL SELECT 45 UNION ALL SELECT 46 UNION ALL SELECT 47 UNION ALL SELECT 48
   UNION ALL SELECT 49 UNION ALL SELECT 50 UNION ALL SELECT 51 UNION ALL SELECT 52
   UNION ALL SELECT 53 UNION ALL SELECT 54 UNION ALL SELECT 55 UNION ALL SELECT 56
   UNION ALL SELECT 57 UNION ALL SELECT 58 UNION ALL SELECT 59) AS d
JOIN
  (
    SELECT 'ALICE' AS tag, id FROM journal_users WHERE email='alice.johnson@email.com'
    UNION ALL
    SELECT 'BOB'   AS tag, id FROM journal_users WHERE email='bob.smith@email.com'
    UNION ALL
    SELECT 'CAROL' AS tag, id FROM journal_users WHERE email='carol.davis@email.com'
  ) AS x
;

-- -----------------------------
-- 3) ENTRY_EMOTIONS: one per journal entry, mood-aligned (same mapping style as V3)
-- -----------------------------
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
WHERE ju.email IN (
  'alice.johnson@email.com',
  'bob.smith@email.com',
  'carol.davis@email.com'
)
AND je.created_at >= TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 59 DAY), '00:00:00')
AND je.created_at <  TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '00:00:00')
;

SET FOREIGN_KEY_CHECKS = 1;
