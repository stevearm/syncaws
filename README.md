#SyncAWS

This utility fingerprints a given directory using md5 hashes, and uploads it to S3.
Subsequent uploads only upload changed files, and the index file links to old files
to download any unchanged files.

## Usage
Assuming syncaws is a script to run java -jar syncaws.jar --configDir <configDir> %

`syncaws init`

 - if no syncaws.js config file exists, create a blank

`syncaws validate`

 - check that the config loads properly, and that the aws info (keys, bucket name and base dir)
   are valid requires internet connection

`syncaws create --project <projectName> --dir <projectDir>`

 - projectName must not already exist in the config
 - projectDir must exist
 - adds the project to the config and saves it
 - creates the project dir on S3 and uploads the index.html and a blank indexList.js

`syncaws --project <projectName> scan`

 - projectName must exist in the config
 - Scan the project's folder, and save the fingerprint in the config directory under projectId.js

`syncaws --project <projectName> upload [--dry-run]`

 - projectName must exist in the config
 - fingerprint must exist for the given project at projectId.js
 - there must exist an indexList.js on S3 for the given project (it can be empty)
 - compares the saved fingerprint with the most recent index on S3 (if any)
 - display the total number of files to be uploaded, and the sum of their sizes
 - if not dry run:
 -- upload the new created index
 -- upload files
 -- upload new indexList.js to include the new index

## Not yet implemented functions

`syncaws --project <projectName> recover`

 - projectName must exist in the config
 - fingerprint must exist for the given project at projectId.js
 - there must exist an indexList.js on S3 for the given project (it can be empty)
 - scan S3 for any indexes not included in the indexList.js
 - for each orphaned index found, compare it to the fingerprint
 -- if they are the same, prompt the user to delete, finish, or do nothing
 -- if they are different, prompt for delete or do nothing
 -- for delete:
 --- for all new files in the index (not in the previous one) that exist on S3, delete them
 ---- delete the index
 -- for finish:
 --- for all new files in the index (not in the previous one) that are not on S3, upload them
 ---- update indexList.js to include the new index

`syncaws --project <projectName> prune`

* projectName must exist in the config
* show a list of the dates available
** ask which one to delete upto (do not allow sparse lists, only delete oldest X entries)

##Known bugs and features to implement

 - Command-line output is messy
