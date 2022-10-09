package model;

import java.util.List;

public class Library {
    // A library with a name,  with a collection of books,
    // a list of members,the ability to borrow books, donate books, return books


    // EFFECTS: creates a library with a name and list of books
    public Library(String name, List<Book> books) {

    }

    // MODIFIES: this
    // EFFECTS: adds book to librarys collection
    public void donateBook(Book book) {

    }

    // REQUIRES: book is able to be borrowed
    // EFFECTS: Member borrows book from library
    public void borrowBook(Book book, Member member) {

    }

    public List<Book> getBooks() {
        return null;
    }

    public List<Member> getMembers() {
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
