#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
mvn deploy -P sign,build-extras -X --settings settings.xml
fi
