FROM node:6.9.1

# Create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Bundle app source
COPY . /usr/src/app

# Install dependencies
RUN npm install --global angular-cli &&\
        npm install --unsafe-perm &&\
            npm cache clean