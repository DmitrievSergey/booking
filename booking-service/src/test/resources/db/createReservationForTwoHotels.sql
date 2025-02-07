INSERT INTO booking_schema.users
(id, email, "password", roles, user_name)
VALUES('10badb70-7001-4511-8d5f-188a927a212f', 'admin@admin.ru', '$2a$12$mHRDaQVfaSwziaYsizYhD.U4c6zQU4BGDmgni0YUuME.ESb9/dCxO', '{ROLE_USER}', 'Admin');

INSERT INTO booking_schema.hotel
(id, address, distance, "name", number_of_rating, rating, title, town)
VALUES('e913b22d-5d21-4998-ae8f-a258fca8913f', 'Tverskaja Street 1', '9', 'First hotel', 0, 0, 'First hotel title', 'Moscow');

INSERT INTO booking_schema.hotel
(id, address, distance, "name", number_of_rating, rating, title, town)
VALUES('e913b22d-5d21-4998-ae8f-a258fca8913d', 'Tverskaja Street 2', '9', 'Second Hotel', 0, 0, 'Second hotel title', 'Moscow');

INSERT INTO booking_schema.room
(id, description, "name", "number", people_count, price, hotel_id)
VALUES('73afa3b2-067a-4b68-acc1-3cfed494fe8a', 'first room description', 'first room', '1', 1, 500, 'e913b22d-5d21-4998-ae8f-a258fca8913f');

INSERT INTO booking_schema.room
(id, description, "name", "number", people_count, price, hotel_id)
VALUES('73afa3b2-067a-4b68-acc1-3cfed494fe8b', 'first room description', 'first room', '1', 1, 500, 'e913b22d-5d21-4998-ae8f-a258fca8913d');


INSERT INTO booking_schema.reservation
(id, end_date, start_date, room_id, user_id)
VALUES('620a2dac-b9fa-40c6-a965-46726a3568b9', '1977-03-05', '1977-03-01', '73afa3b2-067a-4b68-acc1-3cfed494fe8a', '10badb70-7001-4511-8d5f-188a927a212f');