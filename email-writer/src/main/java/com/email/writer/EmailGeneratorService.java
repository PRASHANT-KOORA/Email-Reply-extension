/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.email.writer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
class EmailGeneratorService {
    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
    }

  public String generateReplyEmail(EmailRequest emailRequest){
    String content=emailRequest.getContent();
    String tone=emailRequest.getTone();
    
    // Building the prrompt for gemini ai
    String prompt=buildPrompt(emailRequest);
    
    // formating the request for the gemini ai
    Map<String,Object> request=Map.of("contents",new Object[]{
        Map.of("parts",new Object[]{
            Map.of("text",prompt)
        })          
   });
        

    // send the request and get the response

    String response=webClient.post().uri(geminiApiUrl+geminiApiKey)
            .header("Content-Type", "application/json")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    // return the response in format back to the user
    return ExtractContentFromResponse(response);
}

    private String buildPrompt(EmailRequest emailRequest) { //step1
    StringBuilder prompt = new StringBuilder();
    prompt.append("Generate a clear, ready-to-send email reply for the following email. ");
    prompt.append("Rules:\n");
    prompt.append("1. Do NOT include a subject line.\n");
    prompt.append("2. Base the reply strictly on the facts, names, and context present in the original email. Do not invent details.\n");
    prompt.append("3. NEVER include bracketed placeholders like [Your Name], [Your Phone Number], [Date], or [LinkedIn URL]. If a sign-off name is needed and not known, end cleanly with 'Best regards,' and nothing after it.\n");
    prompt.append("4. Output plain, natural paragraphs with standard line breaks—no markdown formatting or escaped characters.\n");

    if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
        prompt.append("5. Use a ").append(emailRequest.getTone()).append(" tone.\n");
    }

    prompt.append("\nOriginal Email:\n").append(emailRequest.getContent());
    return prompt.toString();
}

    private String ExtractContentFromResponse(String response) {
       try {
           ObjectMapper objectMapper=new ObjectMapper();
           JsonNode node=objectMapper.readTree(response);
           return node.path("candidates").get(0).path("content").path("parts").get(0).path("text").asString();
       } catch (Exception e) {
        return
         "ERROR PROCESSING THE REQUEST: "+e.getMessage();
       }
    }
}
