CREATE TABLE cars.user_car (
    USER_ID BIGINT NOT NULL,
    CAR_ID BIGINT NOT NULL,
    PURCHASE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (USER_ID, CAR_ID),
                                                            -- If a user is deleted, remove ownership records
    CONSTRAINT fk_user_car_user FOREIGN KEY (USER_ID) REFERENCES cars.app_user(ID) ON DELETE CASCADE,
                                                            -- Prevent deleting a car if it has been purchased
    CONSTRAINT fk_user_car_car FOREIGN KEY (USER_ID) REFERENCES cars.car(ID) ON DELETE RESTRICT
);
