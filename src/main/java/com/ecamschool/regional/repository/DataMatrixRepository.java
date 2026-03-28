package com.ecamschool.regional.repository;

import com.ecamschool.regional.entity.DataMatrix;
import com.ecamschool.regional.enums.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataMatrixRepository extends JpaRepository<DataMatrix, Long> {

    Optional<DataMatrix> findByClientKey(String clientKey);

    List<DataMatrix> findBySyncStatus(SyncStatus syncStatus);

    List<DataMatrix> findBySyncStatusAndUser_Id(SyncStatus syncStatus, Long userId);

    List<DataMatrix> findBySyncStatusAndEstablishment_Id(SyncStatus syncStatus, Long establishmentId);

    @Modifying
    @Transactional
    @Query("UPDATE DataMatrix d SET d.syncStatus = :status, d.syncedAt = :syncedAt WHERE d.clientKey = :clientKey")
    int updateSyncStatus(@Param("clientKey") String clientKey,
                         @Param("status") SyncStatus status,
                         @Param("syncedAt") LocalDateTime syncedAt);

    @Modifying
    @Transactional
    @Query("DELETE FROM DataMatrix d WHERE d.syncStatus = :status AND d.syncedAt < :cutoffDate")
    int deleteSyncedOlderThan(@Param("status") SyncStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);

    long countBySyncStatus(SyncStatus syncStatus);
}