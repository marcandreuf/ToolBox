#!/bin/bash


# Flatten with folder names.
shopt -s globstar
for i in **/*.*; do mv "$i" "${i//\//_}"; done

# Remove white spaces from directories and filenames
find -name "* *" -type f | rename 's/ /_/g'

# Delete Non Alphanumeric characters
for f in *; do mv "$f" "$(sed 's/[^0-9A-Za-z_.]/_/g' <<< "$f")"; done

# Delete old directories
find . -name "*" -type d -empty -delete

# Delete non image files
find -name '*.db' -type f -exec rm '{}' \;




