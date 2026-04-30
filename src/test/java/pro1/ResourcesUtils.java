package pro1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ResourcesUtils {
    public static String readResourceFile(String path){
        try {
            var directPath = Paths.get("src", "test", path);
            if (Files.exists(directPath)) {
                return Files.readString(directPath);
            }
            return Files.readString(Paths.get("src","test", "resources", path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
