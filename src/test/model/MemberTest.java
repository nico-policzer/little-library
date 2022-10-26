package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {
    private Member m1;
    private Member m2;
    private Member m3;
    private Member m4;
    private Book b1;
    private Book b2;
    private Transaction t1;
    private Transaction t2;
    private Review r1;
    private Review r2;
    private Review r3;

    @BeforeEach
    public void setUp() {
        m1 = new Member("James Curtis");
        m2 = new Member("Jimmy Kimmel");
        m3 = new Member("Aunt Tim");
        m4 = new Member("J. Jonah Jameson");

        b1 = new Book("The Orenda", "History", "Joseph Boyden");
        b2 = new Book("The Computer and the Brain", "Science", "von Neumann");

        t1 = new Transaction(b1.getTitle(), m3.getName());
        t2 = new Transaction(b2.getTitle(), m3.getName());

        r1 = new Review(b1, m3, 4, "Not a bedtime read.");
        r2 = new Review(b2, m3, 5, "Mind blowing");
        r3 = new Review(b1, m4, 3, "Liked it. That's all.");
    }
    @Test
    public void testMember() {
        assertFalse(m1.isAdmin());
        assertFalse(m2.isAdmin());

        assertEquals(m1.getName(), "James Curtis");
        assertEquals(m2.getName(), "Jimmy Kimmel");

        assertFalse(m1.isBorrowingBooks());
        assertFalse(m2.isBorrowingBooks());

        assertEquals(m1.getReviews().size(), 0);
        assertEquals(m2.getReviews().size(), 0);

        assertEquals(m1.getTransactions().size(),0);
        assertEquals(m2.getTransactions().size(),0);

        assertEquals(m1.getBorrowedBooks().size(), 0);
        assertEquals(m2.getBorrowedBooks().size(),0);

    }

    @Test
    public void testAddTransaction() {
        m3.addTransaction(t1);
        assertEquals(m3.getTransactions().size(),1);
        assertTrue(m3.getTransactions().contains(t1));
    }
    @Test
    public void testAddTransactionTwice() {
        m3.addTransaction(t1);
        m3.addTransaction(t2);
        assertEquals(m3.getTransactions().size(),2);
        assertTrue(m3.getTransactions().contains(t2));
    }

    @Test
    public void testLeaveReview() {
        assertEquals(b1.getReviews().size(),0);
        m3.leaveReview(b1, r1);

        assertEquals(b1.getReviews().size(),1);
        assertTrue(b1.getReviews().contains(r1));

        assertEquals(m3.getReviews().size(),1);
        assertTrue(m3.getReviews().contains(r1));
    }
    @Test
    public void testLeaveReviewTwice() {
        m3.leaveReview(b1, r1);
        m3.leaveReview(b2, r2);
        assertEquals(m3.getReviews().size(),2);
        assertTrue(m3.getReviews().contains(r1));
        assertTrue(m3.getReviews().contains(r2));
    }
    @Test
    public void testLeaveReviewTwiceOnBook() {
        m3.leaveReview(b1, r1);
        m4.leaveReview(b1, r3);
        assertEquals(b1.getReviews().size(),2);
        assertTrue(b1.getReviews().contains(r1));
        assertTrue(b1.getReviews().contains(r3));
    }
    @Test
    public void testBorrowBook() {
        m3.borrowBook(b1);
        assertTrue(m3.getBorrowedBooks().contains(b1));
        assertEquals(m3.getBorrowedBooks().size(),1);
    }
    @Test
    public void testBorrowBookTwoBooks() {
        m3.borrowBook(b1);
        m3.borrowBook(b2);
        assertEquals(m3.getBorrowedBooks().size(),2);
        assertTrue(m3.getBorrowedBooks().contains(b1));
        assertTrue(m3.getBorrowedBooks().contains(b2));
    }
    @Test
    public void testIsBorrowingBooks() {
        m3.borrowBook(b1);
        assertTrue(m3.isBorrowingBooks());
        m3.borrowBook(b2);
        assertTrue(m3.isBorrowingBooks());
    }
    @Test
    public void testReturnBooks() {
        m4.borrowBook(b1);
        m4.borrowBook(b2);
        m4.returnBook(b1);
        assertTrue(m4.getBorrowedBooks().contains(b2));
        assertEquals(m4.getBorrowedBooks().size(),1);
    }
    @Test
    public void testReturnBooksTwice() {
        m4.borrowBook(b1);
        m4.borrowBook(b2);
        m4.returnBook(b1);
        m4.returnBook(b2);
        assertFalse(m4.isBorrowingBooks());
        assertEquals(m4.getBorrowedBooks().size(),0);
    }

}
