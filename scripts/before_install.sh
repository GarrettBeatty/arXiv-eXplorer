#!/bin/bash

#if [[ $TRAVIS_BRANCH == "master" && $TRAVIS_PULL_REQUEST == "false" ]]; then
openssl aes-256-cbc -K $encrypted_77bee7102036_key -iv $encrypted_77bee7102036_iv -in secrets.tar.gz.enc -out secrets.tar.gz -d
tar xzvf secrets.tar.gz
#fi

