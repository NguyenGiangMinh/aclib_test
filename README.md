## Library Management System Backend (aclib_test)
This is the backend service for a library management system built with Spring Boot by JAVA. It manages book records, user authentication, borrowing processes, and integrates with the Google Books API.

## Inheritance tree
-
-
-

## UML Diagram
[Image UML](https://github.com/user-attachments/assets/a6e4aefd-d41e-4242-9248-6a8a9312b667)

## Design pattern
MVC pattern. [MVC Structure](https://github.com/user-attachments/assets/5f252ac0-66d2-43b3-bd52-bfd27cc850b3)

The application follows the MVC pattern:

Model: book, user, loan
View: Frontend integration with endpoints
Controller: RESTful APIs for interaction

## Features
Email Notifications
 * Asynchronous Processing for efficient email handling.
    - Send OTP after user registration (OTP expires after 5 minutes).
    - Resend OTP (limited to once per minute).
    - Notify users when borrowing or returning books.

User Management:

1. User registration and login with role-based access (ROLE_USER, ROLE_ADMIN).
2. Profile upload functionality.

Book Management:

1. Search books from the Google Books API.
2. Add, update, and delete books in the library.
3. Borrow and return functionality with real-time stock updates.

Loan Management:

1. Borrow book tracking, renewal, and overdue notifications.
2. Automated stock decrement for overdue/unreturned books.

Administrator Management:

1. Have the same usage as a regular-user
2. Add copy of book, delete book
3. Make a regular user to a new administrator

## Special feature for administrator role: (server side only)
We've developed some code that are really effective to control the overdue loan from regular users. 
The server will automatically check the loan list and send the email to notify the user whose overdue loan.
When the user recieved, they can choose to make a new loan or return the book. On the other hand, if they do not act anything from a time that we set, the book will be labeled with "LOST" 
and an email will be sent to all adminstrators. From that, the admintrator can decide what they have to do after that. 

Borrowing period: 150 days (new users: 30 days)
Notify users: 10 days after due date.
Renewal limit: 1 time.
Testing Timeframes:
- Borrowing period: 3 minutes
- Notify users: 2 minutes.

## Key Endpoints

1. User Endpoints
GET /mls_user/profile - Retrieve user profile.
2. Register Endpoints
POST /register
POST /verifying_otp
POST /resenting_otp
4. Login Endpoints
POST /login - enter login
GET /auth/user - support frontend only
5. Admin Endpoints
POST /admin/book/add-copies - Add a number of books.
PUT /admin/book/update - Update book details.
DELETE //admin/book/id?BookId=<Id_in_db> - Delete a book by ID.
POST /admin/newAdmin - make a regular to administrator.
7. Book Endpoints
GET /api/book/searching?title=<your_book_title> - search with title.
GET /api/book/searchingWithId?id=<your_book_id> - seach with ID (ID here is the id for self_link of google)
GET /api/book/searchingWithCategory?category=<your_book_category> - search with book's category.
GET /api/book/homepage - homepage of my team's web application
8. Loan Endpoints
POST /api/loan/borrowing - Borrow a book.
POST /api/loan/returning - Return a book.
PUT /api/loan/renewing - Renew a loan. (just test with postman)
GET /api/loan/loanDeals - View loans for a specific user.

CORS Configuration
The backend allows cross-origin requests from:

## Frontend

Frontend: http://localhost:5173 .
To enable this, a global CORS configuration is implemented.

## Testing - Giang minh:


## Requirements
Java 17 or higher: (recommend using IntelliJ)
Spring Boot 3.x
MySQL for database
SMTP for email notifications
Setup
Clone the repository:

## Notes
1.The database is automatically created on the first run.
2.Run these SQL queries to ensure proper table setup: sql
- ALTER TABLE aclib_book MODIFY thumbnail TEXT;
- ALTER TABLE aclib_book ADD INDEX (id_self_link);
## Known Issues
1. Ensure CORS is configured correctly for the frontend.
2. Session handling should align with frontend expectations.
## Future Enhancements
1. Implement advanced user analytics.
2. Batch process overdue loans.

## Feel free to adjust this based on your specific project details!
