package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        return null;
    }

    // EFFECTS: adds books to library from JSON object
    private void addBooks(Library lib, JSONObject jsonObject) {

    }

    // EFFECTS: parses book from JSON object and returns it
    private Book parseBook(JSONObject jsonObject) {
        return null;
    }


    // EFFECTS: adds reviews to book from JSON object
    private void addReviews(Book book, JSONObject jsonObject) {

    }

    // EFFECTS: parses review from JSON object and returns it
    private Review parseReview(JSONObject jsonObject) {
        return null;
    }


    // EFFECTS: adds members to library from JSON object
    private void addMembers(Library lib, JSONObject jsonObject) {

    }

    // EFFECTS: parses member from JSON object and returns it
    private Member parseMember(JSONObject json, List<Book> booksList) {
        return null;
    }

    // EFFECTS: adds books to m's currently borrowed list from index relative to booksList in jsonObject
    private void addMemberBooks(Member m, JSONObject jsonObject, List<Book> booksList) {

    }

    // EFFECTS: adds transactions to library from JSON object
    private void addTransactions(Library lib, JSONObject jsonObject) {

    }

    // EFFECTS: adds transactions to member from JSON object
    private void addTransactions(Member m, JSONObject jsonObject) {

    }

    // EFFECTS: parses transaction from JSON object and returns it
    private Transaction parseTransaction(JSONObject jsonObject) {
        return null;
    }



}
