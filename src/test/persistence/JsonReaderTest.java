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
    private Member m1;
    private Member m2;
    private Member m3;


    @BeforeEach
    public void setUp() {
        lib = new Library("TEST LIBRARY", new ArrayList<>());
        b0 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");
        b1 = new Book("A Tale of Two Cities", "Classic", "Charles Dickens");
        b2 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        b3 = new Book("Stalingrad", "War", "Antony Beevor");
        b4 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");


        m1 = new Member("D. Fiddle");
        m2 = new Member("G. Foreman");

        m3 = new Member("J. Johnson");

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
        JsonReader reader = new JsonReader("./data/readerTestEmptyLibrary.json");
        try {
            loadedLib = reader.read();

        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }

    @Test
    void testSomeBooksNoMembersLibrary() {
        JsonReader reader = new JsonReader("./data/readerTestSomeBooks.json");
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
        JsonReader reader = new JsonReader("./data/readerTestSomeBooksAndMembers.json");
        try {
            loadedLib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }

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
        JsonReader reader = new JsonReader("./data/readerTestFullLibrary.json");
        try {
            loadedLib = reader.read();
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib,loadedLib);
    }
}