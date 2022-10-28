package model;

import org.json.JSONObject;

public class Review {
    // REVIEW: A book review with a star rating from 1-5, a comment,
    // and the name of the member who left the review and the title of the book it is about.


    private final String bookTitle;
    private final String member;
    private final int rating;
    private final String comment;

    // REQUIRES: rating is in interval [1,5]
    // EFFECTS: creates a review for book from member with rating and comment
    public Review(Book book, Member member, int rating, String comment) {
        this.bookTitle = book.getTitle();
        this.member = member.getName();
        this.rating = rating;
        this.comment = comment;
    }

    // REQUIRES: rating is in interval [1,5]
    // EFFECTS: creates a review for book from member with rating and comment
    public Review(String bookTitle, String memberName, int rating, String comment) {
        this.bookTitle = bookTitle;
        this.member = memberName;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getMember() {
        return member;
    }

    public String getBook() {
        return bookTitle;
    }


    // EFFECTS: returns review as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("book", this.bookTitle);
        json.put("member", this.member);
        json.put("rating", this.rating);
        json.put("comment", this.comment);
        return json;
    }
}

