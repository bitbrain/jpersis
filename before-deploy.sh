#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc \
        -K $encrypted_434c6c55d411_key \
        -iv $encrypted_434c6c55d411_iv \
        -in codesigning.asc.enc \
        -out codesigning.asc \
        -d
    gpg \
        --fast-import codesigning.asc
fi
