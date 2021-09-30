
This Gift Certificate system is a web service where the user can create and purchase gift vouchers, namely:
- search by tag or part of the name or description for matching gift certificates;
- sort gift certificates by last update date or by name;
- create, update, partial update, delete gift certificates;
- assign, update, remove tags when creating or updating gift certificates;
- create new tags when creating or updating gift certificates.

During the operation of the application, the following exceptions are possible:
- errorCode 40001 - message: The request you sent to the server was somehow incorrect or corrupted, and the server couldn't understand it;
- errorCode 40002 - message: Http request method not supported;
- errorCode 40003 - message: The request contain incorrect parameters;
- errorCode 40004 - message: The invalid input;
- errorCode 40005 - message: The path variable;
- errorCode 40006 - message: Expected URI variable is not present among the URI variables extracted from the URL;
- errorCode 40007 - message: The resource already exists;
- errorCode 40004 - message: The invalid input;

- errorCode 40401 - message: Requested resource not found;
- errorCode 40402 - message: Page not found;

- errorCode 50001 - message: Internal Server Error;