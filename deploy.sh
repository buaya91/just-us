#!/usr/bin/env bash
act assembly &&
mv target/scala-2.11/just-us-assembly-1.0.jar dist/ &&
scp -r dist root@128.199.204.38:just-us/