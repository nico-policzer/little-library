package ui;

import model.*;

import java.util.*;

// SOURCES:
// Used to create a helper method to capitilze the start of each word in a string
// - https://www.programiz.com/java-programming/examples/capitalize-first-character-of-string


public class LibraryApp {

    private Library lib;
    private Member member;
    private Scanner input;

    private static final String MEMBER_PROFILE_COMMAND = "profile";
    private static final String RETURN_BOOK_COMMAND = "return";
    private static final String SEARCH_BOOK_COMMAND = "search";
    private static final String QUIT_COMMAND = "quit";
    private static final String LOG_OUT_COMMAND = "log out";

    private static final String BY_AUTHOR_COMMAND = "author";
    private static final String BY_TITLE_COMMAND = "all";
    private static final String BY_GENRE_COMMAND = "genre";
    private static final String BACK_COMMAND = "back";

    private static final String REGISTER_BOOK_COMMAND = "register book";
    private static final String VIEW_TRANSACTIONS_COMMAND = "transactions";
    private static final String BORROW_BOOK_COMMAND = "borrow";
    private static final String VIEW_RATINGS_COMMAND = "ratings";

    private static final String REGISTER_MEMBER_COMMAND = "register";
    private static final String SIGN_IN_COMMAND = "sign in";




    // TODO: organize code so it is organized by menu
    // TODO: complete CLEAR specifications for all methods
    // EFFECTS: runs library app
    public LibraryApp() {
        runLibrary();
    }



    // EFFECTS: runs library and process input
    private void runLibrary() {

        boolean run = true;
        initializeLibrary();
        while (run) {
            //displaysWelcomeMenu();
            run =  handleLogOn();
            if (!run) {
             // skips to end
            } else if (member.isAdmin()) {
                adminMenu();
            } else {
                mainMenu();
            }
        }
        end();
    }


    // EFFECTS: runs sign in menu allowing users to register as a new member or sign in as previous members, or quit
    // and stop the program. returns true if member signs in and false if they quit
    private boolean handleLogOn() {
        boolean quit = false;
        while (!quit) {
            displaysWelcomeMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case REGISTER_MEMBER_COMMAND:
                        if (handleNewMember()) {
                            return true;
                        }
                        break;
                    case SIGN_IN_COMMAND:
                        if (handleSignIn()) {
                            return true;
                        }
                        break;
                    case QUIT_COMMAND:
                        quit = true;
                        break;
                }
            }
        }
        return false;
    }

    private void end() {
        System.out.println("Thanks for visiting!");
        input.close();
    }

    private void displaysWelcomeMenu() {
        clear();
        System.out.println("\nWelcome to " + lib.getName());
        System.out.println(REGISTER_MEMBER_COMMAND + ": register a new member");
        System.out.println(SIGN_IN_COMMAND + ": sign in as returning member");
        System.out.println(QUIT_COMMAND + ": quit");

    }


    // EFFECTS: displays librarys current members, and asks user to input name of member to sign in
    private boolean handleSignIn() {
        displayMembers();
        String memberName = input.nextLine();
        for (Member m: lib.getMembers()) {
            if (Objects.equals(memberName, m.getName())) {
                member = m;
                return true;
            }
        }
        System.out.println("no member of that name found, taking you back to the log in menu");
        return false;
    }


    // EFFECTS: displays currently registered members and prompts sign in
    private void displayMembers() {
        System.out.println("Registered members:");
        for (Member m : lib.getMembers()) {
            System.out.println(m.getName());
        }
        System.out.println("please enter member name to sign in:");
    }


    // EFFECTS:
    private boolean handleNewMember() {
        System.out.println("Please enter your name to register:");
        String memberName = input.nextLine();
        Member registeredMember =  new Member(memberName);
        if (lib.registerMember(registeredMember)) {
            System.out.println("Registering " + memberName + " to " + lib.getName());
            member = registeredMember;
            return true;
        } else {
            System.out.println("There is already a member of that name, please use a different name");
            return false;
        }
    }



    // EFFECTS: displays and processes input for admin menu where admins can register books and view
    // the library's history
    private void adminMenu() {
        clear();
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
                }
            }
        }
    }

    // TODO: finish register book handling. should ask for title(needs to check against all
    //  other titles in PRETTY STRING, title doesn't need to be in any specific format, just can't be the
    //  same as other titles). Then ask for author(use capitalize method for name), then genre(also capitilze)
    // EFFECTS: registers a new book to library's collection. Won't accept books with titles already in the collection
    private void handleRegisterBook() {
        System.out.println("Please input the title of the book:");
        if (input.hasNext()) {
            String title = input.nextLine();
        }
    }

    private void handleViewTransactions() {
        System.out.println(lib.getName() + " transaction history");
        for (Transaction transaction: lib.getTransactions()) {
            System.out.println(transaction.getMember().getName() + " : " + transaction.getBook().getTitle());
        }
    }
    private void displayAdminMenu() {
        System.out.println("LIBRARY ADMIN MENU:");
        System.out.println(REGISTER_MEMBER_COMMAND + ": to register a new book to the library");
        System.out.println(VIEW_TRANSACTIONS_COMMAND + ": to view all the librarys past transactions");
        System.out.println(LOG_OUT_COMMAND + ": to log out");
    }

    // EFFECTS: displays currently borrowed books and allowed to choose one to return
    private void returnBook() {
        System.out.println("Return book menu!");
    }


    // EFFECTS: runs book searching process, allows users to search through books by genre or author, choose a book by
    // title to view info about and decide to borrow or not
    private void searchMenu() {
        boolean back = false;
        while (!back) {
            displaySearchMenu();
            String command = getUserInputString();
            if (command.length() > 0) {
                switch (command) {
                    case (BY_TITLE_COMMAND):
                        handleAllTitleSearch();
                        break;
                    case (BY_AUTHOR_COMMAND):
                        handleByAuthorSearch();
                        break;
                    case (BY_GENRE_COMMAND):
                        handleByGenreSearch();
                        break;
                    case (BACK_COMMAND):
                        back = true;
                }
            }
        }
    }

    // EFFECTS: displays all titles and allows user to input
    private void handleAllTitleSearch() {
        boolean searching = true;
        while (searching) {
            displaysAllTitles();
            searching = handleSearch();
        }
    }


    // EFFECTS: displays all the titles in the library able to be borrowed, sorted alphabetically
    private void displaysAllTitles() {
        System.out.println("TITLES:");
        List<String> availableTitles = new ArrayList<String>();
        for (Book book: lib.getAvailableBooks()) {
            availableTitles.add(book.getTitle());
        }
        Collections.sort(availableTitles);
        for (String title: availableTitles) {
            System.out.println("- " + title);
        }
    }

    // EFFECTS: takes book title input from user and searches available catalogue for book, then directs user
    // to info about book
    private boolean handleSearch() {
        System.out.println("To view and borrow a book, please input it's title below:");
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
        return true;
    }

    private void handleByAuthorSearch() {
        boolean searching = true;
        while (searching) {
            displaysByAuthor();
            searching = handleSearch();
        }
    }

    private void displaysByAuthor() {

        System.out.println("Authors with books:");
        List<String> availableAuthors = findAvailableAuthors();
        Collections.sort(availableAuthors);

        for (String author: availableAuthors) {
            System.out.println(author);
            List<String> availableTitlesByAuthor = new ArrayList<String>();
            for (Book book: lib.getAvailableBooks()) {
                if (book.getAuthor().equals(author)) {
                    availableTitlesByAuthor.add(book.getTitle());
                }
            }
            Collections.sort(availableTitlesByAuthor);
            for (String title: availableTitlesByAuthor) {
                System.out.println("- " + title);
            }
        }

    }

    private List<String> findAvailableAuthors() {
        List<String> availableAuthors = new ArrayList<String>();
        for (String author: lib.getAuthors()) {
            int i = 0;
            for (Book book: lib.getAvailableBooks()) {
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



    private void handleByGenreSearch() {
        boolean searching = true;
        while (searching) {
            displaysByGenre();
            searching = handleSearch();
        }
    }


    private void displaysByGenre() {
        System.out.println("Genres with books:");
        List<String> availableGenres = findAvailableGenres();
        Collections.sort(availableGenres);
        for (String genre: availableGenres) {
            System.out.println(genre);
            List<String> availableTitlesByGenre = new ArrayList<String>();
            for (Book book: lib.getAvailableBooks()) {
                if (book.getGenre().equals(genre)) {
                    availableTitlesByGenre.add(book.getTitle());
                }
            }
            Collections.sort(availableTitlesByGenre);
            for (String title: availableTitlesByGenre) {
                System.out.println("- " + title);
            }
        }

    }

    private List<String> findAvailableGenres() {
        List<String> availableGenres = new ArrayList<String>();
        for (String genre: lib.getGenres()) {
            int i = 0;
            for (Book book: lib.getAvailableBooks()) {
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


    private void displaySearchMenu() {
        System.out.println("How would you like our collection to be displayed?");
        System.out.println(BY_TITLE_COMMAND + ": display all titles");
        System.out.println(BY_AUTHOR_COMMAND + ": displays titles by author");
        System.out.println(BY_GENRE_COMMAND + ": displays titles by genre");
        System.out.println(BACK_COMMAND + ": return to menu");
    }

    // EFFECTS: displays ratings, reviews, and information about book and lets user decide to borrow the book or
    // not, or go back
    private void bookMenu(Book book) {
        boolean viewing = true;
        String command;
        while (viewing) {
            displayBookMenu(book);
            command =  getUserInputString();
            switch (command) {
                case(BACK_COMMAND):
                    viewing = false;
                    break;
                case(BORROW_BOOK_COMMAND):
                    lib.borrowBook(book, member);
                    System.out.println("Member " + member.getName() + " borrowed " + book.getTitle());
                    viewing = false;
                    break;
                case(VIEW_RATINGS_COMMAND):
                    displayReviews(book);
                    break;
            }
        }
    }

    // EFFECTS: displays information about the book including author, genre and rating/reviews,
    // and prompts user for input
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

    // EFFECTS: displays all the reviews for book
    private void displayReviews(Book book) {
        System.out.println("\nReviews for " + "\"" + book.getTitle() + "\"");
        if (book.getReviews().isEmpty()) {
            System.out.println("No ratings/reviews have been left for this book.");
        }
        for (Review review: book.getReviews()) {
            System.out.println(review.getMember().getName()
                    + " gave " + review.getRating() + " out of 5 stars");
            System.out.println("\t" + "\"" + review.getComment() + "\"");
        }
    }

    // EFFECTS: displays transaction history and currently borrowed books of member
    private void infoMenu() {
        if (member.isBorrowingBooks()) {
            System.out.println(member.getName() + "'s currently borrowed books:");
            for (Book book: member.getBorrowedBooks()) {
                System.out.println("- " + book.getTitle());
            }
        } else {
            System.out.println(member.getName() + " is not currently borrowing any books");
        }
        System.out.println("Returned books");
        for (Transaction transaction: member.getTransactions()) {
            System.out.println("- " + transaction.getBook().getTitle());
        }
    }

    // TODO: make all the spacing between menus the same(use clear or just one space)

    // EFFECTS: displays and processes main menu options for members allowing user to borrow a book, return a book,
    // display info about their history, or quit back to sign in
    private void mainMenu() {
        clear();
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
                        returnBook();
                        break;
                    case MEMBER_PROFILE_COMMAND:
                        infoMenu();
                        break;
                    case LOG_OUT_COMMAND:
                        logOut = true;
                        break;
                }
            }
        }
    }

    // EFFECTS: prints out main menu options
    private void displayMainMenu() {
        System.out.println(lib.getName());
        System.out.println("\nlogged in as: " + member.getName());
        System.out.println(SEARCH_BOOK_COMMAND + ": search through catalogue to borrow a book");
        System.out.println(RETURN_BOOK_COMMAND + ": seturn books you've previously borrowed");
        System.out.println(MEMBER_PROFILE_COMMAND + ": view member profile");
        System.out.println(LOG_OUT_COMMAND + ": log out");
    }


    private void initializeLibrary() {
        Book b1 = new Book("Art of War", "Classic", "Sun Tzu");
        Book b2 = new Book("The Blank Slate", "Science", "Steven Pinker");
        Book b3 = new Book("A tale of Two Cities", "Classic", "Charles Dickens");
        Book b4 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        Book b5 = new Book("Stalingrad", "War", "Antony Beevor");
        Book b6 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");
        Book b7 = new Book("Crime and Punishment", "Classic", "Fyodor Dostoevsky");
        Book b8 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");

        Member m1 = new Member("Georges Vanier");
        Member m2 = new Member("H.P Deeves");
        Member m3 = new Member("m");

        List<Book> initialBooks = new ArrayList<Book>(Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8));
        lib = new Library("Nico's Library", initialBooks);
        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);

        lib.borrowBook(b8,m3);
        lib.returnBook(b8,m3);
        b1.addReview(new Review(b1, m1, 4, "I loved this book. But it also sucks..."));
        b1.addReview(new Review(b1, m2, 1, "I HATE THIS BOOK"));
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }



    //EFFECTS: removes white space and quotation marks around s
    private String makePrettyString(String s) {
        s = s.toLowerCase();
        s = s.trim();
        s = s.replaceAll("\"|'", "");
        return s;
    }

    private String getUserInputString() {
        String str = "";
        if (input.hasNext()) {
            str = input.nextLine();
            str = makePrettyString(str);
        }
        return str;
    }

    private void clear() {
        for (int i = 0; i < 10; ++i) {
            System.out.println();
        }
    }

    // FROM (source1)
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
