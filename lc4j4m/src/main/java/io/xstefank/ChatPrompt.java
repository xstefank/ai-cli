package io.xstefank;

public record ChatPrompt(String prompt, String model, Double temperature, Integer topK, Double topP) {
}
