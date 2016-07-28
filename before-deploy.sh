#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_434c6c55d411_key -iv $encrypted_434c6c55d411_iv -in signingkey.asc.enc -out signingkey.asc -d
    gpg --fast-import signingkey.asc
    gpg --export-secret-keys ${env.GPG_KEY_NAME} | gpg --import
fi
