#!/bin/bash

# Check if the OCI artifact name has been passed [cite: 2724]
if [ -z "$1" ]; then
    echo "Error: You must specify the artifact name (user/repo:tag)."
    echo "Usage: ./publish_docker-compose.sh <dockerhub_username>/<compose_repo_name>:<tag>"
    exit 1
fi

# Publish the docker-compose.yml file as an OCI Artifact [cite: 1503]
docker compose publish "$1"