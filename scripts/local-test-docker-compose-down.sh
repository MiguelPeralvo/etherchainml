#!/usr/bin/env bash

cd ..
docker-compose -f .ci/docker-compose.local-test.yml down
cd -