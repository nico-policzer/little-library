package model;

public class Transaction {
    // TRANSACTION: A transaction, recording a member who returned a book
    private final String bookTitle;
    private final String memberName;

    // REQUIRES: book must be returned
    // EFFECTS: creates a transaction recording member who return book
    public Transaction(String title, String memberName) {
        bookTitle = title;
        this.memberName = memberName;
    }

    public String getBook() {
        return bookTitle;
    }

    public String getMember() {
        return memberName;
    }
}

