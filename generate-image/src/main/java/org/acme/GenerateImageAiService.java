package org.acme;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface GenerateImageAiService {

    Image generateImage(String prompt);

    @UserMessage("""
        Generate a short name for the given image available at {image}.
        Use the .png extension and use the dash (-) as word separator.
        Keep the name including the .png extension under 80 characters.
        """)
    String name(Image image);
}
