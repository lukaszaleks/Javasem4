package com.example.zad1sem3;

import com.example.api.model.QuadCreateRequest;
import com.example.api.model.QuadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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

    public ResponseEntity<List<QuadResponse>> getAllQuads() {
        List<QuadResponse> foundQuads = quadRepository.findAll().stream()
                .map(quadMapper::mapEntityToResponse)
                .collect(Collectors.toList());

        if (foundQuads.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(foundQuads, HttpStatus.OK);
    }

    public ResponseEntity<QuadResponse> getQuadById(Long id) {
        Quad quad = quadRepository.findById(id).orElseThrow(NoSuchElementException::new);

        return new ResponseEntity<>(quadMapper.mapEntityToResponse(quad), HttpStatus.OK);
    }

    public ResponseEntity<QuadResponse> createQuad(QuadCreateRequest createRequest) {
        var quad = quadMapper.mapRequestToEntity(createRequest);
        Quad savedQuad = quadRepository.save(quad);
        return new ResponseEntity<>(quadMapper.mapEntityToResponse(savedQuad), HttpStatus.OK);
    }

    public ResponseEntity<QuadResponse> updateQuad(Long id, QuadCreateRequest updateRequest) {
        Quad quad = quadRepository.findById(id).orElse(null);

        if (Objects.isNull(quad)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Quad updatedQuad = quadMapper.update(updateRequest, quad);
        return new ResponseEntity<>(quadMapper.mapEntityToResponse(quadRepository.save(updatedQuad)), HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteQuad(Long id) {
        Quad quad = quadRepository.findById(id).orElse(null);

        if (Objects.isNull(quad)) {
            throw new NoSuchElementException();
        }

        quadRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
