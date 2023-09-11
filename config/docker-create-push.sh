#!/bin/bash

# Default values
DEFAULT_AWS_ACCOUNT_ID=539416276730
DEFAULT_REGION="us-east-2"
DEFAULT_REPOSITORY_NAME="jmuscles"
DEFAULT_DOCKER_IMAGE_TAG="jmuscles-rest-producer-app-j8sb2713"
DEFAULT_DOCKERFILE_PATH="../"

# Ask for input or use default values
echo -n "Enter your AWS account ID (default: $DEFAULT_AWS_ACCOUNT_ID): "
read AWS_ACCOUNT_ID
AWS_ACCOUNT_ID=${AWS_ACCOUNT_ID:-$DEFAULT_AWS_ACCOUNT_ID}

echo -n "Enter the AWS region (default: $DEFAULT_REGION): "
read REGION
REGION=${REGION:-$DEFAULT_REGION}

echo -n "Enter the ECR repository name (default: $DEFAULT_REPOSITORY_NAME): "
read REPOSITORY_NAME
REPOSITORY_NAME=${REPOSITORY_NAME:-$DEFAULT_REPOSITORY_NAME}

echo -n "Enter the Docker image tag (default: $DEFAULT_DOCKER_IMAGE_TAG): "
read DOCKER_IMAGE_TAG
DOCKER_IMAGE_TAG=${DOCKER_IMAGE_TAG:-$DEFAULT_DOCKER_IMAGE_TAG}

echo -n "Enter the path to your Dockerfile (default: $DEFAULT_DOCKERFILE_PATH): "
read DOCKERFILE_PATH
DOCKERFILE_PATH=${DOCKERFILE_PATH:-$DEFAULT_DOCKERFILE_PATH}

# Build the Docker image
echo "Building Docker image..."
docker build -t $DOCKER_IMAGE_TAG $DOCKERFILE_PATH

# Log in to ECR
echo "Logging in to ECR..."
AWS_LOGIN=$(aws ecr get-login --no-include-email --region $REGION)
$AWS_LOGIN

# Tag the Docker image with ECR repository URI
ECR_IMAGE_URI="$AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPOSITORY_NAME:$DOCKER_IMAGE_TAG"
echo "Tagging Docker image: $DOCKER_IMAGE_TAG as $ECR_IMAGE_URI..."
docker tag $DOCKER_IMAGE_TAG $ECR_IMAGE_URI

aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com

# Push the Docker image to ECR
echo "Pushing Docker image to ECR..."
docker push $ECR_IMAGE_URI

echo "Docker image pushed to ECR: $ECR_IMAGE_URI"