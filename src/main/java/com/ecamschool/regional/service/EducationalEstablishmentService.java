package com.ecamschool.regional.service;

import com.ecamschool.regional.dto.EducationalEstablishmentDTO;
import com.ecamschool.regional.dto.EducationalEstablishmentRequestDTO;
import com.ecamschool.regional.entity.EducationalEstablishment;
import com.ecamschool.regional.exception.NotFoundException;
import com.ecamschool.regional.exception.ConflictException;
import com.ecamschool.regional.mapper.EducationalEstablishmentMapper;
import com.ecamschool.regional.repository.EducationalEstablishmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class EducationalEstablishmentService {

    private final EducationalEstablishmentRepository repository;
    private final EducationalEstablishmentMapper mapper;

    public EducationalEstablishmentService(EducationalEstablishmentRepository repository,
                                           EducationalEstablishmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // =========================
    // READ
    // =========================

    @Transactional(readOnly = true)
    public List<EducationalEstablishmentDTO> getAll() {
        log.debug("Request to get all establishments");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EducationalEstablishmentDTO getById(Long id) {
        log.debug("Request to get establishment by id: {}", id);
        EducationalEstablishment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Establishment not found with id: " + id));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public EducationalEstablishmentDTO getByCode(String code) {
        final String cleanedCode = cleanString(code);

        log.debug("Request to get establishment by code: {}", cleanedCode);

        EducationalEstablishment entity = repository.findByCode(cleanedCode)
                .orElseThrow(() -> new NotFoundException("Establishment not found with code: " + cleanedCode));

        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<EducationalEstablishmentDTO> getByRegion(String region) {
        region = cleanString(region);
        log.debug("Request to get establishments by region: {}", region);

        return repository.findByDelegationRegion(region).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EducationalEstablishmentDTO> getByDepartment(String department) {
        department = cleanString(department);
        log.debug("Request to get establishments by department: {}", department);

        return repository.findByDepartment(department).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EducationalEstablishmentDTO> getActive() {
        log.debug("Request to get active establishments");
        return repository.findByIsActiveTrue().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // CREATE
    // =========================

    public EducationalEstablishmentDTO create(EducationalEstablishmentRequestDTO request) {
        log.debug("Request to create establishment: {}", request.getCode());

        String code = cleanString(request.getCode());
        String name = cleanString(request.getName());
        String city = cleanString(request.getCity());
        String department = cleanString(request.getDepartment());
        String region = cleanString(request.getDelegationRegion());
        String type = cleanString(request.getType()); // FIX IMPORTANT

        if (repository.findByCode(code).isPresent()) {
            throw new ConflictException("Establishment already exists with code: " + code);
        }

        EducationalEstablishment entity = mapper.toEntity(request);

        // On écrase avec les valeurs nettoyées
        entity.setCode(code);
        entity.setName(name);
        entity.setCity(city);
        entity.setDepartment(department);
        entity.setDelegationRegion(region);
        entity.setType(type);

        EducationalEstablishment saved = repository.save(entity);
        log.debug("Establishment created with id: {}", saved.getId());

        return mapper.toDto(saved);
    }

    // =========================
    // UPDATE
    // =========================

    public EducationalEstablishmentDTO update(Long id, EducationalEstablishmentRequestDTO request) {
        log.debug("Request to update establishment: {}", id);

        EducationalEstablishment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Establishment not found with id: " + id));

        entity.setCode(cleanString(request.getCode()));
        entity.setName(cleanString(request.getName()));
        entity.setCity(cleanString(request.getCity()));
        entity.setDepartment(cleanString(request.getDepartment()));
        entity.setDelegationRegion(cleanString(request.getDelegationRegion()));
        entity.setType(cleanString(request.getType())); //  FIX IMPORTANT

        EducationalEstablishment updated = repository.save(entity);
        log.debug("Establishment updated with id: {}", updated.getId());

        return mapper.toDto(updated);
    }

    // =========================
    // DELETE
    // =========================

    public void delete(Long id) {
        log.debug("Request to delete establishment: {}", id);

        EducationalEstablishment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Establishment not found with id: " + id));

        repository.delete(entity);
        log.debug("Establishment deleted with id: {}", id);
    }

    // =========================
    // UTIL
    // =========================

    private String cleanString(String value) {
        if (value == null) return null;

        String cleaned = value.replaceAll("^\"+|\"+$", "");
        cleaned = cleaned.replace("\\\"", "\"");
        return cleaned.trim();
    }
}