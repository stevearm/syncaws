#SyncAWS

This utility fingerprints a given directory using md5 hashes, and uploads it to S3.
Subsequent uploads only upload changed files, and the index file links to old files
to download any unchanged files.

## Licence
This project is released under the Apache 2.0. See attached LICENCE.txt

## Usage
Assuming syncaws is a script to run `java -jar syncaws.jar --configDir <configDir> %`

`syncaws init`

1. If no syncaws.js config file exists, create a blank

`syncaws validate`

1. Check that the config parses and loads properly
2. Test that the aws info (keys, bucket name and base dir) are valid 

`syncaws create <projectName> <projectDir>`

1. Adds the project to the config and saves it
2. Creates the project dir on S3 and uploads the html and js display files

`syncaws status <projectName>`

1. Update the local index for `projectName` if needed
2. Compare the local index with the newest uploaded index and list any new files

`syncaws upload <projectName>`

1. Update the local index for `projectName` if needed
2. Compare the local index with the newest uploaded index and create a list of all new files
3. Upload the new index
4. Upload all new files
5. Update the display files to add the new index as the newest
