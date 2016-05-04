CREATE DATABASE just_us;

\connect just_us;

CREATE TABLE users (
email text PRIMARY KEY,
password text NOT NULL,
name text NOT NULL
);

CREATE TABLE posts (
pid serial PRIMARY KEY,
author text,
title text NOT NULL,
content text NOT NULL,
post_at DATE NOT NULL DEFAULT CURRENT_DATE,
tags text[] NOT NULL DEFAULT '{}',
FOREIGN KEY (author) REFERENCES users(email)
);

INSERT INTO users VALUES('l.q.wei91@gmail.com', '$2a$10$JhbPAWpRzUZekd/xxigCruaWnnyZeV2roLqenCfNpo5IBjgfZMcVC', 'Qingwei');
INSERT INTO users VALUES('suhui1128@gmail.com', '$2a$10$JhbPAWpRzUZekd/xxigCruaWnnyZeV2roLqenCfNpo5IBjgfZMcVC', 'Suhui');
