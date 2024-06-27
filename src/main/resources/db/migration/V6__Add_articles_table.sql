CREATE TABLE articles(
  article_id VARCHAR(150) NOT NULL PRIMARY KEY,
  article_title VARCHAR(150) NOT NULL ,
  article_body TEXT NOT NULL,
  author VARCHAR(50) NOT NULL,
  published_at VARCHAR(150) NOT NULL,
  is_bookmarked BOOLEAN,
  is_favorite BOOLEAN,
  category VARCHAR(50)[]
);