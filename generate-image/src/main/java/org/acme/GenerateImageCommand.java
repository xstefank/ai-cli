package org.acme;

import dev.langchain4j.data.image.Image;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@CommandLine.Command
public class GenerateImageCommand implements Runnable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
        .withZone(ZoneId.systemDefault());

    @CommandLine.Parameters(paramLabel = "<prompt>", description = "The prompt for the generated image.")
    String prompt;

    @Inject
    GenerateImageAiService generateImageAiService;

    @Override
    @ActivateRequestContext
    public void run() {
        Uni<Image> imageUni = generateImageAiService.generateImage(prompt);
        Image image = imageUni.await().indefinitely();
        String generatedName = generateImageAiService.name(image);
        String name = formatter.format(Instant.now()) + "-" + generatedName;

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(image.url().toURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/home/mstefank/Pictures/" + name);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Generated image at " + file.getAbsolutePath());
    }
}
