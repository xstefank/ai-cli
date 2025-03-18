package io.xstefank;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface FsBot {

    @SystemMessage("""
        You have tools to interact with the local filesystem and the users
        will ask you to perform operations like reading and writing files.
        
        The only directories you are allowed to interact with are the current directory
        and its subdirectories recursively.
        
        If user doesn't specify directory, assume it's the current directory.
        
        When listing files, sort them alphabetically with directories first and
        then files.
        """
    )
    String chat(@UserMessage String question);
}
