package org.acme;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command
public class CreateFileCommand implements Callable<Integer> {

    @Inject
    CreateFileAiService createFileAiService;

    @CommandLine.Parameters(paramLabel = "<prompt>", description = "The prompt for the generated file.")
    String prompt;

    @Override
    @ActivateRequestContext
    public Integer call() {
        System.out.println(createFileAiService.createFile(prompt).toString());

        return 0;
    }
}