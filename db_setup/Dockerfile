# Start from the official MongoDB image
FROM mongo:latest

# Set the entry point script to run on container start
COPY init-mongo.sh /docker-entrypoint-initdb.d/

# Copy the dump of your existing data
COPY dump/ /docker-entrypoint-initdb.d/dump/