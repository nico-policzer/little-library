package model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    // BOOK: A book with a title, genre, author, reviews, average rating
    // and information about whether it is being borrowed or not
    private final String genre;
    private final String author;
    private final String title;
    private final List<Review> reviews;
    private Boolean isBorrowed;

    // EFFECTS: creates a book with title by author of genre
    public Book(String title, String genre, String author) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        reviews = new ArrayList<Review>();
        isBorrowed = false;

    }

    public Book(String title, String genre, String author, Boolean isBorrowed, List<Review> reviews) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.reviews = reviews;
        this.isBorrowed = isBorrowed;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    // EFFECTS: return true if book is currently being borrowed
    public Boolean isBorrowed() {
        return isBorrowed;
    }

    // REQUIRES: book is not being borrowed
    // EFFECTS: sets the book as currently being borrowed
    public void borrowBook() {
        isBorrowed = true;

    }

    // REQUIRES: book is being borrowed
    // EFFECTS: returns book, now not being borrowed
    public void returnBook() {
        isBorrowed = false;
    }

    // EFFECTS: returns all reviews previously left on book
    public List<Review> getReviews() {
        return reviews;
    }

    // EFFECTS: adds review to reviews of book
    public void addReview(Review review) {
        reviews.add(review);
    }

    // EFFECTS: returns a average star rating [1,5] from reviews left, if no ratings then returns 0
    public int getRating() {
        if (reviews.size() == 0) {
            return 0;
        }
        int rating = 0;
        for (Review review : reviews) {
            rating += review.getRating();
        }
        return rating / reviews.size();
    }
}
