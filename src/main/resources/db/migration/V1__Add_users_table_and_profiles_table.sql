CREATE TABLE users(
    user_id VARCHAR(50) PRIMARY KEY NOT NULL,
    email VARCHAR(50) NOT NULL,
    hashed_password VARCHAR(150) NOT NULL
);

CREATE TABLE profiles(
    profile_id VARCHAR(50) PRIMARY KEY NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    user_profile_picture VARCHAR(50) NOT NULL
);


