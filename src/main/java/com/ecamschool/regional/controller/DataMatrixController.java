package com.ecamschool.regional.controller;

import com.ecamschool.regional.api.DataMatrixApi;
import com.ecamschool.regional.dto.DataMatrixDTO;
import com.ecamschool.regional.dto.DataMatrixRequestDTO;
import com.ecamschool.regional.service.DataMatrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DataMatrixController implements DataMatrixApi {

    private final DataMatrixService service;

    public DataMatrixController(DataMatrixService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<DataMatrixDTO>> getAllDataMatrices() {
        log.debug("REST request to get all data matrices");
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<DataMatrixDTO> getDataMatrixByClientKey(String clientKey) {
        log.debug("REST request to get data matrix by clientKey: {}", clientKey);
        return ResponseEntity.ok(service.getByClientKey(clientKey));
    }

    @Override
    public ResponseEntity<DataMatrixDTO> createDataMatrix(DataMatrixRequestDTO request) {
        log.debug("REST request to create data matrix with clientKey: {}", request.getClientKey());
        DataMatrixDTO created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}