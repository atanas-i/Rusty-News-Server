ALTER TABLE profiles
ADD CONSTRAINT profiles_users_user_id_fk
   FOREIGN KEY (user_id) REFERENCES users(user_id)

-- alter table profiles
--     add constraint profiles_users_user_id_fk
--         foreign key (user_id) references users;