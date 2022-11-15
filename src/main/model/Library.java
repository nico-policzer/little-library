package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library {
    // LIBRARY: A library with a name,  with a collection of books,
    // the ability to borrow books, register books and return books
    private List<Book> books;
    private List<Member> members;
    private String name;
    private List<Transaction> transactions;

    // EFFECTS: creates a library with a name and list of books, and a member named "admin"
    public Library(String name, List<Book> books) {
        this.books = books;
        this.name = name;
        members = new ArrayList<Member>();
        transactions = new ArrayList<Transaction>();
        members.add(new AdminMember());
    }

    // EFFECTS: creates a library with name, list of books, members, and transactions
    public Library(String name, List<Book> books, List<Member> members, List<Transaction> transactions) {
        this.books = books;
        this.name = name;
        this.members = members;
        this.transactions = transactions;
        this.members.add(0,new AdminMember());
    }

    // MODIFIES: this
    // EFFECTS: adds book to librarys collection
    public void registerBook(Book book) {
        books.add(book);
    }

    // EFFECTS: returns transaction history, ordered from least to most recent
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // REQUIRES: book is able to be borrowed, member is registered in library
    // MODIFIES: this, book, member
    // EFFECTS: sets book to being borrowed, adds to members list of borrowed books
    public void borrowBook(Book book, Member member) {
        book.borrowBook();
        member.borrowBook(book);
    }

    // REQUIRES: book has been previously borrowed by member, member is registered in library
    // MODIFIES: this, book, member
    // EFFECTS: sets book to not being borrowed, removes from members borrowed list and adds transactions
    public void returnBook(Book book, Member member) {
        Transaction t = new Transaction(book.getTitle(), member.getName());
        book.returnBook();
        member.returnBook(book);
        member.addTransaction(t);
        transactions.add(t);

    }

    public List<Book> getBooks() {
        return books;
    }


    // EFFECTS: returns a list of all the genres of books in the library
    public List<String> getGenres() {
        List<String> genres = new ArrayList<String>();
        for (Book book: books) {
            if (!genres.contains(book.getGenre())) {
                genres.add(book.getGenre());
            }
        }
        return genres;
    }


    //EFFECTS: returns a list of all the authors of books in the library
    public List<String> getAuthors() {
        List<String> authors = new ArrayList<String>();
        for (Book book: books) {
            if (!authors.contains(book.getAuthor())) {
                authors.add(book.getAuthor());
            }
        }
        return authors;
    }

    // EFFECTS: returns all books available to be borrowed(not currenty borrowed)
    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book: books) {
            if (!book.isBorrowed()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: returns true and registers member to library if no other member of same name exists, else returns false
    public Boolean registerMember(Member newMember) {
        for (Member m: members) {
            if (makePrettyString(m.getName()).equals(makePrettyString(newMember.getName()))) {
                return false;
            }
        }
        members.add(newMember);
        return true;
    }

    public List<Member> getMembers() {
        return members;
    }


    // EFFECTS: removes white space and quotation marks around s
    // from fitLifeGym UI
    private String makePrettyString(String s) {
        s = s.toLowerCase();
        s = s.trim();
        s = s.replaceAll("\"|'", "");
        return s;
    }



    // EFFECTS: returns library as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("books", booksToJson());
        json.put("members", membersToJson());
        json.put("transactions",transactionsToJson());
        return json;
    }

    // EFFECTS: returns members transactions as a JSON array
    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Book b: books) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns members transactions as a JSON array
    private JSONArray membersToJson() {
        JSONArray jsonArray = new JSONArray();
        // removes admin member, doesn't  need to be added.
        for (int i = 1; i < members.size(); i++) {
            jsonArray.put(members.get(i).toJson(this));
        }

        return jsonArray;
    }

    // EFFECTS: returns members transactions as a JSON array
    private JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Transaction t : transactions) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

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

    // EFFECTS: initializes the initial catalogue of books
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
