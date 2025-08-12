-- V4__contextualize_entries_and_dual_emotions.sql
-- Alters existing seeded journal_entries to have realistic titles/text
-- and adds a second emotion to ~50% of entries (max 2 per entry).

SET FOREIGN_KEY_CHECKS = 0;

-- =========================================
-- 1) CONTEXTUALIZE TITLES AND TEXT
-- Targets only entries created by V3 seeds:
--    - entry_text LIKE 'Journal entry (%'
--    - OR entry_title like the ' Dxx Ex' tokens used previously
-- =========================================
UPDATE journal_entries je
SET
  je.entry_title =
    CASE
      WHEN HOUR(je.created_at) BETWEEN 5 AND 10 THEN
        CONCAT('Morning check-in - ',
               CASE je.mood
                 WHEN 5 THEN 'feeling great'
                 WHEN 4 THEN 'feeling good'
                 WHEN 3 THEN 'feeling steady'
                 WHEN 2 THEN 'a bit off'
                 ELSE 'having a tough day'
               END)
      WHEN HOUR(je.created_at) BETWEEN 11 AND 14 THEN
        CONCAT('Midday reflection - ',
               CASE je.mood
                 WHEN 5 THEN 'feeling great'
                 WHEN 4 THEN 'feeling good'
                 WHEN 3 THEN 'feeling steady'
                 WHEN 2 THEN 'a bit off'
                 ELSE 'having a tough day'
               END)
      WHEN HOUR(je.created_at) BETWEEN 15 AND 18 THEN
        CONCAT('Afternoon notes - ',
               CASE je.mood
                 WHEN 5 THEN 'feeling great'
                 WHEN 4 THEN 'feeling good'
                 WHEN 3 THEN 'feeling steady'
                 WHEN 2 THEN 'a bit off'
                 ELSE 'having a tough day'
               END)
      WHEN HOUR(je.created_at) BETWEEN 19 AND 22 THEN
        CONCAT('Evening wind-down - ',
               CASE je.mood
                 WHEN 5 THEN 'feeling great'
                 WHEN 4 THEN 'feeling good'
                 WHEN 3 THEN 'feeling steady'
                 WHEN 2 THEN 'a bit off'
                 ELSE 'having a tough day'
               END)
      ELSE
        CONCAT('Late-night debrief - ',
               CASE je.mood
                 WHEN 5 THEN 'feeling great'
                 WHEN 4 THEN 'feeling good'
                 WHEN 3 THEN 'feeling steady'
                 WHEN 2 THEN 'a bit off'
                 ELSE 'having a tough day'
               END)
    END,
  je.entry_text =
    CASE
      WHEN HOUR(je.created_at) BETWEEN 5 AND 10 THEN
        CASE (CRC32(je.id) % 3)
          WHEN 0 THEN CONCAT('Woke up ',
                             CASE je.mood
                               WHEN 5 THEN 'feeling great'
                               WHEN 4 THEN 'feeling good'
                               WHEN 3 THEN 'steady'
                               WHEN 2 THEN 'a bit off'
                               ELSE 'pretty low'
                             END,
                             '. Intention: focus on one key task and take short breaks.')
          WHEN 1 THEN CONCAT('Starting the day ',
                             CASE je.mood
                               WHEN 5 THEN 'energised'
                               WHEN 4 THEN 'optimistic'
                               WHEN 3 THEN 'even-tempered'
                               WHEN 2 THEN 'slightly tense'
                               ELSE 'drained'
                             END,
                             '. Plan: hydrate, clear top emails, and do a 30-min deep-work block.')
          ELSE CONCAT('Morning check-in, ',
                      CASE je.mood
                        WHEN 5 THEN 'confidence is high'
                        WHEN 4 THEN 'feeling capable'
                        WHEN 3 THEN 'keeping it steady'
                        WHEN 2 THEN 'taking it slow'
                        ELSE 'being gentle with myself'
                      END,
                      '. Priority: finish one meaningful task before lunch.')
        END
      WHEN HOUR(je.created_at) BETWEEN 11 AND 14 THEN
        CASE (CRC32(je.id) % 3)
          WHEN 0 THEN CONCAT('Midday update — ',
                             CASE je.mood
                               WHEN 5 THEN 'great momentum'
                               WHEN 4 THEN 'good pace'
                               WHEN 3 THEN 'steady rhythm'
                               WHEN 2 THEN 'a few bumps'
                               ELSE 'low energy'
                             END,
                             '. Best moment so far: made progress on a key item.')
          WHEN 1 THEN CONCAT('Lunch-time check-in — feeling ',
                             CASE je.mood
                               WHEN 5 THEN 'positive'
                               WHEN 4 THEN 'good'
                               WHEN 3 THEN 'okay'
                               WHEN 2 THEN 'stressed'
                               ELSE 'flat'
                             END,
                             '. Reminder: stretch, breathe, and pace for the afternoon.')
          ELSE CONCAT('Half-day reflection — ',
                      CASE je.mood
                        WHEN 5 THEN 'on track'
                        WHEN 4 THEN 'mostly on track'
                        WHEN 3 THEN 'keeping afloat'
                        WHEN 2 THEN 'a bit behind'
                        ELSE 'struggling'
                      END,
                      '. Adjusting plans based on what came up.')
        END
      WHEN HOUR(je.created_at) BETWEEN 15 AND 18 THEN
        CASE (CRC32(je.id) % 3)
          WHEN 0 THEN CONCAT('Afternoon notes — ',
                             CASE je.mood
                               WHEN 5 THEN 'solid focus'
                               WHEN 4 THEN 'decent focus'
                               WHEN 3 THEN 'manageable focus'
                               WHEN 2 THEN 'distracted'
                               ELSE 'tired'
                             END,
                             '. Aim: one small win before wrapping up.')
          WHEN 1 THEN CONCAT('Post-lunch check-in — ',
                             CASE je.mood
                               WHEN 5 THEN 'calm and clear'
                               WHEN 4 THEN 'productive enough'
                               WHEN 3 THEN 'holding steady'
                               WHEN 2 THEN 'a bit overwhelmed'
                               ELSE 'foggy'
                             END,
                             '. Staying patient and present.')
          ELSE CONCAT('Later-day focus — ',
                      CASE je.mood
                        WHEN 5 THEN 'strong'
                        WHEN 4 THEN 'good'
                        WHEN 3 THEN 'moderate'
                        WHEN 2 THEN 'fragile'
                        ELSE 'low'
                      END,
                      '. I will close loops and set up tomorrow.')
        END
      WHEN HOUR(je.created_at) BETWEEN 19 AND 22 THEN
        CASE (CRC32(je.id) % 3)
          WHEN 0 THEN CONCAT('Evening wind-down — ',
                             CASE je.mood
                               WHEN 5 THEN 'grateful'
                               WHEN 4 THEN 'content'
                               WHEN 3 THEN 'neutral'
                               WHEN 2 THEN 'tense'
                               ELSE 'worn out'
                             END,
                             '. What worked: staying consistent. One thing to improve: clearer breaks.')
          WHEN 1 THEN CONCAT('Evening wrap-up — ',
                             CASE je.mood
                               WHEN 5 THEN 'bright'
                               WHEN 4 THEN 'light'
                               WHEN 3 THEN 'stable'
                               WHEN 2 THEN 'heavy'
                               ELSE 'flat'
                             END,
                             '. Letting go of what I cannot control.')
          ELSE CONCAT('Night reflection — ',
                      CASE je.mood
                        WHEN 5 THEN 'uplifted'
                        WHEN 4 THEN 'fine'
                        WHEN 3 THEN 'steady'
                        WHEN 2 THEN 'strained'
                        ELSE 'low'
                      END,
                      '. I’ll prepare a short list for tomorrow.')
        END
      ELSE
        CASE (CRC32(je.id) % 3)
          WHEN 0 THEN CONCAT('Late-night debrief — ',
                             CASE je.mood
                               WHEN 5 THEN 'satisfied'
                               WHEN 4 THEN 'okay'
                               WHEN 3 THEN 'even'
                               WHEN 2 THEN 'restless'
                               ELSE 'spent'
                             END,
                             '. Quick summary before bed.')
          WHEN 1 THEN CONCAT('End-of-day check-in — ',
                             CASE je.mood
                               WHEN 5 THEN 'positive'
                               WHEN 4 THEN 'alright'
                               WHEN 3 THEN 'fine'
                               WHEN 2 THEN 'uneasy'
                               ELSE 'down'
                             END,
                             '. Tomorrow I will start with the hardest task.')
          ELSE CONCAT('Quiet wrap-up — ',
                      CASE je.mood
                        WHEN 5 THEN 'light'
                        WHEN 4 THEN 'calm'
                        WHEN 3 THEN 'neutral'
                        WHEN 2 THEN 'tired'
                        ELSE 'drained'
                      END,
                      '. Noting what to carry forward.')
        END
    END
WHERE
  je.entry_text LIKE 'Journal entry (%'
  OR je.entry_title REGEXP ' D[0-9]{2} E[0-9]';

-- =========================================
-- 2) ADD A SECOND EMOTION TO ~50% OF ENTRIES
-- Pairs are chosen deterministically from the first emotion,
-- inserted only when an entry currently has exactly 1 emotion,
-- and only for entries targeted above (pattern check).
-- =========================================
INSERT INTO entry_emotions (entry_id, emotion_id)
SELECT
  je.id,
  e2.id AS second_emotion_id
FROM journal_entries je
JOIN entry_emotions ee1         ON ee1.entry_id = je.id
JOIN emotions e1                ON e1.id = ee1.emotion_id
JOIN emotions e2                ON e2.emotion_label =
  CASE e1.emotion_label
    WHEN 'happy'     THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'curious'   ELSE 'surprised' END)
    WHEN 'sad'       THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'confused'  ELSE 'anxious'   END)
    WHEN 'angry'     THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'anxious'   ELSE 'confused'  END)
    WHEN 'anxious'   THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'sad'       ELSE 'confused'  END)
    WHEN 'confused'  THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'anxious'   ELSE 'sad'       END)
    WHEN 'surprised' THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'happy'     ELSE 'curious'   END)
    WHEN 'curious'   THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'happy'     ELSE 'surprised' END)
    WHEN 'neutral'   THEN (CASE WHEN (CRC32(je.id) % 3)=0 THEN 'curious'   ELSE 'surprised' END)
  END
WHERE
  -- ~50% of entries get a second emotion, deterministic by id
  (CRC32(je.id) % 2) = 0
  -- limit to the same seeded set we updated above
  AND (je.entry_text LIKE 'Journal entry (%'
       OR je.entry_title REGEXP ' D[0-9]{2} E[0-9]')
  -- only if the entry currently has exactly one emotion
  AND (SELECT COUNT(*) FROM entry_emotions ee_cnt WHERE ee_cnt.entry_id = je.id) = 1
  -- and avoid inserting a duplicate emotion row
  AND NOT EXISTS (
      SELECT 1 FROM entry_emotions ee2
      WHERE ee2.entry_id = je.id AND ee2.emotion_id = e2.id
  )
;

SET FOREIGN_KEY_CHECKS = 1;
