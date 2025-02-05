INSERT INTO booking_schema.hotel
(id, address, distance, "name", number_of_rating, rating, title, town)
VALUES('e913b22d-5d21-4998-ae8f-a258fca8913f', 'Tverskaja Street 1', '9', 'First hotel', 0, 0, 'First hotel title', 'Moscow');

INSERT INTO booking_schema.room
(id, description, "name", "number", people_count, price, hotel_id)
VALUES('73afa3b2-067a-4b68-acc1-3cfed494fe8a', 'first room description', 'first room', '1', 1, 500, 'e913b22d-5d21-4998-ae8f-a258fca8913f');