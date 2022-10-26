package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonTest {

    // EFFECTS: asserts libraries lib1 and lib2 contain identical fields
    public void sameLibrary(Library lib1, Library lib2) {
        assertEquals(lib1.getName(), lib2.getName());
        assertEquals(lib1.getBooks().size(), lib2.getBooks().size());
        for (int i = 0; i < lib1.getBooks().size(); i++) {
            sameBook(lib1.getBooks().get(i), lib2.getBooks().get(i));
        }
        assertEquals(lib1.getMembers().size(), lib2.getMembers().size());
        for (int i = 0; i < lib1.getMembers().size(); i++) {
            sameMember(lib1.getMembers().get(i), lib2.getMembers().get(i));
        }
        assertEquals(lib1.getTransactions().size(), lib2.getTransactions().size());
        for (int i = 0; i < lib1.getTransactions().size(); i++) {
            sameTransaction(lib1.getTransactions().get(i), lib2.getTransactions().get(i));
        }
    }

    // EFFECTS: asserts book1 and book2 share the exact same fields
    public void sameBook(Book b1, Book b2) {
        assertEquals(b1.getTitle(), b2.getTitle());
        assertEquals(b1.getRating(), b2.getRating());
        assertEquals(b1.getAuthor(), b2.getAuthor());
        assertEquals(b1.getGenre(), b2.getGenre());
        assertEquals(b1.isBorrowed(), b2.isBorrowed());
        assertEquals(b1.getReviews().size(), b2.getReviews().size());
        for (int i = 0; i < b1.getReviews().size(); i++) {
            sameReview(b1.getReviews().get(i), b2.getReviews().get(i));
        }
    }

    // EFFECTS: asserts r1 and r2 share the exact same fields
    public void sameReview(Review r1, Review r2) {
        assertEquals(r1.getComment(), r2.getComment());
        assertEquals(r1.getRating(), r2.getRating());
        assertEquals(r1.getBook(), r2.getBook());
        assertEquals(r1.getMember(), r2.getMember());
    }

    // EFFECTS: returns true if t1 and t2 share the exact same fields
    public void sameTransaction(Transaction t1, Transaction t2) {
        assertEquals(t1.getMember(), t2.getMember());
        assertEquals(t1.getBook(), t2.getBook());
    }

    // EFFECTS: returns true if m1 and m2 share the exact same fields
    public void sameMember(Member m1, Member m2) {
        assertEquals(m1.getName(), m2.getName());
        assertEquals(m1.getBorrowedBooks().size(), m2.getBorrowedBooks().size());
        for (int i = 0; i < m1.getBorrowedBooks().size(); i++) {
            sameBook(m1.getBorrowedBooks().get(i), m2.getBorrowedBooks().get(i));
        }

        assertEquals(m1.getTransactions().size(), m2.getTransactions().size());
        for (int i = 0; i < m1.getTransactions().size(); i++) {
            sameTransaction(m1.getTransactions().get(i), m2.getTransactions().get(i));
        }
        assertEquals(m1.getReviews().size(), m2.getReviews().size());
        for (int i = 0; i < m1.getReviews().size(); i++) {
            sameReview(m1.getReviews().get(i), m2.getReviews().get(i));
        }
    }
}


