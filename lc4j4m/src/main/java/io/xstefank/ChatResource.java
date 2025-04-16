package io.xstefank;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/chat")
public class ChatResource {

    @POST
    public String chat(ChatPrompt prompt) {
        Log.error(prompt);
//        ChatLanguageModel chatModel = OpenAiChatModel.builder()
//            .apiKey(ApiKeys.OPENAI_API_KEY)
//            .build();

        ChatLanguageModel chatModel = GoogleAiGeminiChatModel.builder()
            .apiKey(ApiKeys.GEMINI_API_KEY)
            .modelName("gemini-1.5-flash")
            .logRequestsAndResponses(true)
            .build();

        ChatRequestParameters parameters = ChatRequestParameters.builder()
            .temperature(Double.valueOf(prompt.temperature()))
            .topK(5)
            .topP(0.95)
            .build();

        ChatRequest chatRequest = ChatRequest.builder()
            .messages(UserMessage.from(prompt.prompt()))
            .parameters(parameters)
            .build();

        return chatModel.chat(chatRequest).aiMessage().text();
    }
}
