#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

if [ -z $1 ] || [ -z $2 ] ; then
    echo "The REPO_DIR or WORK_DIR is empty and it needs to point to the corresponding directories."
    echo "Please run it with:"
    echo " $0 REPO_DIR WORK_DIR"
    exit 1
fi

REPO_DIR="$1"
WORK_DIR="$2"

# @description Check that a path is a directory, and stop the script with
#   an error message when it is not.
# @arg $1 string The path to check, such as REPO_DIR or WORK_DIR.
# @exitcode 0 The path is a directory.
# @exitcode 1 The path is not a directory; the whole script exits.
# @example
#   _is_dir "$REPO_DIR"
_is_dir(){
    test -d "$1" && return
    echo "The '$1' is not a directory."
    exit 1
}

_is_dir "$REPO_DIR"
_is_dir "$WORK_DIR"


$WORK_DIR/oe-core/scripts/contrib/patchreview.py -v -b -j status.json $REPO_DIR

# return an error if any malformed patch is found
cat $WORK_DIR/build/status.json |
    python3 -c "import json,sys;obj=json.load(sys.stdin); sys.exit(1) if 'malformed-sob' in obj[0] or 'malformed-upstream-status' in obj[0] else sys.exit(0)"
