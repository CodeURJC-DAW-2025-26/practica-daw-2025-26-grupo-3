#!/bin/bash

# Check if the image name to publish has been passed [cite: 2722]
if [ -z "$1" ]; then
    echo "Error: You must specify the image name to publish."
    echo "Usage: ./publish_image.sh <dockerhub_username>/<image_name>"
    exit 1
fi

# Publish to DockerHub [cite: 1817]
docker push "$1"