///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.13.0@pom
//DEPS io.quarkiverse.langchain4j:quarkus-langchain4j-openai:0.17.0
//DEPS io.quarkus:quarkus-picocli
//DEPS io.quarkus:quarkus-arc
//JAVAC_OPTIONS -parameters
//JAVA_OPTIONS -Djava.util.logging.manager=org.jboss.logmanager.LogManager
//FILES src/main/resources/application.properties
//SOURCES src/main/java/io/xstefank/GrammarCheckAIService.java
//SOURCES src/main/java/io/xstefank/CheckGrammarCommand.java

import io.quarkus.runtime.Quarkus;
import jakarta.enterprise.context.ApplicationScoped;
import io.xstefank.*;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;
import jakarta.enterprise.context.control.ActivateRequestContext;


@ApplicationScoped
public class JBangGrammarCheck {

    public static void main(String... args) {
        Quarkus.run(GrammarCheckMain.class, args);
    }

    @QuarkusMain
    @ActivateRequestContext
    public static class GrammarCheckMain implements QuarkusApplication {
        @Inject
        CommandLine.IFactory factory;

        @Override

        public int run(String... args) throws Exception {
            return new CommandLine(new CheckGrammarCommand(), factory).execute(args);
        }

    }
}
