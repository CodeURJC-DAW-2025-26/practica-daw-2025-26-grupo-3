#!/bin/bash

# Check if the image name has been passed as a parameter [cite: 2721]
if [ -z "$1" ]; then
    echo "Error: You must specify the image name."
    echo "Usage: ./create_image.sh <dockerhub_username>/<image_name>"
    exit 1
fi

# Build the image [cite: 1660, 1779]
# -f Dockerfile: indicates the instructions file
# .. : indicates that the build context is the 'backend' folder to see the source code
docker build -t "$1" -f Dockerfile ..