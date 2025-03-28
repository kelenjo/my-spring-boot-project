ALTER TABLE cars.user_car DROP CONSTRAINT fk_user_car_car;


ALTER TABLE cars.user_car
ADD CONSTRAINT fk_user_car_car_corrected
FOREIGN KEY (CAR_ID) REFERENCES cars.car(ID) ON DELETE RESTRICT;
