CREATE TABLE users(
    user_id VARCHAR(50) PRIMARY KEY NOT NULL,
    email VARCHAR(50) NOT NULL,
    hash_password VARCHAR(150) NOT NULL
)

CREATE TABLE profiles(
    profile_id VARCHAR(50) PRIMARY KEY NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
    user_name VARCHAR(50),
    user_profile_picture VARCHAR(150)
)