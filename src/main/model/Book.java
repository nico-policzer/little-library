package model;

import java.util.List;

public class Book {
    // A book with a title, genre, author, reviews, average rating
    // and information about whether it is being borrowed or not

    public Book(String title, String genre, String author) {

    }

    public String getTitle() {
        return null;
    }

    public String getAuthor() {
        return null;
    }

    public String getGenre() {
        return null;
    }

    // EFFECTS: return true is book is currently being borrowed
    public Boolean isBorrowed() {
        return false;
    }


    // EFFECTS: sets the book as currently being borrowed
    public void borrowBook() {

    }

    // EFFECTS: returns book, now not being borrowed
    public void returnBook() {

    }

    // EFFECTS: returns all reviews previously left on book
    public List<Review> getReviews() {
        return null;
    }

    // EFFECTS: adds review to reviews of book
    public void addReview(Review review) {

    }

    // EFFECTS: returns a average star rating [1,5] from reviews left
    public int getRating() {
        return 0;
    }
}
