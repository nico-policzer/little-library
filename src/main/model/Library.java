package model;

import java.util.ArrayList;
import java.util.List;

public class Library {
    // A library with a name,  with a collection of books,
    // the ability to borrow books, donate books, return books
    private List<Book> books;
    private List<Member> members;
    private String name;

    // EFFECTS: creates a library with a name and list of books
    public Library(String name, List<Book> books) {
        this.books = books;
        this.name = name;
        members = new ArrayList<Member>();
    }

    // MODIFIES: this
    // EFFECTS: adds book to librarys collection
    public void donateBook(Book book) {
        books.add(book);
    }

    // REQUIRES: book is able to be borrowed, member is registered in library
    // MODIFIES: this, book, member
    // EFFECTS: sets book to being borrowed, adds to members list of borrowed books
    public void borrowBook(Book book, Member member) {
        book.borrowBook();
        member.borrowBook(book);
    }

    // REQUIRES: book has been previously borrowed by member, member is registered in library
    // MODIFIES: this, book, member
    // EFFECTS: sets book to not being borrowed, removes from members borrowed list and adds transaction to member
    public void returnBook(Book book, Member member) {
        book.returnBook();
        member.returnBook(book);
        member.addTransaction(new Transaction(book, member));
    }

    public List<Book> getBooks() {
        return books;
    }


    // EFFECTS: returns a list of all the genres of books in the library
    public List<String> getGenres() {
        List<String> genres = new ArrayList<String>();
        for (Book book: books) {
            if (!genres.contains(book.getGenre())) {
                genres.add(book.getGenre());
            }
        }
        return genres;
    }


    //EFFECTS: returns a list of all the authors of books in the library
    public List<String> getAuthors() {
        List<String> authors = new ArrayList<String>();
        for (Book book: books) {
            if (!authors.contains(book.getAuthor())) {
                authors.add(book.getAuthor());
            }
        }
        return authors;
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: returns true and registers member to library if no other member of same name exists, else returns false
    public Boolean registerMember(Member newMember) {
        for (Member m: members) {
            if (m.getName() == newMember.getName()) {
                return false;
            }
        }
        members.add(newMember);
        return true;
    }

    public List<Member> getMembers() {
        return members;
    }


}
