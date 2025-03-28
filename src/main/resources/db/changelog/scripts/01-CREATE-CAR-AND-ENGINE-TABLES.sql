CREATE TABLE IF NOT EXISTS cars.engine(ID BIGINT NOT NULL PRIMARY KEY,
                                            HORSEPOWER INTEGER NOT NULL,
                                                               CAPACITY DECIMAL(10, 2) NOT NULL);

CREATE TABLE IF NOT EXISTS cars.car(ID BIGINT NOT NULL PRIMARY KEY,
                                         MODEL CHARACTER VARYING(255) NOT NULL,
                                                                    YEAR INTEGER NOT NULL,
                                                                                    IS_DRIVING BOOLEAN NOT NULL,
                                                                                                       ENGINE_ID BIGINT NOT NULL REFERENCES cars.engine(ID));

CREATE SEQUENCE IF NOT EXISTS cars.engine_seq INCREMENT 1
START 1000;

CREATE SEQUENCE IF NOT EXISTS cars.car_seq INCREMENT 1
START 1000;