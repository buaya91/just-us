#!/usr/bin/env bash

docker build --rm
docker run -d --name just-us-postgres-test -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=just_us -p 5432:5432 buaya91/just-us-postgres