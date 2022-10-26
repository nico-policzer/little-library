package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book b1;
    private Book b2;
    private Book b3;
    private Review r1;
    private Review r2;
    private Review r3;



    @BeforeEach
    public void setUp() {
        b1 = new Book("The Lord of the Rings", "Fantasy", "J.R.R Tolkien");

        b2 = new Book("The Wars", "War", "Timothy Findley");
        r1 = new Review(b2, new Member("James Johness"), 2, "Alright read, could be better");
        b2.addReview(r1);

        b3 = new Book("King Leopolds Ghost", "History", "Adam Hochschild");
        r2 = new Review(b3, new Member("Piper Peterson"), 4, "Loved it!");
        r3 = new Review(b3, new Member("John Lemmon"), 1, "I hate this book");

        b3.addReview(r2);
        b3.addReview(r3);


    }
    @Test
    public void testBook() {
        assertEquals(b1.getAuthor(), "J.R.R Tolkien");
        assertEquals(b1.getGenre(),"Fantasy");
        assertEquals(b1.getTitle(), "The Lord of the Rings");
        assertFalse(b1.isBorrowed());

        assertEquals(b2.getAuthor(), "Timothy Findley");
        assertEquals(b2.getGenre(),"War");
        assertEquals(b2.getTitle(), "The Wars");
        assertFalse(b2.isBorrowed());
    }
    @Test
    public void testBookFullConstructor() {
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(r1);
        reviews.add(r2);
        Book b0 = new Book("King Leopolds Ghost", "History", "Adam Hochschild",
                true, reviews);
        assertEquals(b0.getAuthor(), "Adam Hochschild");
        assertEquals(b0.getGenre(),"History");
        assertEquals(b0.getTitle(), "King Leopolds Ghost");
        assertTrue(b0.isBorrowed());
        assertEquals(b0.getReviews(),reviews);
    }

    @Test
    public void testBorrowBook() {
        assertFalse(b1.isBorrowed());
        assertFalse(b2.isBorrowed());

        b1.borrowBook();
        b2.borrowBook();

        assertTrue(b1.isBorrowed());
        assertTrue(b2.isBorrowed());
    }

    @Test
    public void testReturnBook() {
        b1.borrowBook();
        b2.borrowBook();

        b1.returnBook();
        b2.returnBook();

        assertFalse(b1.isBorrowed());
        assertFalse(b2.isBorrowed());
    }

    @Test
    public void testAddReview() {
        assertEquals(b1.getReviews().size(),0);
        assertEquals(b2.getReviews().size(),1);
        assertEquals(b3.getReviews().size(),2);

        assertTrue(b2.getReviews().contains(r1));

        assertTrue(b3.getReviews().contains(r2));
        assertTrue(b3.getReviews().contains(r3));
    }

    @Test
    public void testGetRating() {
        assertEquals(b1.getRating(), 0);
        assertEquals(b2.getRating(),2);
        assertEquals(b3.getRating(),(1+4)/2);
    }
    @Test
    public void testGetRatingAddNewReview() {
        Review r4 = new Review(b3, new Member("Paul Peterson"), 3, "DECENT... lol");
        b3.addReview(r4);

        assertEquals(b3.getRating(), (1+4+3)/3);
    }






}
