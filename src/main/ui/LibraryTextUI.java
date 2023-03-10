package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.*;

// SOURCES:
// https://www.programiz.com/java-programming/examples/capitalize-first-character-of-string
// - Used to create a helper method to capitilze the start of each word in a string
// FitLifeGymChainUi
// - Used makePrettyString and getUserInputString, as well as inspiration for menu design using switch statements


public class LibraryTextUI {

    // LIBRARY APP: runs the library UI, text based. User's can access various menus, starting by signing in
    // then accessing the main menu, which allows users to access book search functionality, return books and leave
    // reviews, view their history of currently borrowed books and previously returned books. All menus have options
    // to go back to the previous menu. To register new book to the library, the user can sign in as "admin"
    // in the sign in menu, and add books to the library, as well as access the librarys transaction(returned books)
    // history.

    private Library lib;
    private Member member;
    private Scanner input;
    // COMMANDS:
    private static final String UNKNOWN_COMMAND_MESSAGE = "\tUnrecognized command, please try a different command";

    private static final String SAVE_COMMAND = "yes";
    private static final String DONT_SAVE_COMMAND = "no";
    private static final String NEW_LIBRARY_COMMAND = "new";
    private static final String LOAD_LIBRARY_COMMAND = "load";
    private static final String DEFAULT_LIBRARY_COMMAND = "default";
    // LOG IN MENU COMMANDS
    private static final String REGISTER_MEMBER_COMMAND = "register";
    private static final String SIGN_IN_COMMAND = "sign in";
    private static final String QUIT_COMMAND = "quit";
    // MAIN MENU COMMANDS
    private static final String MEMBER_PROFILE_COMMAND = "profile";
    private static final String RETURN_BOOK_COMMAND = "return";
    private static final String SEARCH_BOOK_COMMAND = "search";
    private static final String LOG_OUT_COMMAND = "log out";
    // SEARCH MENU COMMANDS
    private static final String BY_AUTHOR_COMMAND = "author";
    private static final String BY_TITLE_COMMAND = "all";
    private static final String BY_GENRE_COMMAND = "genre";
    private static final String BACK_COMMAND = "back";
    // BOOK MENU COMMANDS
    private static final String BORROW_BOOK_COMMAND = "borrow";
    private static final String VIEW_RATINGS_COMMAND = "ratings";
    // ADMIN MENU COMMANDS
    private static final String REGISTER_BOOK_COMMAND = "register book";
    private static final String VIEW_TRANSACTIONS_COMMAND = "transactions";
    // RETURN COMMANDS
    private static final String CANCEL_COMMAND = "cancel";

    public static void main(String[] args) {
        new LibraryTextUI();
    }
    // EFFECTS: Begins running the library application
    public LibraryTextUI() {
        runLibrary();
    }



    // MODIFIES: this
    // EFFECTS: initializes library, starts login menu, and directs logged-in user to proper menu(admin or main)
    private void runLibrary() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        boolean run = true;
        lib = loadMenu();
        while (run) {
            run = logOnMenu();
            if (!run) {
                // skips to end
            } else if (member.isAdmin()) {
                adminMenu();
            } else {
                mainMenu();
            }
        }
        handleSave();
        end();
    }

    // MODIFIES: this
    // EFFECTS: prints goodbye message, closes input
    private void end() {
        System.out.println("\nCome back soon!");
        input.close();

    }

    // LOG IN MENU METHODS:


    // EFFECTS: prints sign in menu options
    private void displayLogOnMenu() {
        System.out.println("\nWelcome to " + lib.getName());
        System.out.println(REGISTER_MEMBER_COMMAND + ": register a new member");
        System.out.println(SIGN_IN_COMMAND + ": sign in as returning member");
        System.out.println(QUIT_COMMAND + ": quit");
    }

    // MODIFIES: this
    // EFFECTS: parses input for sign in menu, allowing users to register or sign in and return true, or quit
    // to end the program and return false.
    private boolean logOnMenu() {
        while (true) {
            boolean loggedIn = false;
            displayLogOnMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case REGISTER_MEMBER_COMMAND:
                        loggedIn = handleRegister();
                        break;
                    case SIGN_IN_COMMAND:
                        loggedIn = handleSignIn();
                        break;
                    case QUIT_COMMAND:
                        return false;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
                if (loggedIn) {
                    return true;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: asks user for new member name to register, if member of that name already exists returns false, if
    // not, registers member to the library and returns true
    private boolean handleRegister() {
        System.out.println("\nPlease enter your name to register:");
        String memberName = capitalize(getUserInputString());
        Member registeredMember = new Member(memberName);
        if (lib.registerMember(registeredMember)) {
            System.out.println("Registering " + memberName + " to " + lib.getName());
            member = registeredMember;
            return true;
        } else {
            System.out.println("\tThere is already a member of that name, please use a different name");
            return false;
        }
    }

    // EFFECTS: displays currently registered members and prompts sign in
    private void displaySignInMenu() {
        System.out.println("\nRegistered members:");
        for (Member m : lib.getMembers()) {
            System.out.println(m.getName());
        }
        System.out.println("please enter member name to sign in:");
    }

    // MODIFIES: this
    // EFFECTS: displays librarys current members, and parses input for member name to sign in. if input matches
    // registered member, logs in member and returns true, if not returns false.
    private boolean handleSignIn() {
        displaySignInMenu();
        String memberName = getUserInputString();
        for (Member m : lib.getMembers()) {
            if (Objects.equals(memberName, makePrettyString(m.getName()))) {
                member = m;
                return true;
            }
        }
        System.out.println("\tNo member of that name found, taking you back to the log in menu");
        return false;
    }

    // ADMIN MENU METHODS:

    // EFFECTS: displays admin menu commands
    private void displayAdminMenu() {
        System.out.println("\nLIBRARY ADMIN MENU:");
        System.out.println(REGISTER_BOOK_COMMAND + ": to register a new book to the library");
        System.out.println(VIEW_TRANSACTIONS_COMMAND + ": to view all the librarys past transactions");
        System.out.println(LOG_OUT_COMMAND + ": to log out");
    }

    // MODIFIES: this
    // EFFECTS: displays command options and processes input for admin menu to register books and view
    // the library's history of returned books
    private void adminMenu() {
        boolean logOut = false;
        while (!logOut) {
            displayAdminMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case REGISTER_BOOK_COMMAND:
                        handleRegisterBook();
                        break;
                    case VIEW_TRANSACTIONS_COMMAND:
                        handleViewTransactions();
                        break;
                    case LOG_OUT_COMMAND:
                        logOut = true;
                        break;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new book to library's collection based on inputted title, author and genre.
    // Won't accept books with titles already in the collection
    private void handleRegisterBook() {
        System.out.println("\nPlease input the title of the book:");
        if (input.hasNext()) {
            String newTitle = input.nextLine();
            // cannot register books with title "back" as it would interfere with commands during search and returns
            boolean validTitle = !makePrettyString(newTitle).equals(BACK_COMMAND);
            for (Book book : lib.getBooks()) {
                if (makePrettyString(newTitle).equals(makePrettyString(book.getTitle()))) {
                    validTitle = false;
                }
            }
            if (validTitle) {
                System.out.println("Please input the author's name");
                String author = capitalize(getUserInputString());
                System.out.println("Please input the genre to categorize this book in");
                String genre = capitalize(getUserInputString());
                lib.registerBook(new Book(newTitle, genre, author));
                System.out.println("Registering " + newTitle
                        + " by " + author + " at "
                        + lib.getName() + " under " + genre);
            } else {
                System.out.println("\tWe cannot register this book as we already have"
                        + " a book of that title, or the title is invalid");
            }
        }
    }

    // EFFECTS: displays all librarys transactions(books that have been returned)
    private void handleViewTransactions() {
        System.out.println("\n" + lib.getName() + " transaction history");
        for (Transaction transaction : lib.getTransactions()) {
            System.out.println(transaction.getMember() + " : " + transaction.getBook());
        }
    }

    // MAIN MENU METHODS:

    // EFFECTS: prints out main menu command options, as well as name of library and logged in member
    private void displayMainMenu() {
        System.out.println("\n\t\t" + lib.getName() + " Main Menu - " + member.getName());
        System.out.println(SEARCH_BOOK_COMMAND + ": search through catalogue to borrow a book");
        System.out.println(RETURN_BOOK_COMMAND + ": return books you've previously borrowed");
        System.out.println(MEMBER_PROFILE_COMMAND + ": view member profile");
        System.out.println(LOG_OUT_COMMAND + ": log out");
    }

    // MODIFIES: this
    // EFFECTS: displays and parses main menu options for members allowing user to borrow a book, return a book,
    // display info about their history, or log out
    private void mainMenu() {
        boolean logOut = false;
        while (!logOut) {
            displayMainMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case SEARCH_BOOK_COMMAND:
                        searchMenu();
                        break;
                    case RETURN_BOOK_COMMAND:
                        handleReturn();
                        break;
                    case MEMBER_PROFILE_COMMAND:
                        displayMemberInfo();
                        break;
                    case LOG_OUT_COMMAND:
                        logOut = true;
                        break;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
            }
        }
    }


    // SEARCH MENU METHODS:

    // EFFECT: displays search menu commands
    private void displaySearchMenu() {
        System.out.println("\nHow would you like our collection to be displayed?");
        System.out.println(BY_TITLE_COMMAND + ": display all titles");
        System.out.println(BY_AUTHOR_COMMAND + ": displays titles by author");
        System.out.println(BY_GENRE_COMMAND + ": displays titles by genre");
        System.out.println(BACK_COMMAND + ": return to menu");
    }

    // MODIFIES: this
    // EFFECTS: displays and parses search menu, allows users to search through books by all, genre or author
    private void searchMenu() {
        boolean back = false;
        while (!back) {
            displaySearchMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case (BY_TITLE_COMMAND):
                        allTitleSearchMenu();
                        break;
                    case (BY_AUTHOR_COMMAND):
                        authorSearchMenu();
                        break;
                    case (BY_GENRE_COMMAND):
                        genreSearchMenu();
                        break;
                    case (BACK_COMMAND):
                        back = true;
                        break;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: takes book title input from user and searches available catalogue for book, then opens books menu.
    // returns false if user inputs "back", and true otherwise.
    private boolean handleSearch() {
        System.out.println("\nTo view and borrow a book, please input it's title below:");
        System.out.println(BACK_COMMAND + ": to go back to the menu");
        Book book = null;
        String command = getUserInputString();
        if (command.equals(BACK_COMMAND)) {
            return false;
        }
        for (Book b : lib.getAvailableBooks()) {
            if (makePrettyString(b.getTitle()).equals(command)) {
                book = b;
                bookMenu(book);
            }
        }
        if (book == null) {
            System.out.println("\tTitle not found, please try another title");
        }
        return true;
    }

    // EFFECTS: displays all the titles in the library able to be borrowed, sorts them semi-alphbetically
    private void displayAllTitles() {
        System.out.println("\nTITLES:");
        if (lib.getAvailableBooks().isEmpty()) {
            System.out.println("No currently available titles, please come back later");
        }
        List<String> availableTitles = new ArrayList<String>();
        for (Book book : lib.getAvailableBooks()) {
            availableTitles.add(book.getTitle());
        }
        Collections.sort(availableTitles);
        for (String title : availableTitles) {
            System.out.println("- " + title);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays all title search menu options and allows user to select by inputting a title(calls method)
    private void allTitleSearchMenu() {
        boolean searching = true;
        while (searching) {
            displayAllTitles();
            searching = handleSearch();
        }
    }

    // EFFECTS: sorts authors with books available, and their available books, semi-alphabetically then displays them
    private void displayByAuthor() {
        System.out.println("\nBooks by author:");
        if (lib.getAvailableBooks().isEmpty()) {
            System.out.println("No currently available titles, please come back later");
        }
        List<String> availableAuthors = findAvailableAuthors();
        Collections.sort(availableAuthors);
        // finds and prints all the authors and their books(only ones not currently borrowed)
        for (String author : availableAuthors) {
            System.out.println(author);
            List<String> availableTitlesByAuthor = new ArrayList<String>();
            for (Book book : lib.getAvailableBooks()) {
                if (book.getAuthor().equals(author)) {
                    availableTitlesByAuthor.add(book.getTitle());
                }
            }
            // sort method will sort list semi alphabetically
            Collections.sort(availableTitlesByAuthor);
            for (String title : availableTitlesByAuthor) {
                System.out.println("- " + title);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays authors and their books sorted semi-alpbetically and allows user to select by inputting a title
    private void authorSearchMenu() {
        boolean searching = true;
        while (searching) {
            displayByAuthor();
            searching = handleSearch();
        }
    }

    // EFFECTS: returns a list of all the authors at lib who have books that are currently able to be borrowed
    private List<String> findAvailableAuthors() {
        List<String> availableAuthors = new ArrayList<String>();
        for (String author : lib.getAuthors()) {
            int i = 0;
            for (Book book : lib.getAvailableBooks()) {
                if (book.getAuthor().equals(author)) {
                    i++;
                }
            }
            if (i > 0) {
                availableAuthors.add(author);
            }
        }
        return availableAuthors;
    }

    // EFFECTS: sorts genres with books available, and their available books, semi-alphabetically then displays them
    private void displayByGenre() {
        System.out.println("\nBooks by genre:");
        if (lib.getAvailableBooks().isEmpty()) {
            System.out.println("No currently available titles, please come back later");
        }
        List<String> availableGenres = findAvailableGenres();
        Collections.sort(availableGenres);
        for (String genre : availableGenres) {
            System.out.println(genre);
            List<String> availableTitlesByGenre = new ArrayList<String>();
            for (Book book : lib.getAvailableBooks()) {
                if (book.getGenre().equals(genre)) {
                    availableTitlesByGenre.add(book.getTitle());
                }
            }
            Collections.sort(availableTitlesByGenre);
            for (String title : availableTitlesByGenre) {
                System.out.println("- " + title);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays genres and their books sorted semi-alpbetically and allows user to select by inputting a title
    private void genreSearchMenu() {
        boolean searching = true;
        while (searching) {
            displayByGenre();
            searching = handleSearch();
        }
    }

    // EFFECTS: returns a list of all the genres at lib who have books that are currently able to be borrowed
    private List<String> findAvailableGenres() {
        List<String> availableGenres = new ArrayList<String>();
        for (String genre : lib.getGenres()) {
            int i = 0;
            for (Book book : lib.getAvailableBooks()) {
                if (book.getGenre().equals(genre)) {
                    i++;
                }
            }
            if (i > 0) {
                availableGenres.add(genre);
            }
        }
        return availableGenres;
    }

    // BOOKS MENU METHODS:

    // EFFECTS: displays information about the book including author, genre and ratings and book menu commands
    private void displayBookMenu(Book book) {
        System.out.println("\nTitle: \"" + book.getTitle() + "\"");
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Genre: " + book.getGenre());
        if (book.getRating() > 0) {
            System.out.println("Average Rating: " + book.getRating());
        } else {
            System.out.println("No ratings/reviews have been left for this book.");
        }
        System.out.println("\n" + BORROW_BOOK_COMMAND + ": borrow this book");
        System.out.println(BACK_COMMAND + ": go back to search");
        System.out.println(VIEW_RATINGS_COMMAND + ": view reviews left for this book");
    }

    // MODIFIES: this, book
    // EFFECTS: displays book information, commands and parses input for book menu, allowing users to borrow the book,
    // view reviews or go back.
    private void bookMenu(Book book) {
        boolean viewing = true;
        String command;
        while (viewing) {
            displayBookMenu(book);
            command = getUserInputString();
            switch (command) {
                case (BACK_COMMAND):
                    viewing = false;
                    break;
                case (BORROW_BOOK_COMMAND):
                    lib.borrowBook(book, member);
                    System.out.println("Member " + member.getName() + " borrowed " + book.getTitle());
                    viewing = false;
                    break;
                case (VIEW_RATINGS_COMMAND):
                    displayReviews(book);
                    break;
                default:
                    System.out.println(UNKNOWN_COMMAND_MESSAGE);
            }
        }
    }

    // EFFECTS: displays all the reviews left for book
    private void displayReviews(Book book) {
        System.out.println("\nReviews for " + "\"" + book.getTitle() + "\"");
        if (book.getReviews().isEmpty()) {
            System.out.println("No ratings/reviews have been left for this book.");
        }
        for (Review review : book.getReviews()) {
            System.out.println(review.getMember()
                    + " gave " + review.getRating() + " out of 5 stars");
            System.out.println("\t" + "\"" + review.getComment() + "\"");
        }
    }

    // RETURN/REVIEW METHODS:

    // MODIFIES: this
    // EFFECTS: displays users currently borrowed books and allows user to input a title to return it
    private void handleReturn() {
        displayBorrowedBooks();
        System.out.println("\nPlease input the title of the book you'd like to return, or go back");
        String title = getUserInputString();
        Book bookReturned = null;
        boolean returnedABook = false;
        for (Book book : member.getBorrowedBooks()) {
            if (makePrettyString(book.getTitle()).equals(title)) {
                bookReturned = book;
                returnedABook = true;
            }
        }
        if (returnedABook) {
            lib.returnBook(bookReturned, member);
            System.out.println("Returned " + bookReturned.getTitle());
            handleReview(bookReturned);
        } else {
            System.out.println("\tYou are not currently borrowing a book of that title!");
        }
    }

    // MODIFIES: this, book
    // EFFECTS: handles review process after user returns a book, asks for rating from 1-5(integer) and comment, or
    // cancel to cancel review.
    private void handleReview(Book book) {
        boolean cancel = false;
        while (!cancel) {
            System.out.println("\nPlease enter a rating between 1 to 5 for this book");
            System.out.println(CANCEL_COMMAND + ": cancel this review");
            String command = getUserInputString();
            // checks that input is one digit from 1-5
            if (command.matches("[1-5]")) {
                System.out.println("Write a comment");
                String comment = input.nextLine();
                int rating = Integer.parseInt(command);
                member.leaveReview(book, new Review(book, member, rating, comment));
                System.out.println("Review added. Thank you for your rating.");
                cancel = true;
            } else if (command.equals(CANCEL_COMMAND)) {
                cancel = true;
            } else {
                System.out.println("\tInvalid input. Please input a digit from 1 to 5, or cancel this review process");
            }
        }
    }


    // MEMBER INFO METHODS:

    // EFFECTS: displays transaction history and currently borrowed books of member
    private void displayMemberInfo() {
        if (member.isBorrowingBooks()) {
            displayBorrowedBooks();
        } else {
            System.out.println("\n" + member.getName() + " is not currently borrowing any books.");
        }
        if (!member.getTransactions().isEmpty()) {
            System.out.println("Returned books");
            for (Transaction transaction : member.getTransactions()) {
                System.out.println("- " + transaction.getBook());
            }
        } else {
            System.out.println("No returned books to display.");
        }
    }

    // EFFECTS: prints out members currently borrowed books
    private void displayBorrowedBooks() {
        System.out.println("\n" + member.getName() + "'s currently borrowed books:");
        for (Book book : member.getBorrowedBooks()) {
            System.out.println("- " + book.getTitle());
        }
    }

    // PERSISTENCE METHODS(SAVING AND LOADING)

    // EFFECTS: handles menu for loading library from a file, or choosing a default starting library. or a new empty one
    private Library loadMenu() {
        while (true) {
            Library loadedLibrary = null;
            displayLoadMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case NEW_LIBRARY_COMMAND:
                        loadedLibrary = handleNewLibrary(false);
                        break;
                    case LOAD_LIBRARY_COMMAND:
                        loadedLibrary = handleLoadLibrary();
                        break;
                    case DEFAULT_LIBRARY_COMMAND:
                        loadedLibrary = handleNewLibrary(true);
                        break;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
                if (!(loadedLibrary == null)) {
                    return loadedLibrary;
                }
            }
        }
    }

    // EFFECTS: displays load menu with instructions
    private void displayLoadMenu() {
        System.out.println("\nWelcome to the library app!");
        System.out.println(NEW_LIBRARY_COMMAND + ": name and create a new empty library");
        System.out.println(LOAD_LIBRARY_COMMAND + ": load an exisiting saved library");
        System.out.println(DEFAULT_LIBRARY_COMMAND + ": name and then load a library with some books and members");
    }

    // EFFECTS: handles the loading of the library from saves in data/user.
    private Library handleLoadLibrary() {
        System.out.println("\nPlease enter name of library to load from list below");
        File userSaves = new File("./data/user");
        String[] contents = userSaves.list();
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            fileNames.add(contents[i]);
        }
        while (true) {
            for (String fileName : fileNames) {
                System.out.println("\t " + fileName.replaceAll(".json", ""));
            }
            if (input.hasNext()) {
                String loadLibrary = input.nextLine();
                try {
                    JsonReader reader = new JsonReader("./data/user/" + loadLibrary + ".json");
                    Library lib = reader.read();
                    System.out.println("\nLoading library... ");
                    return lib;
                } catch (IOException e) {
                    System.out.println("\nNo such library exists, please input library from the list below.");
                }
            }

        }
    }

    // EFFECTS: prompts user for name, if defaultSetUp, creates a library with some books and members
    // else, creates an empty library.
    private Library handleNewLibrary(boolean defaultSetUp) {
        System.out.println("\nPlease enter the name of the library!");
        File userSaves = new File("./data/user");
        String[] contents = userSaves.list();
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            fileNames.add(makePrettyString(contents[i]));
        }
        while (true) {
            if (input.hasNext()) {
                String newLibrary = input.nextLine();
                if (!fileNames.contains(newLibrary + ".json")) {
                    System.out.println("\nCreating new library " + newLibrary);
                    if (defaultSetUp) {
                        return LibraryInitializer.defaultLibrary(newLibrary);
                    } else {
                        return new Library(newLibrary, new ArrayList<>());
                    }
                }
                System.out.println("\nThere already exists a library of that name, please input a different name.");

            }
        }
    }


    // EFFECTS: prompts user to decide if they would like to save, and processes save.
    private void handleSave() {
        boolean run = true;
        while (run) {
            System.out.println("Would you like to save?");
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case SAVE_COMMAND:
                        saveLibrary();
                        run = false;
                        break;
                    case DONT_SAVE_COMMAND:
                        run = false;
                        break;
                    default:
                        System.out.println(UNKNOWN_COMMAND_MESSAGE);
                }
            }
        }

    }

    // EFFECTS: writes library to JSON and saves in data/user/LIBRARYNAME.json, where LIBRARYNAME is the libraries name.
    // Overwrites any previously saved librarys with this name.
    private void saveLibrary() {
        JsonWriter writer = new JsonWriter("./data/user/" + lib.getName() + ".json");
        try {
            writer.open();
            writer.write(lib);
            writer.close();
            System.out.println("Saved.");
        } catch (IOException e) {
            System.err.println("ERROR: COULDN'T SAVE");
        }
    }

    // HELPER METHODS:


    // EFFECTS: gets next user input and makes it pretty
    private String getUserInputString() {
        String str = "";
        if (input.hasNext()) {
            str = input.nextLine();
            str = makePrettyString(str);
        }
        return str;
    }

    // MODIFIES: s
    // EFFECTS: removes white space and quotation marks around s
    // from fitLifeGym UI
    private String makePrettyString(String s) {
        s = s.toLowerCase();
        s = s.trim();
        s = s.replaceAll("\"|'", "");
        return s;
    }

    // MODIFIES: str(?)
    // EFFECTS: capitalizes the first letter of every word
    // from source programwiz(source at top)
    private String capitalize(String str) {
        char[] charArray = str.toCharArray();
        boolean foundSpace = true;
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            } else {
                foundSpace = true;
            }
        }
        return String.valueOf(charArray);
    }


}
