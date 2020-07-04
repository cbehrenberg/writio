#!/bin/bash

branch=$1

set -e

mkdir -p ~/git
cd ~/git
git clone --single-branch --branch $1 https://github.com/cbehrenberg/writio.git writio-$1
