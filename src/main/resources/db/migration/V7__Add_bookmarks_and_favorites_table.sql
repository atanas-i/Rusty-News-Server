CREATE TABLE bookmarks(
    bookmark_id VARCHAR(150) PRIMARY KEY NOT NULL,
    article_id VARCHAR(150) NOT NULL,
    user_id VARCHAR(150) NOT NULL,
    bookmarked_at VARCHAR(150) NOT NULL,
    FOREIGN KEY (article_id) REFERENCES articles(article_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE favorites(
    favorite_id VARCHAR(150) PRIMARY KEY NOT NULL,
    article_id VARCHAR(150) NOT NULL,
    user_id VARCHAR(150) NOT NULL,
    FOREIGN KEY (article_id) REFERENCES articles(article_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);