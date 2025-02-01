#!/bin/bash

# Define variables
IGNITE_VERSION="2.16.0-jdk11-nebula"
IGNITE_NETWORK="ignite-network"
DOCKER_IMAGE="yblazartsciam/apache-ignite:${IGNITE_VERSION}"
CONTAINER_PREFIX="ignite-node-"

# Function to create Docker network if it doesn't exist
create_network() {
  if ! docker network inspect "$IGNITE_NETWORK" > /dev/null 2>&1; then
    echo "Creating Docker network '$IGNITE_NETWORK'..."
    docker network create "$IGNITE_NETWORK"
  else
    echo "Docker network '$IGNITE_NETWORK' already exists."
  fi
}

# Function to start Ignite containers
start_ignite_containers() {
  read -p "Enter the number of Ignite containers to start: " NUM_CONTAINERS

  if ! [[ "$NUM_CONTAINERS" =~ ^[0-9]+$ ]]; then
    echo "Invalid number of containers. Please enter a number."
    return 1
  fi

  create_network

  for i in $(seq 1 "$NUM_CONTAINERS"); do
    CONTAINER_NAME="${CONTAINER_PREFIX}${i}"
    echo "Starting container '$CONTAINER_NAME'..."
    docker run -d --rm \
      --name "$CONTAINER_NAME" \
      --net="$IGNITE_NETWORK" \
      -p 54300-"$((54300 + i - 1))":8080 \
      -p 10800-"$((10800 + i - 1))":10800 \
      -p 47100-"$((47100 + i - 1))":47100 \
      -p 47500-"$((47500 + i - 1))":47500 \
      "$DOCKER_IMAGE"
  done
  echo "Ignite containers started."
}

# Function to list running Ignite containers
list_ignite_containers() {
  echo "Listing running Ignite containers:"
  docker ps --filter "network=$IGNITE_NETWORK" --filter "name=$CONTAINER_PREFIX" --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}"
}

# Function to stop a specific Ignite container
stop_ignite_container() {
  read -p "Enter the number of the Ignite container to stop: " CONTAINER_NUMBER
  CONTAINER_NAME="${CONTAINER_PREFIX}${CONTAINER_NUMBER}"

  if ! docker ps -a --filter "name=$CONTAINER_NAME" --quiet | grep -q "$CONTAINER_NAME"; then
    echo "Container '$CONTAINER_NAME' not found."
    return 1
  fi

  echo "Stopping container '$CONTAINER_NAME'..."
  docker stop "$CONTAINER_NAME"
  echo "Container '$CONTAINER_NAME' stopped."
}

# Function to clear all Ignite containers
clear_ignite_containers() {
  read -p "Are you sure you want to clear all Ignite containers? (yes/no): " CONFIRMATION
  if [[ "$CONFIRMATION" != "yes" ]]; then
    echo "Clear operation cancelled."
    return 1
  fi

  echo "Stopping and removing all Ignite containers..."
  docker stop $(docker ps -q --filter "network=$IGNITE_NETWORK" --filter "name=$CONTAINER_PREFIX") 2>/dev/null
  docker rm $(docker ps -aq --filter "network=$IGNITE_NETWORK" --filter "name=$CONTAINER_PREFIX") 2>/dev/null
  echo "All Ignite containers cleared."
}

# Main script logic
case "$1" in
  start)
    start_ignite_containers
    ;;
  list)
    list_ignite_containers
    ;;
  stop)
    stop_ignite_container
    ;;
  clear)
    clear_ignite_containers
    ;;
  *)
    echo "Usage: $0 {start|list|stop|clear}"
    exit 1
    ;;
esac

exit 0