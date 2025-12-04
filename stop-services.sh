#!/bin/bash

cd kafka
docker-compose down
cd ..
cd mongo
docker-compose down
cd ..

