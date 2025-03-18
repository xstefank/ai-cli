package org.acme;

import dev.langchain4j.data.image.Image;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CommandLine.Command
public class GenerateImageCommand implements Runnable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
        .withZone(ZoneId.systemDefault());

    @CommandLine.Parameters(paramLabel = "<prompt>", description = "The prompt for the generated image.")
    String prompt;

    @Inject
    GenerateImageAiService generateImageAiService;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    @ActivateRequestContext
    public void run() {
        try {
            int counter = 0;

            Image image = get(executor.submit(() -> generateImageAiService.generateImage(prompt)));
            String generatedName = get(executor.submit(() -> generateImageAiService.name(image)));

            String name = formatter.format(Instant.now()) + "-" + generatedName;

            BufferedImage bufferedImage = ImageIO.read(image.url().toURL());
            File file = new File("/home/mstefank/Pictures/" + name);
            ImageIO.write(bufferedImage, "png", file);
            System.out.write(("\rGenerated image at " + file.getAbsolutePath() + "\n").getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T get(Future<T> future) throws Exception {
        String anim = "|/-\\";
        int counter = 0;

        while (!future.isDone()) {
            String data = "\r" + anim.charAt(counter++ % anim.length());
                System.out.write(data.getBytes());
                Thread.sleep(100);
        }

        return future.get();
    }
}
