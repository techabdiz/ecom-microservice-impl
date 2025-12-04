#!/bin/bash

cd kafka
docker-compose up -d
cd ..
cd mongo
docker-compose up -d
cd ..

