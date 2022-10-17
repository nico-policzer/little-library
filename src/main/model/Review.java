package model;

public class Review {
    // REVIEW: A book review with a star rating from 1-5, a comment,
    // and the member who left the review and the book it is about.


    private final Book book;
    private final Member member;
    private final int rating;
    private final String comment;

    // REQUIRES: rating is in interval [1,5]
    // EFFECTS: creates a review for book from member with rating and comment
    public Review(Book book, Member member, int rating, String comment) {
        this.book = book;
        this.member = member;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }
}

