package file_storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PENTAGON on 16.08.14.
 */
public class ImageUploadResponse {
    public List<ImageFile> files = new ArrayList<>();

    public static class ImageFile {
        public String name;
        public String id;
        public String thumbnailUrl;
        public String deleteUrl;
    }
}
