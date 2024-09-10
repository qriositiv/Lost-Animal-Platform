#!/bin/bash

convert_to_sql() {
  local typescript_array="$1"
  local type_id="$2"
  local output="INSERT INTO Breed (type_id, breed_name) VALUES\n"

# Remove the first and last lines using awk
  typescript_array=$(echo "$typescript_array" | awk 'NR>1 {print p} {p=$0}' | awk 'NR>1')

  # Split the array items into individual lines
  breeds=$(echo "$typescript_array" | sed -e "s/^[[:space:]]*'//" -e "s/'[[:space:]]*$//" -e "s/',$//" -e "s/^ *'//" -e "s/^ *\"//" -e "s/\", */'),\n  ($type_id, '/g" -e "s/\"$/'/")

  IFS=$'\n' read -r -d '' -a breed_array <<< "$breeds"

  for breed in "${breed_array[@]}"; do
    output="$output  ($type_id, '${breed//\'/\'\'}'),\n"
  done

  echo -e "$output"
}

typescript_array=$(cat "cat-breeds.ts")

convert_to_sql "$typescript_array" 2
