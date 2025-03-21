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
        Create new file for the given prompt. Output only the name
        and the content of the file in the form of JSON file which is the
        only allowed output. The format of the resulting JSON is:
        
        ```json
        {
            "name": "<name-of-the-generated-file>",
            "content": "<content-of-the-generated-file>"
        }
        ```
        
        The prompt for this request is "{prompt}".
        """)
    GeneratedFile createFile(String prompt);
}
