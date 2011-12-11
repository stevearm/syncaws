#SyncAWS

This utility fingerprints a given directory using md5 hashes, and uploads it to S3.
Subsequent uploads only upload changed files, and the index file links to old files
to download any unchanged files.

## Usage
Assuming syncaws is a script to run `java -jar syncaws.jar --configDir <configDir> %`

`syncaws init`

1. if no syncaws.js config file exists, create a blank

`syncaws validate`

Requires internet connection

1. check that the config loads properly
2. check that the aws info (keys, bucket name and base dir) are valid 

`syncaws create --project <projectName> --dir <projectDir>`

`projectName` must not already exist in the config, and `projectDir` must exist

1. adds the project to the config and saves it
2. creates the project dir on S3 and uploads the index.html and a blank indexList.js

`syncaws --project <projectName> scan`

`projectName` must exist in the config

1. Scan the project's folder, and save the fingerprint in the config directory under projectId.js

`syncaws --project <projectName> upload [--dry-run]`

`projectName` must exist in the config, fingerprint must exist for the given project at projectId.js, and
there must exist an `indexList.js` on S3 for the given project (it can be empty)

1. compares the saved fingerprint with the most recent index on S3 (if any)
2. display the total number of files to be uploaded, and the sum of their sizes
3. if dry run, exit at this point
4. upload the new created index
5. upload files
6. upload new indexList.js to include the new index
