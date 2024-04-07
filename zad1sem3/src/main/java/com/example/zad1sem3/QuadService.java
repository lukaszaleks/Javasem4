package com.example.zad1sem3;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuadService {
    private final QuadRepository quadRepository;

    @Autowired
    public QuadService(QuadRepository quadRepository) {
        this.quadRepository = quadRepository;
    }

    public List<QuadResponse> getAllQuads() {
        return quadRepository.findAll().stream()
                .map(this::mapQuadToQuadResponse)
                .collect(Collectors.toList());
    }

    public QuadResponse getQuadById(Long id) {
        Quad quad = quadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quad not found"));
        return mapQuadToQuadResponse(quad);
    }

    public QuadResponse createQuad(QuadCreateRequest createRequest) {
        Quad quad = new Quad();
        BeanUtils.copyProperties(createRequest, quad);
        Quad savedQuad = quadRepository.save(quad);
        return mapQuadToQuadResponse(savedQuad);
    }

    public QuadResponse updateQuad(Long id, QuadCreateRequest updateRequest) {
        Quad quad = quadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quad not found"));
        BeanUtils.copyProperties(updateRequest, quad);
        Quad updatedQuad = quadRepository.save(quad);
        return mapQuadToQuadResponse(updatedQuad);
    }

    public void deleteQuad(Long id) {
        quadRepository.deleteById(id);
    }

    private QuadResponse mapQuadToQuadResponse(Quad quad) {
        QuadResponse response = new QuadResponse();
        BeanUtils.copyProperties(quad, response);
        return response;
    }
}