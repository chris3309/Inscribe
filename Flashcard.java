import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class Flashcard {
    private String ref;
    
    private static final String API_URL = "https://api.esv.org/v3/passage/text/";
    private static final String API_KEY = Config.getApiKey();

    public Flashcard(String ref) {
        this.ref = ref;
    }

    public String getReference() {
        return ref;
    }

}
