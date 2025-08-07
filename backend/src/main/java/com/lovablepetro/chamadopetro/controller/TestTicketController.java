package com.lovablepetro.chamadopetro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestTicketController {
    
    @PostMapping("/simple")
    public ResponseEntity<String> testSimple(@RequestBody String body) {
        System.out.println("Test endpoint recebido: " + body);
        return ResponseEntity.ok("Sucesso: " + body);
    }
    
    @PostMapping("/json")
    public ResponseEntity<String> testJson(@RequestBody Object json) {
        System.out.println("Test JSON recebido: " + json.toString());
        return ResponseEntity.ok("JSON processado com sucesso");
    }
}