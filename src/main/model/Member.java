package model;

import java.util.List;

public class Member {
    // MEMBER: a member at the library with name, transaction and review history,
    // and information on whether they are borrowing books or not, also able to borrow and return books.

    // EFFECTS: creates a member of name name
    public Member(String name){

    }


    public String getName() {
        return null;
    }

    // EFFECTS: returns the transaction history of the member, ending with the most recent

    public List<Transaction> getTransactions() {
        return null;
    }

    public Boolean isBorrowingBooks() {
        return false;
    }
    // EFFECTS: returns list of books currently being borrowed by member

    public List<Book> getBorrowedBooks() {
        return null;
    }

    // EFFECTS: returns list of all reviews left by member

    public List<Review> getReviews() {
        return null;
    }

    // REQUIRES: book is in library and is able to be borrowed
    // MODIFIES: this
    // EFFECTS: member borrows book
    public void borrowBook(Book book) {

    }

    // REQUIRES: book must have been previously borrowed by member
    // MODIFIES: this
    // EFFECTS: returns previously borrowed book to library, and records in a transaction
    public Boolean returnBook(Book book) {
        return false;
    }

    // MODIFIES: this, book
    // EFFECTS: leaves a review on book that was just returned, add
    public void leaveReview(Book book) {

    }


}
