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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {


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
        b6 = new Book("Paris 1919", "History", "Margaret Macmillan");
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
    void testWriterNonExistentFile() {
        JsonReader reader = new JsonReader("\"./data/my\\0illegal:fileName.json");
        try {
            Library lib = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLibrary() {
        try {
            Library lib = new Library("TestWriterLibrary", new ArrayList<>());
            JsonWriter writer = new JsonWriter("./data/writerTestEmptyLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestEmptyLibrary.json");
            Library loadedLib = reader.read();
            sameLibrary(loadedLib, lib);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterSomeBooksNoMembersLibrary() {
        Library loadedLib = null;
        lib.registerBook(b5);
        lib.registerBook(b6);
        lib.registerBook(b7);
        lib.registerBook(b8);
        lib.registerBook(b0);
        try {
            JsonWriter writer = new JsonWriter("./data/writerTestSomeBooksLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestSomeBooksLibrary.json");
            loadedLib = reader.read();
            sameLibrary(loadedLib, lib);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterSomeBooksSomeMembersLibrary() {
        lib.registerBook(b0);
        lib.registerBook(b1);
        lib.registerBook(b2);
        lib.registerBook(b3);
        lib.registerBook(b4);

        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);
        lib.borrowBook(b0, m1);
        lib.borrowBook(b1, m1);
        lib.borrowBook(b3, m3);
        Library loadedLib = null;
        try {
            JsonWriter writer = new JsonWriter("./data/writerTestSomeBooksAndMembers.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestSomeBooksAndMembers.json");
            loadedLib = reader.read();
            sameLibrary(loadedLib, lib);
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib, loadedLib);
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
        lib.returnBook(b4, m1);

        lib.borrowBook(b2, m2);
        lib.returnBook(b2, m2);
        m2.leaveReview(b2, new Review(b2, m2, 3, "Great read!"));

        lib.borrowBook(b0, m1);
        lib.borrowBook(b1, m1);
        lib.borrowBook(b3, m3);
        try {
            JsonWriter writer = new JsonWriter("./data/writerTestFullLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/writerTestFullLibrary.json");
            loadedLib = reader.read();
            sameLibrary(loadedLib, lib);
        } catch (IOException e) {
            fail("Couldn't find file");
        }
        sameLibrary(lib, loadedLib);

    }
}
