create table users (
email text PRIMARY KEY,
password text NOT NULL,
name text NOT NULL
)

create table posts (
pid serial PRIMARY KEY,
author text,
title text NOT NULL,
content text NOT NULL,
post_at date NOT NULL DEFAULT CURRENT_DATE,
tags text[],
FOREIGN KEY (author) REFERENCES users(email)
);
