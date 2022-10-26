package model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    // MEMBER: a member at the library with name, transaction and review history,
    // and information on the books they are currently borrowing, also able to borrow and return books.


    protected final String name;
    private final List<Book> borrowedBooks;
    private final List<Transaction> transactions;
    private final List<Review> reviews;

    // EFFECTS: creates a member of name name with no books being borrowed and no transaction or review history
    public Member(String name) {
        this.name = name;
        borrowedBooks = new ArrayList<Book>();
        transactions = new ArrayList<Transaction>();
        reviews = new ArrayList<Review>();
    }


    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds transaction to members transactions
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);

    }

    // EFFECTS: returns the transaction history of the member, ending with the most recent
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Boolean isBorrowingBooks() {
        return (borrowedBooks.size() != 0);
    }

    // EFFECTS: returns list of books currently being borrowed by member
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // EFFECTS: returns list of all reviews left by member
    public List<Review> getReviews() {
        return reviews;
    }

    // REQUIRES: book is in library and is able to be borrowed
    // MODIFIES: this
    // EFFECTS: adds book to members currently borrowed books
    public void borrowBook(Book book) {
        borrowedBooks.add(book);

    }

    // REQUIRES: book must have been previously borrowed by member and be returned by library simultaneously
    // MODIFIES: this
    // EFFECTS: removes currently borrowed book from members currently borrowed books
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    // MODIFIES: this, book
    // EFFECTS: leaves a review on book that was just returned by member, adds to members review history
    public void leaveReview(Book book, Review review) {
        reviews.add(review);
        book.addReview(review);
    }

    // EFFECTS: returns false to reflect that the member is not an admin member
    public boolean isAdmin() {
        return false;
    }


}
