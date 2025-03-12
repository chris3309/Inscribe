import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;


public class Flashcard {
    
    private String reference;
    
    private static final String API_URL = "https://api.esv.org/v3/passage/text/";
    private static final String API_KEY = Config.getApiKey();

    public Flashcard(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference + " (ESV)";
    }

    public String getVerse() {
        try {
            String encodedRef = URLEncoder.encode(reference, StandardCharsets.UTF_8);
            String url = API_URL + "?q=" + encodedRef 
                + "&include-headings=false&include-footnotes=false&include-verse-numbers=false"
                + "&include-short-copyright=false&include-passage-references=false";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Token " + API_KEY)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Simple parsing: look for the "passages" array and extract the first passage.
            int passagesIndex = responseBody.indexOf("\"passages\":");
            if (passagesIndex == -1) {
                return "Error: Passage not found";
            }
            int startBracket = responseBody.indexOf("[", passagesIndex);
            int endBracket = responseBody.indexOf("]", startBracket);
            if (startBracket == -1 || endBracket == -1) {
                return "Error: Passage not found";
            }
            String passagesContent = responseBody.substring(startBracket + 1, endBracket).trim();
            if (passagesContent.startsWith("\"") && passagesContent.endsWith("\"")) {
                String passageText = passagesContent.substring(1, passagesContent.length() - 1);
                passageText = passageText.replace("\\n", "\n");
                // After extracting passageText from the JSON response:
                passageText = passageText.replace("\\u201c", "\"").replace("\\u201d", "\"");

                return passageText.trim();
            } else {
                return "Error: Passage not found";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error retrieving passage";
        }
    }

}
