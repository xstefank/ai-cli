package io.xstefank;

import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.data.message.UserMessage;
import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Path("/chat")
public class ChatResource {

    @POST
    public String chat(ChatPrompt prompt) {
        Log.error(prompt);
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
            .apiKey(ApiKeys.OPENAI_API_KEY)
            .build();

        ChatRequestParameters parameters = ChatRequestParameters.builder()
            .modelName(GPT_4_O_MINI.toString())
            .temperature(Double.valueOf(prompt.temperature()))
//            .topK(prompt.topK())
//            .topP(prompt.topP())
            .build();

        ChatRequest chatRequest = ChatRequest.builder()
            .messages(UserMessage.from(prompt.prompt()))
            .parameters(parameters)
            .build();

        return chatModel.chat(chatRequest).aiMessage().text();
    }
}
