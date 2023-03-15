__________DB Migration SQLCipher__________
It is a 256 bit AES encryption in CBC mode.
The encryption key is derived from the passphrase using a random salt (stored in the first 16 bytes of the database file) and the standardized PBKDF2 algorithm with an SHA1, SHA256, or SHA512 hash function.

__________STEPS_____
1. Run the project without using SupportFactory for creating DB instance.
2. Insert records to later test the migrated data.
3. Uncomment/Add the SupportFactory in DB instance creation.
4. Delay the viewModel, repository initiaization (however DB instance is being called), because we need to call "sqlcipher_export" query to export plain/unencrypted DB to an encrypted one.
5. Delete OG Database file.
6. After migration, make any DB operation which first checks for viewmodel/oberserver initialization, then make the DB call.
7. The new records will be inserted in the new DB and the old data is also saved.

Links: 
Official Developer Site: https://www.zetetic.net/sqlcipher/

Medium Blog: https://sonique6784.medium.com/protect-your-room-database-with-sqlcipher-on-android-78e0681be687#:~:text=We%20first%20need%20to%20create,the%20Room%20Database%20is%20encrypted

Official SqlCipher Github: https://github.com/sqlcipher/android-database-sqlcipher#using-sqlcipher-for-android-with-room

Reference: https://stackoverflow.com/questions/21165676/sqlcipher-export-didnt-export-my-tables

Sample Code Github: https://github.com/sqlcipher/sqlcipher-android-tests/blob/master/app/src/main/java/net/zetetic/tests/ImportUnencryptedDatabaseTest.java