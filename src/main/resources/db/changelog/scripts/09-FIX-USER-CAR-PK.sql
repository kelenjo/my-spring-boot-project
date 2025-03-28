ALTER TABLE cars.user_car DROP CONSTRAINT user_car_pkey;

ALTER TABLE cars.user_car ADD COLUMN ID BIGINT NOT NULL;

ALTER TABLE cars.user_car ADD PRIMARY KEY (ID);

-- Ensure user_id and car_id remain NOT NULL
ALTER TABLE cars.user_car ALTER COLUMN USER_ID SET NOT NULL;
ALTER TABLE cars.user_car ALTER COLUMN CAR_ID SET NOT NULL;

CREATE SEQUENCE cars.user_car_seq
   INCREMENT 1
   START 1000;