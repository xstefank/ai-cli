package org.acme;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command
public class CreateFileCommand implements Callable<Integer> {

    @Inject
    CreateFileAiService createFileAiService;

    @CommandLine.Parameters(paramLabel = "<prompt>", description = "The prompt for the generated file.")
    String prompt;

    @Override
    @ActivateRequestContext
    public Integer call() throws IOException {
        GeneratedFile generatedFile = createFileAiService.createFile(prompt);

        Files.write(Path.of(generatedFile.name), generatedFile.content.getBytes());

        return 0;
    }
}