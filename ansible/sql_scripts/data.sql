-- BEFORE THIS don't forget to add breeds.sql

INSERT INTO "User" (id, first_name, last_name, username, password, user_photo, email, telephone, role) VALUES
  ('01901d85-1808-7343-9fa4-4b4e3b2fa6d6', 'Alex', 'Straysafe', 'client1', '$2a$10$DH9CRcubFERp5qwJOZz/6.AT1e.72mVQ4FtFPTgJhF73ASMw42yNW', '', 'client.mail@straysafe.com', '+37060000000', 'USER');

INSERT INTO "User" (id, first_name, last_name, username, password, user_photo, email, telephone, role) VALUES
  ('01901d64-a88e-73fe-bbb7-91daa9422cd1', 'Eva', 'Straysafe', 'shelter1', '$2a$10$pzA2GLT/sBadEU3Bc5n9FuNQy69NG0zcnCt1N2Op0uP6nqHK86fC6', '', 'shelter.mail@straysafe.com', '+37060000001', 'SHELTER');

INSERT INTO Shelter (user_id, shelter_id, shelter_name, shelter_address, shelter_latitude, shelter_longitude)
SELECT id, 'shelter1', 'Baltupiu pieglaudos', 'Baltupiai', 54.7272159607266, 25.27908188689602
FROM "User"
WHERE username = 'shelter1';

INSERT INTO "User" (id, first_name, last_name, username, password, user_photo, email, telephone, role) VALUES
  ('01901d79-ad46-75ab-8233-d4fb8a5e3112', 'Tadas', 'Straysafe', 'moderator1', '$2a$10$MvwS23pWLuUnNZZtfUHXC..2FTj2Ov1n0o3myCD6xPKV7iK8SOc3u', '', 'moderator.mail@straysafe.com', '+37060000002', 'MODERATOR');
