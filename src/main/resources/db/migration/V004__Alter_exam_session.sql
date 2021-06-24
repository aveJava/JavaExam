# В таблицу сессий добавлено поле completed (завешрена ли данная сессия)


ALTER TABLE `java_exam`.`exam_session`
    ADD COLUMN `completed` bit not null AFTER `user_id`;