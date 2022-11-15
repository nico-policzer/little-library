package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Member admin;

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

       admin = lib.getMembers().get(0);

        lib.registerMember(m1);
        lib.registerMember(m2);
    }

    @Test
    public void testLibrary() {
        assertEquals(lib.getName(), "Little Library");
        assertTrue(admin.isAdmin());
        assertEquals(lib.getBooks().size(),5);
        assertTrue(lib.getBooks().contains(b1));
        assertTrue(lib.getBooks().contains(b2));
        assertTrue(lib.getBooks().contains(b3));
        assertTrue(lib.getBooks().contains(b4));
        assertTrue(lib.getBooks().contains(b5));
    }

    @Test
    public void testLibraryOtherCons() {
        List<Member> members = new ArrayList<>();
        members.add(m1);
        members.add(m2);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("The lord of the rings", "J. James Jamerson"));
        Library lib = new Library("Little Library", books, members, transactions);
        assertEquals(lib.getName(), "Little Library");
        assertTrue(lib.getMembers().get(0).isAdmin());
        assertEquals(lib.getMembers().size(), 3);
        assertEquals(lib.getMembers().get(1), m1);
        assertEquals(lib.getMembers().get(2), m2);
        assertEquals(lib.getBooks().size(),5);
        assertTrue(lib.getBooks().contains(b1));
        assertTrue(lib.getBooks().contains(b2));
        assertTrue(lib.getBooks().contains(b3));
        assertTrue(lib.getBooks().contains(b4));
        assertTrue(lib.getBooks().contains(b5));
        assertEquals(lib.getTransactions(),transactions);
    }

    @Test
    public void testRegisterBook() {
    lib.registerBook(b6);
    assertEquals(lib.getBooks().size(),6);
    assertTrue(lib.getBooks().contains(b6));
    assertTrue(lib.getBooks().contains(b1));
    assertTrue(lib.getBooks().contains(b2));
    assertTrue(lib.getBooks().contains(b3));
    assertTrue(lib.getBooks().contains(b4));
    assertTrue(lib.getBooks().contains(b5));
    }

    @Test
    public void testRegisterTwoBooks() {
        lib.registerBook(b6);
        lib.registerBook(b7);
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

        Transaction t = m2.getTransactions().get(0);

        assertEquals(t.getBook(), b3.getTitle());
        assertEquals(t.getMember(), m2.getName());

        assertEquals(lib.getTransactions().size(),1);
        assertEquals(lib.getTransactions().get(0), t);
    }
    @Test
    public void testReturnBookBorrowTwoBooksReturnOne() {
        lib.borrowBook(b4, m2);
        lib.borrowBook(b5,m2);
        lib.returnBook(b4,m2);

        assertTrue(m2.isBorrowingBooks());
        assertFalse(b4.isBorrowed());
        assertTrue(b5.isBorrowed());

        assertEquals(m2.getBorrowedBooks().size(),1);
        assertTrue(m2.getBorrowedBooks().contains(b5));

        Transaction t = m2.getTransactions().get(0);
        assertEquals(m2.getTransactions().size(),1);
        assertEquals(t.getBook(), b4.getTitle());
        assertEquals(t.getMember(), m2.getName());

        assertEquals(lib.getTransactions().size(),1);
        assertEquals(lib.getTransactions().get(0), t);
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


        Transaction t1 = m2.getTransactions().get(0);
        Transaction t2 = m2.getTransactions().get(1);

        assertEquals(m2.getTransactions().size(),2);
        assertEquals(t1.getBook(), b4.getTitle());
        assertEquals(t1.getMember(), m2.getName());

        assertEquals(t2.getBook(), b5.getTitle());
        assertEquals(t2.getMember(), m2.getName());

        assertEquals(lib.getTransactions().size(),2);
        assertEquals(lib.getTransactions().get(0), t1);
        assertEquals(lib.getTransactions().get(1), t2);
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
        lib.registerBook(b7);
        lib.registerBook(b8);
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
        assertEquals(lib.getMembers().size(),3);
        assertTrue(lib.getMembers().contains(m1));
        assertTrue(lib.getMembers().contains(m2));
    }
    @Test
    public void testRegisterMemberSameName() {
        assertTrue(lib.registerMember(m3));
        assertTrue(lib.registerMember(m4));
        assertFalse(lib.registerMember(m5));
        assertEquals(lib.getMembers().size(),5);
        assertTrue(lib.getMembers().contains(m1));
        assertTrue(lib.getMembers().contains(m2));
        assertTrue(lib.getMembers().contains(m3));
        assertTrue(lib.getMembers().contains(m4));
    }

    @Test
    public void testGetAvailableBooksBorrowOneBook() {
        assertEquals(lib.getAvailableBooks().size(),5);
        assertTrue(lib.getAvailableBooks().contains(b1));
        assertTrue(lib.getAvailableBooks().contains(b2));
        assertTrue(lib.getAvailableBooks().contains(b3));
        assertTrue(lib.getAvailableBooks().contains(b4));
        assertTrue(lib.getAvailableBooks().contains(b5));
        lib.borrowBook(b5, m1);
        assertEquals(lib.getAvailableBooks().size(),4);
        assertTrue(lib.getAvailableBooks().contains(b1));
        assertTrue(lib.getAvailableBooks().contains(b2));
        assertTrue(lib.getAvailableBooks().contains(b3));
        assertTrue(lib.getAvailableBooks().contains(b4));
        assertFalse(lib.getAvailableBooks().contains(b5));
    }
    @Test
    public void testGetAvailableBooksRegisterBook() {
        lib.registerBook(b6);
        assertEquals(lib.getAvailableBooks().size(),6);
        assertTrue(lib.getAvailableBooks().contains(b1));
        assertTrue(lib.getAvailableBooks().contains(b2));
        assertTrue(lib.getAvailableBooks().contains(b3));
        assertTrue(lib.getAvailableBooks().contains(b4));
        assertTrue(lib.getAvailableBooks().contains(b5));
        assertTrue(lib.getAvailableBooks().contains(b6));
    }
    @Test
    public void testGetAvailableBooksBorrowTwoBooks() {
        lib.borrowBook(b5, m1);
        lib.borrowBook(b4,m2);
        assertEquals(lib.getAvailableBooks().size(),3);
        assertTrue(lib.getAvailableBooks().contains(b1));
        assertTrue(lib.getAvailableBooks().contains(b2));
        assertTrue(lib.getAvailableBooks().contains(b3));
        assertFalse(lib.getAvailableBooks().contains(b4));
        assertFalse(lib.getAvailableBooks().contains(b5));
    }


}
