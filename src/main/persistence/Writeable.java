package persistence;

import org.json.JSONObject;

// SOURCES: JsonSerializationDemo Project
public interface Writeable {
    // SOURCE: JsonSerializationDemo

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
