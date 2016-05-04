#!/usr/bin/env bash

echo 'PASSWORD is set to' $1

docker run -d --name just-us-postgres-test -e POSTGRES_PASSWORD=$1 -p 5432:5432 just-us/postgres


