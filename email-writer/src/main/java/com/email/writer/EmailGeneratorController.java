package com.email.writer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/email-generator")
@CrossOrigin(origins="*")
public class EmailGeneratorController {
  private final EmailGeneratorService emailGeneratorService;
  @PostMapping
  public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
    String emailcontent=emailGeneratorService.generateReplyEmail(emailRequest);
    return ResponseEntity.ok(emailcontent);
  }
}
