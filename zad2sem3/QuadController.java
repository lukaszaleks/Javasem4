package com.example.zad1sem3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quads")
public class QuadController {
    private final QuadService quadService;

    @Autowired
    public QuadController(QuadService quadService) {
        this.quadService = quadService;
    }

    @GetMapping
    public List<QuadResponse> getAllQuads() {
        return quadService.getAllQuads();
    }

    @GetMapping("/{id}")
    public QuadResponse getQuadById(@PathVariable Long id) {
        return quadService.getQuadById(id);
    }

    @PostMapping
    public QuadResponse createQuad(@RequestBody QuadCreateRequest createRequest) {
        return quadService.createQuad(createRequest);
    }

    @PutMapping("/{id}")
    public QuadResponse updateQuad(@PathVariable Long id, @RequestBody QuadCreateRequest updateRequest) {
        return quadService.updateQuad(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteQuad(@PathVariable Long id) {
        quadService.deleteQuad(id);
    }
}