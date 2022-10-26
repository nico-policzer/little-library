package persistence;


import model.Book;
import model.Library;
import model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    private Library lib;
    private Book b0;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Book b5;
    private Book b6;
    private Book b7;
    private Book b8;
    private List<Book> books;
    private Member m1;
    private Member m2;
    private Member m3;
    private Member m4;
    private Member m5;
    private Member admin;

    @BeforeEach
    public void setUp() {
        b0 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");
        b1 = new Book("A tale of Two Cities", "Classic", "Charles Dickens");
        b2 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        b3 = new Book("Stalingrad", "War", "Antony Beevor");
        b4 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");
        b5 = new Book("The Blank Slate", "Science", "Steven Pinker");
        b7 = new Book("Art of War", "Classic", "Sun Tzu");
        b8 = new Book("Crime and Punishment", "Classic", "Fyodor Dostoevsky");

        m1 = new Member("D. Fiddle");
        m2 = new Member("G. Foreman");

        m3 = new Member("J. Johnson");
        m4 = new Member("L. Johnson");
        m5 = new Member("L. Johnson");

        admin = lib.getMembers().get(0);
        // will have to add some reviews and such to books b5+
        // will have to add books and such to members to test
    }
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Library lib = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testEmptyLibrary() {
        JsonReader reader = new JsonReader("./data/emptyLibraryTest.json");
        try {
            lib = reader.read();

        } catch (IOException e) {
            fail("Couldn't find file");
        }
        assertEquals(lib.getTransactions().size(),0);
        assertEquals(lib.getMembers().size(),1);
        assertEquals(lib.getMembers().get(0),admin);
        assertEquals(lib.getBooks().size(),0);
    }

    @Test
    void testSomeBooksNoMembersLibrary() {
        JsonReader reader = new JsonReader("./data/librarySomeBooksTest.json");
        try {
            lib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        List<Book> books = lib.getBooks();
        assertEquals(books.get(0),b0);
        assertEquals(books.get(1),b1);
        assertEquals(books.get(2),b2);
        assertEquals(books.get(3),b3);
        assertEquals(books.get(4),b4);
    }
    // ADD TEST EXAMPLES FOR BOOKS WITH REVIEWS AND ON LOAN, MEMBERS WITH NOTHING AND MEMBERS WITH BORROWING BOOKS
    // ALSO MEMBERS WITH TRANSACTIONS, LOok in apple notes to find data design

}