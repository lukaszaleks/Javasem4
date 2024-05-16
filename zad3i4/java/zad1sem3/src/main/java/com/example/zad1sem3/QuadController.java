package com.example.zad1sem3;

import com.example.api.QuadApi;
import com.example.api.model.QuadCreateRequest;
import com.example.api.model.QuadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/quads")
public class QuadController implements QuadApi {
    private final QuadService quadService;

    @Autowired
    public QuadController(QuadService quadService) {
        this.quadService = quadService;
    }

    @GetMapping
    public ResponseEntity<List<QuadResponse>> getAllQuads() {
        return quadService.getAllQuads();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuadResponse> getQuadById(@PathVariable Long id) {
        return quadService.getQuadById(id);
    }

    @PostMapping
    public ResponseEntity<QuadResponse> createQuad(@RequestBody QuadCreateRequest createRequest) {
        return quadService.createQuad(createRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuadResponse> updateQuad(@PathVariable Long id, @RequestBody QuadCreateRequest updateRequest) {
        return quadService.updateQuad(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuad(@PathVariable Long id) {
        return quadService.deleteQuad(id);
    }
}