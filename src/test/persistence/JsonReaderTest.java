package persistence;


import model.Book;
import model.Library;
import model.Member;
import model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

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
        lib = new Library("TEST LIBRARY", new ArrayList<>());
        b0 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");
        b1 = new Book("A Tale of Two Cities", "Classic", "Charles Dickens");
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
        Library loadedLib = null;
        JsonReader reader = new JsonReader("./data/emptyLibraryTest.json");
        try {
            loadedLib = reader.read();

        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }

    @Test
    void testSomeBooksNoMembersLibrary() {
        JsonReader reader = new JsonReader("./data/librarySomeBooksTest.json");
        Library loadedLib = null;
        try {
            loadedLib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        lib.registerBook(b0);
        lib.registerBook(b1);
        lib.registerBook(b2);
        lib.registerBook(b3);
        lib.registerBook(b4);
        sameLibrary(lib,loadedLib);
        // may have to override equals to use assertEquals in this case
    }

    @Test
    void testSomeBooksSomeMembersLibrary() {
        lib.registerBook(b0);
        lib.registerBook(b1);
        lib.registerBook(b2);
        lib.registerBook(b3);
        lib.registerBook(b4);

        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);
        lib.borrowBook(b0,m1);
        lib.borrowBook(b1,m1);
        lib.borrowBook(b3,m3);
        Library loadedLib = null;
        JsonReader reader = new JsonReader("./data/librarySomeMembersSomeBooksTest.json");
        try {
            loadedLib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }
    // ADD TEST EXAMPLES FOR BOOKS WITH REVIEWS AND ON LOAN, MEMBERS WITH NOTHING AND MEMBERS WITH BORROWING BOOKS
    // ALSO MEMBERS WITH TRANSACTIONS, LOok in apple notes to find data design

    // TO TEST:
    // READ FROM TEST FILE
    @Test
    void testFullLibrary() {
        lib.registerBook(b0);
        lib.registerBook(b1);
        lib.registerBook(b2);
        lib.registerBook(b3);
        lib.registerBook(b4);

        Library loadedLib = null;

        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);

        lib.borrowBook(b4, m1);
        lib.returnBook(b4,m1);

        lib.borrowBook(b2, m2);
        lib.returnBook(b2,m2);
        m2.leaveReview(b2, new Review(b2, m2, 3, "Great read!"));

        lib.borrowBook(b0,m1);
        lib.borrowBook(b1,m1);
        lib.borrowBook(b3,m3);
        JsonReader reader = new JsonReader("./data/fullLibraryTest.json");
        try {
            loadedLib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }
}