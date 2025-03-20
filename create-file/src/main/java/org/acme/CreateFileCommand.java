package org.acme;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command
public class CreateFileCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-n", "--name"}, description = "The language for which to check the grammar.")
    String name;

    @CommandLine.Option(names = {"-e", "--email"}, description = "The email to send the generated file to.")
    String email;

    @CommandLine.Parameters(paramLabel = "<prompt>", description = "The prompt for the generated file.")
    String prompt;

    @Inject
    CreateFileAiService createFileAiService;

    @Inject
    Mailer mailer;

    @Override
    @ActivateRequestContext
    public Integer call() throws IOException {
        GeneratedFile generatedFile = createFileAiService.createFile(prompt);

        String fileName = name != null ? name : generatedFile.name;
        Files.write(Path.of(fileName), generatedFile.content.getBytes());

        if (email != null) {
            mailer.send(Mail.withHtml(email, fileName, "Here is your generated file: " + fileName)
                .setFrom("xstefank122@gmail.com")
                .addAttachment(fileName, generatedFile.content.getBytes(), "text/plain"));
        }

        return 0;
    }
}