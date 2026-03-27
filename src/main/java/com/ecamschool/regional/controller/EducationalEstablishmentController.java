package com.ecamschool.regional.controller;

import com.ecamschool.regional.api.EducationalEstablishmentsApi;
import com.ecamschool.regional.dto.EducationalEstablishmentDTO;
import com.ecamschool.regional.dto.EducationalEstablishmentRequestDTO;
import com.ecamschool.regional.service.EducationalEstablishmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class EducationalEstablishmentController implements EducationalEstablishmentsApi {

    private final EducationalEstablishmentService service;

    public EducationalEstablishmentController(EducationalEstablishmentService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<EducationalEstablishmentDTO>> getAllEstablishments() {
        log.debug("REST request to get all establishments");
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<EducationalEstablishmentDTO> getEstablishmentById(Integer id) {
        log.debug("REST request to get establishment by id: {}", id);
        return ResponseEntity.ok(service.getById(id.longValue()));
    }

    @Override
    public ResponseEntity<List<EducationalEstablishmentDTO>> getEstablishmentsByDelegationRegion(String region) {
        log.debug("REST request to get establishments by region: {}", region);
        return ResponseEntity.ok(service.getByRegion(region));
    }

    @Override
    public ResponseEntity<EducationalEstablishmentDTO> createEstablishment(
            EducationalEstablishmentRequestDTO request) {

        log.debug("REST request to create establishment: {}", request.getCode());
        EducationalEstablishmentDTO created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}