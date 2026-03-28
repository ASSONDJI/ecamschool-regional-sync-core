package com.ecamschool.regional.service;

import com.ecamschool.regional.dto.DataMatrixDTO;
import com.ecamschool.regional.dto.DataMatrixRequestDTO;
import com.ecamschool.regional.entity.DataMatrix;
import com.ecamschool.regional.enums.DataType;
import com.ecamschool.regional.enums.SyncStatus;
import com.ecamschool.regional.exception.NotFoundException;
import com.ecamschool.regional.exception.ConflictException;
import com.ecamschool.regional.mapper.DataMatrixMapper;
import com.ecamschool.regional.repository.DataMatrixRepository;
import com.ecamschool.regional.repository.UserRepository;
import com.ecamschool.regional.repository.EducationalEstablishmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class DataMatrixService {

    private final DataMatrixRepository repository;
    private final DataMatrixMapper mapper;
    private final UserRepository userRepository;
    private final EducationalEstablishmentRepository establishmentRepository;

    public DataMatrixService(DataMatrixRepository repository,
                             DataMatrixMapper mapper,
                             UserRepository userRepository,
                             EducationalEstablishmentRepository establishmentRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
    }

    private String cleanString(String value) {
        if (value == null) return null;
        String cleaned = value.replaceAll("^\"+|\"+$", "");
        cleaned = cleaned.replace("\\\"", "\"");
        return cleaned.trim();
    }

    private String serializeDataValue(Object dataValue) {
        if (dataValue == null) return null;
        return dataValue.toString();
    }

    @Transactional(readOnly = true)
    public List<DataMatrixDTO> getAll() {
        log.debug("Request to get all data matrices");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DataMatrixDTO getById(Long id) {
        log.debug("Request to get data matrix by id: {}", id);
        DataMatrix entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with id: " + id));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public DataMatrixDTO getByClientKey(String clientKey) {
        String cleanedKey = cleanString(clientKey);
        log.debug("Request to get data matrix by clientKey: {}", cleanedKey);
        DataMatrix entity = repository.findByClientKey(cleanedKey)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with clientKey: " + cleanedKey));
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<DataMatrixDTO> getPendingSync() {
        log.debug("Request to get pending sync data matrices");
        return repository.findBySyncStatus(SyncStatus.PENDING).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DataMatrixDTO> getPendingSyncByUser(Long userId) {
        log.debug("Request to get pending sync by user: {}", userId);
        return repository.findBySyncStatusAndUser_Id(SyncStatus.PENDING, userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DataMatrixDTO> getPendingSyncByEstablishment(Long establishmentId) {
        log.debug("Request to get pending sync by establishment: {}", establishmentId);
        return repository.findBySyncStatusAndEstablishment_Id(SyncStatus.PENDING, establishmentId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countPendingSync() {
        log.debug("Request to count pending sync data matrices");
        return repository.countBySyncStatus(SyncStatus.PENDING);
    }

    public DataMatrixDTO create(DataMatrixRequestDTO request) {
        String cleanedKey = cleanString(request.getClientKey());
        String cleanedDataType = cleanString(request.getDataType());

        log.debug("Request to create data matrix with clientKey: {}", cleanedKey);

        if (repository.findByClientKey(cleanedKey).isPresent()) {
            throw new ConflictException("ClientKey already exists: " + cleanedKey);
        }

        DataMatrix entity = mapper.toEntity(request);
        entity.setClientKey(cleanedKey);
        entity.setDataValue(serializeDataValue(request.getDataValue()));

        if (cleanedDataType != null) {
            entity.setDataType(DataType.fromValue(cleanedDataType));
        }

        if (request.getUserId() != null) {
            entity.setUser(userRepository.findById(request.getUserId().longValue())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId())));
        }

        if (request.getEstablishmentId() != null) {
            entity.setEstablishment(establishmentRepository.findById(request.getEstablishmentId().longValue())
                    .orElseThrow(() -> new NotFoundException("Establishment not found with id: " + request.getEstablishmentId())));
        }

        try {
            DataMatrix saved = repository.save(entity);
            log.debug("Data matrix created with id: {}", saved.getId());
            return mapper.toDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ClientKey already exists: " + cleanedKey);
        }
    }

    public DataMatrixDTO update(String clientKey, DataMatrixRequestDTO request) {
        String cleanedKey = cleanString(clientKey);
        String cleanedDataType = cleanString(request.getDataType());

        log.debug("Request to update data matrix with clientKey: {}", cleanedKey);

        DataMatrix entity = repository.findByClientKey(cleanedKey)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with clientKey: " + cleanedKey));

        entity.setDataValue(serializeDataValue(request.getDataValue()));

        if (cleanedDataType != null) {
            entity.setDataType(DataType.fromValue(cleanedDataType));
        }

        if (request.getUserId() != null) {
            entity.setUser(userRepository.findById(request.getUserId().longValue())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId())));
        }

        if (request.getEstablishmentId() != null) {
            entity.setEstablishment(establishmentRepository.findById(request.getEstablishmentId().longValue())
                    .orElseThrow(() -> new NotFoundException("Establishment not found with id: " + request.getEstablishmentId())));
        }

        entity.setVersion(entity.getVersion() + 1);
        entity.setSyncStatus(SyncStatus.PENDING);

        DataMatrix updated = repository.save(entity);
        log.debug("Data matrix updated for clientKey: {}", cleanedKey);
        return mapper.toDto(updated);
    }

    public DataMatrixDTO updateSyncStatus(String clientKey, String status) {
        String cleanedKey = cleanString(clientKey);
        String cleanedStatus = cleanString(status);

        log.debug("Request to update sync status for clientKey: {} to {}", cleanedKey, cleanedStatus);

        DataMatrix entity = repository.findByClientKey(cleanedKey)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with clientKey: " + cleanedKey));

        entity.setSyncStatus(SyncStatus.fromValue(cleanedStatus));
        if (SyncStatus.SYNCED.equals(entity.getSyncStatus())) {
            entity.setSyncedAt(LocalDateTime.now());
        }

        DataMatrix updated = repository.save(entity);
        log.debug("Sync status updated for clientKey: {}", cleanedKey);
        return mapper.toDto(updated);
    }

    public DataMatrixDTO updateSyncStatusWithError(String clientKey, String errorMessage) {
        String cleanedKey = cleanString(clientKey);
        log.debug("Request to update sync status with error for clientKey: {}", cleanedKey);

        DataMatrix entity = repository.findByClientKey(cleanedKey)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with clientKey: " + cleanedKey));

        entity.setSyncStatus(SyncStatus.FAILED);
        entity.setErrorMessage(errorMessage);

        DataMatrix updated = repository.save(entity);
        log.debug("Sync status updated with error for clientKey: {}", cleanedKey);
        return mapper.toDto(updated);
    }

    public void delete(Long id) {
        log.debug("Request to delete data matrix: {}", id);
        DataMatrix entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with id: " + id));
        repository.delete(entity);
        log.debug("Data matrix deleted with id: {}", id);
    }

    public void deleteByClientKey(String clientKey) {
        String cleanedKey = cleanString(clientKey);
        log.debug("Request to delete data matrix by clientKey: {}", cleanedKey);
        DataMatrix entity = repository.findByClientKey(cleanedKey)
                .orElseThrow(() -> new NotFoundException("Data matrix not found with clientKey: " + cleanedKey));
        repository.delete(entity);
        log.debug("Data matrix deleted with clientKey: {}", cleanedKey);
    }
    public int cleanOldSyncedData(LocalDateTime cutoffDate) {
        log.debug("Request to clean old synced data before: {}", cutoffDate);
        int deleted = repository.deleteSyncedOlderThan(SyncStatus.SYNCED, cutoffDate);
        log.debug("Deleted {} old synced data matrices", deleted);
        return deleted;
    }
}