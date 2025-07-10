package io.xstefank;

import io.quarkus.arc.Arc;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.util.Optional;

@CommandLine.Command
public class CheckGrammarCommand implements Runnable {

    @CommandLine.Option(names = {"-l", "--language"}, description = "The language for which to check the grammar.", defaultValue = "Czech")
    String language;

    @CommandLine.Parameters(paramLabel = "<text>", description = "The text for which to check the grammar.")
    String text;

    @Inject
    GrammarCheckAIService grammarCheckAIService;

    @Override
    @ActivateRequestContext
    public void run() {
        Optional.ofNullable(System.getenv("GRAMMAR_CHECK_OPENAI_KEY")).orElseThrow(
            () -> new IllegalStateException("Required environment variable 'GRAMMAR_CHECK_OPENAI_KEY' not found. Please set it to the valid OpenAI token."));

        notifySend(String.format("%s grammar fix -- sent", language));
        String result = getGrammarCheckAIService().fixGrammar(language, text);
        notifySend("Grammar fix done");
        System.out.println(result);
    }

    private GrammarCheckAIService getGrammarCheckAIService() {
        // Fallback to programmatic lookup (needed for JBang)
        return grammarCheckAIService != null ? grammarCheckAIService : Arc.container().select(GrammarCheckAIService.class).get();
    }

    private void notifySend(String text) {
        if (!System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            return;
        }

        try {
            new ProcessBuilder().command("notify-send", text).start().waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
