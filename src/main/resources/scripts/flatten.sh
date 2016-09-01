#!/bin/bash

# Flatten with folder names.
shopt -s globstar
for i in **/*.*; do mv "$i" "${i//\//_}"; done

echo "Flatten files done!"
read -p "Press [Enter] to continue..."

# Remove white spaces from directories and filenames
find -name "* *" -type d | rename 's/ /_/g'
find -name "* *" -type f | rename 's/ /_/g'

echo "Remove white spaces in files names done!"
read -p "Press [Enter] to continue..."

# Delete Non Alphanumeric characters
for f in *; do mv "$f" "$(sed 's/[^0-9A-Za-z_.]/_/g' <<< "$f")"; done

echo "Removed non alphanumeric characters done!"
read -p "Press [Enter] to continue..."

# Delete old directories
find . -name ".svn" -type d -empty -delete

echo "Removed empty folders done!"
read -p "Press [Enter] to continue..."

# Delete non image files
find -name '*.db' -type f -exec rm '{}' \

echo "Removed non image files done!"



