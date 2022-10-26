package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewTest {


    @Test
    public void testReview() {
        Member m1 = new Member("J. Gould");
        Book b1 = new Book("Le petit prince", "Foreign Language", "Antoine de Saint-Exupéry");
        Review r1 = new Review(b1, m1, 4, "Pretty good read");
        assertEquals(r1.getBook(), b1.getTitle());
        assertEquals(r1.getMember(), m1.getName());
        assertEquals(r1.getComment(),"Pretty good read");
        assertEquals(r1.getRating(), 4);

    }

    @Test
    public void testReviewSecondConstructor() {
        Member m1 = new Member("J. Gould");
        Book b1 = new Book("Le petit prince", "Foreign Language", "Antoine de Saint-Exupéry");
        Review r1 = new Review(b1.getTitle(), m1.getName(), 4, "Pretty good read");
        assertEquals(r1.getBook(), b1.getTitle());
        assertEquals(r1.getMember(), m1.getName());
        assertEquals(r1.getComment(),"Pretty good read");
        assertEquals(r1.getRating(), 4);

    }
}
