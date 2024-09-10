CREATE TABLE IF NOT EXISTS Type (
  type_id INT,
  type_name VARCHAR(63) UNIQUE NOT NULL,
  PRIMARY KEY (type_id)
);

CREATE TABLE IF NOT EXISTS Breed (
  breed_id SERIAL,
  type_id INT,
  breed_name VARCHAR(63) NOT NULL,
  PRIMARY KEY (breed_id),
  FOREIGN KEY (type_id) REFERENCES Type (type_id)
);

CREATE TABLE IF NOT EXISTS "User" (
    id VARCHAR(40) NOT NULL,
    first_name VARCHAR(63) NOT NULL,
    last_name VARCHAR(63) NOT NULL,
    username VARCHAR(25) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    user_photo BYTEA,
    email VARCHAR(63) NOT NULL,
    telephone VARCHAR(63),
    role VARCHAR(15) NOT NULL,
    updated_at TIMESTAMP DEFAULT now(),
    created_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Shelter (
  shelter_id VARCHAR(40),
  user_id VARCHAR(40),
  shelter_name VARCHAR(63),
  shelter_address VARCHAR(63),
  shelter_latitude NUMERIC NOT NULL,
  shelter_longitude NUMERIC NOT NULL,
  PRIMARY KEY (shelter_id),
  FOREIGN KEY (user_id) REFERENCES "User" (id)
);


CREATE TABLE IF NOT EXISTS Pet (
  pet_id VARCHAR(40),
  user_id VARCHAR(40),
  pet_name VARCHAR(63),
  pet_type INT NOT NULL,
  pet_age VARCHAR(40),
  pet_breed INT,
  pet_gender VARCHAR(30),
  pet_size VARCHAR(30),
  updated_at TIMESTAMP DEFAULT NOW() NOT NULL,
  created_at TIMESTAMP DEFAULT NOW() NOT NULL,
  PRIMARY KEY (pet_id),
  FOREIGN KEY (user_id) REFERENCES "User" (id),
  FOREIGN KEY (pet_type) REFERENCES Type (type_id),
  FOREIGN KEY (pet_breed) REFERENCES Breed (breed_id)
);

CREATE TABLE IF NOT EXISTS Report (
  report_id VARCHAR(40),
  user_id VARCHAR(40) NOT NULL,
  pet_id VARCHAR(40) NOT NULL,
  report_type VARCHAR(20) NOT NULL,
  report_status VARCHAR(20) NOT NULL,
  address VARCHAR(63),
  latitude NUMERIC NOT NULL,
  longitude NUMERIC NOT NULL,
  note TEXT,
  updated_at TIMESTAMP DEFAULT NOW() NOT NULL,
  created_at TIMESTAMP DEFAULT NOW() NOT NULL,
  PRIMARY KEY (report_id),
  FOREIGN KEY (user_id) REFERENCES "User" (id),
  FOREIGN KEY (pet_id) REFERENCES Pet (pet_id)
);

CREATE TABLE Image (
    image_id VARCHAR(40) UNIQUE PRIMARY KEY,
    image VARCHAR(100) NOT NULL,
    texture_histogram VARCHAR(100),
    color_histogram VARCHAR(100),
    FOREIGN KEY (image_id) REFERENCES Pet (pet_id)
);

CREATE TABLE IF NOT EXISTS Notifies (
  user_id VARCHAR(40) NOT NULL,
  report_id VARCHAR(40) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES "User" (id),
  FOREIGN KEY (report_id) REFERENCES Report (report_id)
);

CREATE TABLE IF NOT EXISTS Comment (
  comment_id VARCHAR(40) NOT NULL,
  report_id VARCHAR(40) NOT NULL,
  user_id VARCHAR(40) NOT NULL,
  comment VARCHAR(150) NOT NULL,
  updated_at TIMESTAMP DEFAULT NOW() NOT NULL,
  created_at TIMESTAMP DEFAULT NOW() NOT NULL,
  PRIMARY KEY (report_id, comment_id),
  FOREIGN KEY (user_id) REFERENCES "User" (id),
  FOREIGN KEY (report_id) REFERENCES Report (report_id)
);


CREATE TABLE IF NOT EXISTS Log (
  log_id SERIAL PRIMARY KEY,
  timestamp TIMESTAMP DEFAULT NOW() NOT NULL,
  log_level VARCHAR(10) NOT NULL,
  class_name VARCHAR(255) NOT NULL,
  method_name VARCHAR(255) NOT NULL,
  message TEXT NOT NULL
);
