#!/bin/bash

# Container name
CONTAINER_NAME="the-cat-food-cie-db"

# External port
EXTERNAL_PORT="55434"

# PostgreSQL image
POSTGRES_IMAGE="postgres:latest"

start_db() {
  echo "Starting Docker container $CONTAINER_NAME..."
  docker run -d --name "$CONTAINER_NAME" -p "$EXTERNAL_PORT":5432 -e POSTGRES_USER=thecatfoodcompany -e POSTGRES_PASSWORD=thecatfoodcompany "$POSTGRES_IMAGE"
  echo "Docker container $CONTAINER_NAME started."
}

stop_db() {
  echo "Stopping Docker container $CONTAINER_NAME..."
  docker stop "$CONTAINER_NAME"
  echo "Docker container $CONTAINER_NAME stopped."
}

case "$1" in
  start)
    start_db
    ;;
  stop)
    stop_db
    ;;
  restart)
    stop_db
    start_db
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
    ;;
esac

exit 0