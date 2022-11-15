package ui;

import model.Book;
import model.Library;
import model.Member;
import model.Review;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LibraryInitializer {

    // EFFECTS: initializes library of name "name" with books and members
    public static Library defaultLibrary(String name) {
        java.util.List<Book> initialBooks = initialzeBooks();
        Library lib = new Library(name, initialBooks);

        Member m1 = new Member("GUI Admin User");
        Member m2 = new Member("H.P Deeves");
        Member m3 = new Member("Nico P");

        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);

        lib.borrowBook(initialBooks.get(0), m2);
        lib.returnBook(initialBooks.get(0), m2);
        lib.borrowBook(initialBooks.get(8), m3);
        lib.returnBook(initialBooks.get(8), m3);
        m3.leaveReview(initialBooks.get(8), new Review(initialBooks.get(8), m1,
                2, "Way too convoluted. Characters have four names each, impossible to follow"));
        m2.leaveReview(initialBooks.get(0), new Review(initialBooks.get(0), m2, 5, "Book changed my life."));

        return lib;
    }

    // EFFECTS: returns an initial catalogue of books
    public static List<Book> initialzeBooks() {
        Book b0 = new Book("The Four Agreements", "Self-Help", "Don Miguel Ruiz");
        Book b1 = new Book("Art of War", "Classic", "Sun Tzu");
        Book b2 = new Book("The Blank Slate", "Science", "Steven Pinker");
        Book b3 = new Book("A tale of Two Cities", "Classic", "Charles Dickens");
        Book b4 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        Book b5 = new Book("Stalingrad", "War", "Antony Beevor");
        Book b6 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");
        Book b7 = new Book("Crime and Punishment", "Classic", "Fyodor Dostoevsky");
        Book b8 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");
        Book b9 = new Book("The Lord of the Rings: Return of the King", "Fantasy", "J.R.R Tolkien");
        Book b10 = new Book("The Hobbit", "Fantasy", "J.R.R Tolkien");
        Book b11 = new Book("The Iliad", "Classic", "Homer");
        Book b12 = new Book("Meditations", "Self-Help", "Marcus Aurelius");
        List<Book> initialBooks = new ArrayList<Book>(Arrays.asList(
                b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12));
        return initialBooks;
    }
}
