package com.example.screensound.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

  public static String obterInformacao(String texto) {
    OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"));

    CompletionRequest request = CompletionRequest.builder()
        .model("text-davinci-003")
        .prompt("me fale sobre o artista: " + texto)
        .maxTokens(1000)
        .temperature(0.7)
        .build();

    var response = service.createCompletion(request);

    return response.getChoices().getFirst().getText();
  }
}