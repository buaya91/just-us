#!/usr/bin/env bash
activator assembly &&
mv target/scala-2.11/just-us-assembly-1.0.jar dist/ &&
scp -r dist root@128.199.77.84:/root/just-us/backend/