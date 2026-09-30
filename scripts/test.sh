#!/usr/bin/env sh
set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)

docker run --rm \
  -v "$ROOT_DIR:/app" \
  -w /app \
  maven:3.9.9-eclipse-temurin-21-alpine \
  mvn test -q
