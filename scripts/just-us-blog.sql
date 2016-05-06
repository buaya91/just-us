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
