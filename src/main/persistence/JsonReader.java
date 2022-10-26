package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.*;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Library read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLibrary(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses library from JSON object and returns it
    private Library parseLibrary(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        List<Book> books = getBooks(jsonObject.getJSONArray("books"));
        List<Member> members = getMembers(jsonObject.getJSONArray("members"), books);
        List<Transaction> transactions = getTransactions(jsonObject.getJSONArray("transactions"));
        return new Library(name, books, members, transactions);
    }

    // EFFECTS: returns list of books parsed from jsonArray
    private List<Book> getBooks(JSONArray jsonArray) {
        List<Book> books = new ArrayList<>();
        for (Object json: jsonArray) {
            books.add(parseBook((JSONObject) json));
        }

        return books;
    }

    // EFFECTS: parses book from JSON object and returns it
    private Book parseBook(JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        String genre = jsonObject.getString("genre");
        Boolean isBorrowed = jsonObject.getBoolean("isBorrowed");
        List<Review> reviews = getReviews(jsonObject.getJSONArray("reviews"));
        return new Book(title, genre, author, isBorrowed, reviews);
    }


    // EFFECTS: returns list of reviews parsed from jsonArray
    private List<Review> getReviews(JSONArray jsonArray) {
        List<Review> reviews = new ArrayList<>();
        for (Object json: jsonArray) {
            reviews.add(parseReview((JSONObject) json));
        }

        return reviews;
    }

    // EFFECTS: parses review from JSON object and returns it
    private Review parseReview(JSONObject jsonObject) {
        String book = jsonObject.getString("book");
        String member = jsonObject.getString("member");
        int rating = jsonObject.getInt("rating");
        String comment = jsonObject.getString("comment");
        return new Review(book,member,rating,comment);
    }


    // EFFECTS: returns list of members parsed from jsonArray
    private List<Member> getMembers(JSONArray jsonArray,List<Book> books) {
        List<Member> members = new ArrayList<>();
        for (Object json: jsonArray) {
            members.add(parseMember((JSONObject) json, books));
        }

        return members;
    }

    // EFFECTS: parses member from JSON object and returns it
    private Member parseMember(JSONObject jsonObject, List<Book> books) {
        String name = jsonObject.getString("name");
        List<Book> borrowedBooks = parseMemberBooks(jsonObject.getJSONArray("borrowedBooks"), books);
        List<Review> reviews = getReviews(jsonObject.getJSONArray("reviews"));
        List<Transaction> transactions = getTransactions(jsonObject.getJSONArray("transactions"));
        return new Member(name,borrowedBooks,reviews,transactions);
    }

    // EFFECTS: returns list of book corresponding to members book list index
    private List<Book> parseMemberBooks(JSONArray jsonArray, List<Book> booksList) {
        List<Book> membersBooks = new ArrayList<>();
        for (Object json: jsonArray) {
            membersBooks.add(booksList.get((int)json));
        }
        return membersBooks;
    }

    // EFFECTS: adds transactions to library from JSON object
    private List<Transaction> getTransactions(JSONArray jsonArray) {
        List<Transaction> transactions = new ArrayList<>();
        for (Object json: jsonArray) {
            transactions.add(parseTransaction((JSONObject) json));
        }
        return transactions;
    }

    // EFFECTS: parses transaction from JSON object and returns it
    private Transaction parseTransaction(JSONObject jsonObject) {
        String book = jsonObject.getString("book");
        String member = jsonObject.getString("member");
        return (new Transaction(book, member));
    }



}
