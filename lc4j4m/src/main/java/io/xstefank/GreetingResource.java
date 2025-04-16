package io.xstefank;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
            .apiKey(ApiKeys.OPENAI_API_KEY)
            .modelName(GPT_4_O_MINI)
            .build();

        System.out.println(chatModel.chat("Tell me a joke about Java"));


        return "Hello from Quarkus REST";
    }
}
