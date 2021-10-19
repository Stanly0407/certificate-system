
This Gift Certificate system is a web service where the user can create and purchase gift vouchers, namely:
- search by tag or part of the name or description for matching gift certificates;
- sort gift certificates by last update date or by name;
- create, update, partial update, delete gift certificates;
- assign, update, remove tags when creating or updating gift certificates;
- create new tags when creating or updating gift certificates.

During the operation of the application, the following exceptions are possible:
- errorCode 40001 - message: The request you sent to the server was somehow incorrect or corrupted, and the server couldn't understand it;
- errorCode 40501 - message: Http request method not supported;
- errorCode 40003 - message: The request contain incorrect parameters;
- errorCode 40004 - message: The invalid input;
- errorCode 40005 - message: The path variable;
- errorCode 40006 - message: Expected URI variable is not present among the URI variables extracted from the URL;
- errorCode 40007 - message: The resource already exists;
- errorCode 40008 - message: Parameter is not filled or filled incorrectly;
- errorCode 40009 - message: Unrecognized JSON field (not marked as ignorable);
- errorCode 40010 - message: Incorrect sorting parameters (possible: by "name", "date");
- errorCode 40011 - message: Required request header is not present;

- errorCode 40101 - message: Unauthorized error: Full authentication is required to access this resource;
- errorCode 40102 - message: Unauthorized error: Please make a new login request;
- errorCode 40103 - message: Unauthorized error: Incorrect token;
- errorCode 40104 - message: Unauthorized error: Unknown Login or Password;

- errorCode 40301 - message: Access denied;   

- errorCode 40401 - message: Requested resource not found with resource id;
- errorCode 40402 - message: Page not found;
- errorCode 40402 - message: Requested resource not found;

- errorCode 41501 - message: Content type not supported;
- errorCode 42201 - message: login is already taken;


- errorCode 50001 - message: Internal Server Error;
