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
        Log.error(prompt);
//        ChatLanguageModel chatModel = OpenAiChatModel.builder()
//            .apiKey(ApiKeys.OPENAI_API_KEY)
//            .build();

        ChatLanguageModel chatModel = GoogleAiGeminiChatModel.builder()
            .apiKey(ApiKeys.GEMINI_API_KEY)
            .modelName(prompt.model())
            .logRequestsAndResponses(true)
            .build();

        DefaultChatRequestParameters.Builder<?> builder = ChatRequestParameters.builder()
            .temperature(prompt.temperature());

        if (prompt.topK() != null) {
            builder.topK(prompt.topK());
        }

        if (prompt.topP() != null) {
            builder.topP(prompt.topP());
        }
        ChatRequestParameters parameters = builder.build();

        ChatRequest chatRequest = ChatRequest.builder()
            .messages(UserMessage.from(prompt.prompt()))
            .parameters(parameters)
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
            .messages(UserMessage.from("Write a story with 1000 words."))
            .parameters(ChatRequestParameters.builder().build())
            .build();

//        StreamingChatResponseHandler streamingChatResponseHandler = new StreamingChatResponseHandler() {
//            @Override
//            public void onPartialResponse(String token) {
//                System.out.println("ChatResource.onPartialResponse");
//                emitter.emit(token);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                System.out.println("ChatResource.onError");
//                emitter.fail(error);
//            }
//
//            @Override
//            public void onCompleteResponse(ChatResponse completeResponse) {
//                System.out.println("ChatResource.onCompleteResponse");
//                emitter.complete();
//            }
//        };

//        streamingChatModel.chat(chatRequest, streamingChatResponseHandler);

//        });
        return null;
    }
}
