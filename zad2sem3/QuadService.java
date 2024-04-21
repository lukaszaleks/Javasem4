package com.example.zad1sem3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuadService {
    private final QuadRepository quadRepository;
    private final QuadMapper quadMapper;

    @Autowired
    public QuadService(QuadRepository quadRepository, QuadMapper quadMapper) {
        this.quadRepository = quadRepository;
        this.quadMapper = quadMapper;
    }

    public List<QuadResponse> getAllQuads() {
        return quadRepository.findAll().stream()
                .map(quadMapper::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public QuadResponse getQuadById(Long id) {
        Quad quad = quadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quad not found"));
        return quadMapper.mapEntityToResponse(quad);
    }

    public QuadResponse createQuad(QuadCreateRequest createRequest) {
        var quad = quadMapper.mapRequestToEntity(createRequest);
        Quad savedQuad = quadRepository.save(quad);
        return quadMapper.mapEntityToResponse(savedQuad);
    }

    public QuadResponse updateQuad(Long id, QuadCreateRequest updateRequest) {
        Quad quad = quadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quad not found"));
        Quad updatedQuad = quadMapper.update(updateRequest, quad);
        return quadMapper.mapEntityToResponse(quadRepository.save(updatedQuad));
    }

    public void deleteQuad(Long id) {
        quadRepository.deleteById(id);
    }
}
