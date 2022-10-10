package model;

import java.util.List;

public class Library {
    // A library with a name,  with a collection of books,
    // the ability to borrow books, donate books, return books


    // EFFECTS: creates a library with a name and list of books
    public Library(String name, List<Book> books) {

    }

    // MODIFIES: this
    // EFFECTS: adds book to librarys collection
    public void donateBook(Book book) {

    }

    // REQUIRES: book is able to be borrowed
    // MODIFIES: this, book, member
    // EFFECTS: sets book to being borrowed, adds to members list of borrowed books
    public void borrowBook(Book book, Member member) {

    }

    // REQUIRES: book has been previously borrowed by member
    // MODIFIES: this, book, member
    // EFFECTS: sets book to not being borrowed, removes from members borrowed list and adds transaction to member
    public void returnBook(Book book, Member member) {

    }

    public List<Book> getBooks() {
        return null;
    }


    // EFFECTS: returns a list of all the genres of books in the library
    public List<String> getGenres() {
        return null;
    }

    //EFFECTS: returns a list of all the authors of books in the library
    public List<String> getAuthors() {
        return null;
    }

    // MODIFIES: this
    // EFFECTS: registers member to library, returns true if no other member of same name exists, else false
    public Boolean registerMember(Member member) {
        return false;
    }


}
