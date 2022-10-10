package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {


    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Book b5;
    private Book b6;
    private Book b7;
    private Book b8;
    private List<Book> books;
    private Library lib;
    private Member m1;
    private Member m2;
    private Member m3;
    private Member m4;
    private Member m5;
    @BeforeEach
    public void setUp() {
        b1 = new Book("Art of War", "Classic", "Sun Tzu");
        b2 = new Book("The Blank Slate", "Science", "Steven Pinker");
        b3 = new Book("A tale of Two Cities", "Classic", "Charles Dickens");
        b4 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        b5 = new Book("Stalingrad", "War", "Antony Beevor");
        books = new ArrayList<Book>();
        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);
        books.add(b5);
        lib = new Library("Little Library", books);

        b6 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");
        b7 = new Book("Crime and Punishment", "Classic", "Fyodor Dostoevsky");

        b8 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");

        m1 = new Member("D. Fiddle");
        m2 = new Member("G. Foreman");

        m3 = new Member("J. Johnson");
        m4 = new Member("L. Johnson");
        m5 = new Member("L. Johnson");

        lib.registerMember(m1);
        lib.registerMember(m2);
    }

    @Test
    public void testLibrary() {
        assertEquals(lib.getName(), "Little Library");
        assertEquals(lib.getBooks().size(),5);
        assertTrue(lib.getBooks().contains(b1));
        assertTrue(lib.getBooks().contains(b2));
        assertTrue(lib.getBooks().contains(b3));
        assertTrue(lib.getBooks().contains(b4));
        assertTrue(lib.getBooks().contains(b5));
    }

    @Test
    public void testDonate() {
    lib.donateBook(b6);
    assertEquals(lib.getBooks().size(),6);
    assertTrue(lib.getBooks().contains(b6));
    assertTrue(lib.getBooks().contains(b1));
    assertTrue(lib.getBooks().contains(b2));
    assertTrue(lib.getBooks().contains(b3));
    assertTrue(lib.getBooks().contains(b4));
    assertTrue(lib.getBooks().contains(b5));
    }

    @Test
    public void testDonateTwice() {
        lib.donateBook(b6);
        lib.donateBook(b7);
        assertEquals(lib.getBooks().size(),7);
        assertTrue(lib.getBooks().contains(b7));
        assertTrue(lib.getBooks().contains(b6));
        assertTrue(lib.getBooks().contains(b1));
        assertTrue(lib.getBooks().contains(b2));
        assertTrue(lib.getBooks().contains(b3));
        assertTrue(lib.getBooks().contains(b4));
        assertTrue(lib.getBooks().contains(b5));
    }

    @Test
    public void testBorrowBook() {
        lib.borrowBook(b1, m1);
        assertTrue(b1.isBorrowed());
        assertTrue(m1.isBorrowingBooks());
        assertTrue(m1.getBorrowedBooks().contains(b1));
        assertEquals(m1.getBorrowedBooks().size(),1);
    }
    @Test
    public void testBorrowBookTwo() {
        lib.borrowBook(b1, m1);
        lib.borrowBook(b2, m1);
        assertTrue(b1.isBorrowed());
        assertTrue(b2.isBorrowed());
        assertTrue(m1.isBorrowingBooks());
        assertEquals(m1.getBorrowedBooks().size(),2);
        assertTrue(m1.getBorrowedBooks().contains(b1));
        assertTrue(m1.getBorrowedBooks().contains(b2));
    }

    @Test
    public void testReturnBook() {
        lib.borrowBook(b3,m2);
        lib.returnBook(b3,m2);
        assertFalse(b3.isBorrowed());
        assertFalse(m2.isBorrowingBooks());
        assertEquals(m2.getBorrowedBooks().size(),0);
        assertEquals(m2.getTransactions().size(),1);
        assertEquals(m2.getTransactions().get(0).getBook(), b3);
        assertEquals(m2.getTransactions().get(0).getMember(), m2);
    }
    @Test
    public void testReturnBookTwoBooksReturnOne() {
        lib.borrowBook(b4, m2);
        lib.borrowBook(b5,m2);
        lib.returnBook(b4,m2);
        assertTrue(m2.isBorrowingBooks());
        assertFalse(b4.isBorrowed());
        assertTrue(b5.isBorrowed());
        assertEquals(m2.getBorrowedBooks().size(),1);
        assertTrue(m2.getBorrowedBooks().contains(b5));
        assertEquals(m2.getTransactions().size(),1);
        assertEquals(m2.getTransactions().get(0).getBook(), b4);
        assertEquals(m2.getTransactions().get(0).getMember(), m2);

    }
    @Test
    public void testReturnBookTwoBooksReturnBoth() {
        lib.borrowBook(b4, m2);
        lib.borrowBook(b5,m2);
        lib.returnBook(b4,m2);
        lib.returnBook(b5,m2);

        assertFalse(b4.isBorrowed());
        assertFalse(b5.isBorrowed());
        assertFalse(m2.isBorrowingBooks());
        assertEquals(m2.getBorrowedBooks().size(),0);

        assertEquals(m2.getTransactions().size(),2);
        assertEquals(m2.getTransactions().get(0).getBook(), b4);
        assertEquals(m2.getTransactions().get(0).getMember(), m2);

        assertEquals(m2.getTransactions().get(1).getBook(), b5);
        assertEquals(m2.getTransactions().get(1).getMember(), m2);
    }

    @Test
    public void testGetGenres() {
        assertEquals(lib.getGenres().size(),4);
        assertTrue(lib.getGenres().contains("War"));
        assertTrue(lib.getGenres().contains("Science"));
        assertTrue(lib.getGenres().contains("Classic"));
        assertTrue(lib.getGenres().contains("Foreign Language"));
    }

    @Test
    public void testGetAuthors() {
        assertEquals(lib.getAuthors().size(),5);
        assertTrue(lib.getAuthors().contains("Steven Pinker"));
        assertTrue(lib.getAuthors().contains("Sun Tzu"));
        assertTrue(lib.getAuthors().contains("Charles Dickens"));
        assertTrue(lib.getAuthors().contains("Antony Beevor"));
        assertTrue(lib.getAuthors().contains("Angel Parra"));
    }
    @Test
    public void testGetAuthorsSameAuthorTwice() {
        lib.donateBook(b7);
        lib.donateBook(b8);
        assertEquals(lib.getAuthors().size(),6);
        assertTrue(lib.getAuthors().contains("Fyodor Dostoevsky"));
        assertTrue(lib.getAuthors().contains("Steven Pinker"));
        assertTrue(lib.getAuthors().contains("Sun Tzu"));
        assertTrue(lib.getAuthors().contains("Charles Dickens"));
        assertTrue(lib.getAuthors().contains("Antony Beevor"));
        assertTrue(lib.getAuthors().contains("Angel Parra"));
    }

    @Test
    public void testRegisterMember() {
        assertEquals(lib.getMembers().size(),2);
        assertTrue(lib.getMembers().contains(m1));
        assertTrue(lib.getMembers().contains(m2));
    }
    @Test
    public void testRegisterMemberSameName() {
        assertTrue(lib.registerMember(m3));
        assertTrue(lib.registerMember(m4));
        assertFalse(lib.registerMember(m5));
        assertEquals(lib.getMembers().size(),4);
        assertTrue(lib.getMembers().contains(m1));
        assertTrue(lib.getMembers().contains(m2));
        assertTrue(lib.getMembers().contains(m3));
        assertTrue(lib.getMembers().contains(m4));


    }



}