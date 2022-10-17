package model;

public class Transaction {
    // TRANSACTION: A transaction, recording a member who returned a book
    private final Book book;
    private final Member member;

    // REQUIRES: book must be returned
    // EFFECTS: creates a transaction recording member who return book
    public Transaction(Book book, Member member) {
        this.book = book;
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }
}

