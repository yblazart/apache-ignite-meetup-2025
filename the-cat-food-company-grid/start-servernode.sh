#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: $0 <instance-number>"
    echo "Example: $0 1"
    exit 1
fi

# Vérification que l'argument est un nombre
if ! [[ $1 =~ ^[0-9]+$ ]]; then
    echo "Erreur: l'argument doit être un nombre"
    exit 1
fi

PORT=$((10900 + $1))
DEBUG_PORT=$((10500 + $1))

echo "Démarrage de Quarkus sur le port $PORT avec debug sur $DEBUG_PORT"
./mvnw quarkus:dev -Dquarkus.http.port=$PORT -Ddebug=$DEBUG_PORT