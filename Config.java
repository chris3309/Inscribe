import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class Config {
    private static final Properties props = new Properties();

    static{
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getApiKey(){
        return props.getProperty("ESV_API_KEY");
    }

}
