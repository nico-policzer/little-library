# Library

## Project Proposal

This project is an application for a library system.

It allows library users to
- Borrow books
- See information about books including author, genre and review rating
- Return books they have taken out
- Leave reviews for books they return
- Log in as an **admin** to register books and look at transaction history

It will also allow users to look through the library catalogue through categories like author and genre and read other user reviews that have been left, and will provide an average rating for the books, based on reviews. 
The application will also have functionality to track users reading history. 

This application would be used by library users or admins who are interested in taking out or donating books, or curious about others ratings of books.

## Personal interest in this project
I personally like reading books and am excited to work on this abstraction of them through this project.
The project also provides a fun challenge, with the functionality for users to leave and read reviews and search through books through a number of different search criteria.

## User Stories
* As a user, I want to be able to borrow and then return a book from the library
* As a user, I want to be able to register books to the library and see the general transaction history
* As a user, I want to be able to read through reviews of books and see their overall rating
* As a user, I want to be able to search through the librarys book collection by author, genre and rating
* As a user, I want the library to save it's state of books and members when it closes
* As a user, when I open the library, I want it to load the previous state of the library

NOTE: To access the admin menu to register books and view the libraries' history, please sign in as member "admin"


# Instructions for Grader
* Run Main.main()
* Select "Create a new library with a default set of books"
* Input a name for your library(doesn't matter what it is)

- You can generate the first required event related to adding Xs to a Y by clicking "Register Book", then inputting the title, author and genre of a book. After clicking register, to see this book you can click "Search" in the main menu.
- You can generate the second required event related to adding Xs to a Y by clicking "Search" in the main menu, then clicking on the title of any book, and clicking "Borrow". Use the windows X buttons to exit from the confirmation screen and the search screen and return to the menu.
- You can locate my visual component by (from the main menu) first clicking "Return". You will then see books you have previously borrowed and can return, click one. If there are none, repeat the previous step.
- You can save the state of my application by (from the main menu), selecting "Quit", then "Yes"
- You can reload the state of my application by, (directly after running main) selecting "Load from a previous library" on the opening menu, then selecting the library you created. You should see the book you added present in either "Search" or "Return"(depending on whether you returned it)