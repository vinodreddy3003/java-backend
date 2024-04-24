# Use Alpine Linux as base image
FROM alpine:latest

# Install OpenJDK 18 and Maven
RUN apk add --update --no-cache openjdk18 maven

# Set the working directory
WORKDIR /java-frontend

# Copy the current directory contents into the container at /java-frontend
COPY . /java-frontend

# expose this using a port
EXPOSE 5000