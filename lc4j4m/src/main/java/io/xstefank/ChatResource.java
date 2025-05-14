package io.xstefank;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class ChatResource {

    MultiEmitter<String> emitter;

    @POST
    public String chat(ChatPrompt prompt) {
        ChatLanguageModel chatModel = null;

        String model = prompt.model();

        if (model.startsWith("gemini")) {
            GoogleAiGeminiChatModel.GoogleAiGeminiChatModelBuilder chatModelBuilder = GoogleAiGeminiChatModel.builder()
                .apiKey(ApiKeys.GEMINI_API_KEY)
                .modelName(prompt.model())
                .logRequestsAndResponses(true)
                .temperature(prompt.temperature());

            if (prompt.topK() != null) {
                chatModelBuilder.topK(prompt.topK());
            }

            if (prompt.topP() != null) {
                chatModelBuilder.topP(prompt.topP());
            }

            chatModel = chatModelBuilder.build();
        } else if (model.startsWith("gpt")) {
            OpenAiChatModel.OpenAiChatModelBuilder chatModelBuilder = OpenAiChatModel.builder()
                .apiKey(ApiKeys.OPENAI_API_KEY)
                .modelName(prompt.model())
                .logRequests(true).logResponses(true)
                .temperature(prompt.temperature());

            if (prompt.topP() != null) {
                chatModelBuilder.topP(prompt.topP());
            }

            chatModel = chatModelBuilder.build();
        } else {
            throw new IllegalArgumentException("Unsupported chat model: " + prompt.model());
        }


        ChatRequest chatRequest = ChatRequest.builder()
            .messages(UserMessage.from(prompt.prompt()))
            .build();

        return chatModel.chat(chatRequest).aiMessage().text();
    }


    @GET
    @Path("streaming")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> streaming() {
        GoogleAiGeminiStreamingChatModel streamingChatModel = GoogleAiGeminiStreamingChatModel.builder()
            .apiKey(ApiKeys.GEMINI_API_KEY)
            .modelName("gemini-1.5-flash")
            .logRequestsAndResponses(true)
            .build();

        ChatRequest chatRequest = ChatRequest.builder()
            .messages(UserMessage.from("Write a story with 100 words."))
            .parameters(ChatRequestParameters.builder().build())
            .build();

        return Multi.createFrom().emitter(emitter -> {
            emitter.emit("before 1");
            emitter.emit("before 2");
            streamingChatModel.chat(chatRequest,
                new StreamingChatResponseHandler() {
                    @Override
                    public void onPartialResponse(String token) {
                        emitter.emit(token);
                        System.out.println("ChatResource.onPartialResponse " + token);
                    }

                    @Override
                    public void onError(Throwable error) {
                        emitter.fail(error);
                        System.out.println("ChatResource.onError");
                        System.out.println("error = " + error);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse completeResponse) {
                        emitter.complete();
                        System.out.println("ChatResource.onCompleteResponse");
                    }
                });
            emitter.emit("test 1");
            emitter.emit("test 2");
            emitter.emit("test 3");
        });

    }
}
