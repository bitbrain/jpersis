#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
mvn deploy \
    -P sign,build-extras \
    -DskipTests \
    --settings deployment/settings.xml
fi
