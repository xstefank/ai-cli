package org.acme;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface CreateFileAiService {

    @SystemMessage("""
        You are a coding bot that creates single file scripts.
        """)
    @UserMessage("""
        Create new file for the given prompt. Output only the
        generated file content and nothing else. Use the markdown (```)
        notation.
        
        The prompt for this request is "{prompt}".
        """)
    String createFile(String prompt);
}
