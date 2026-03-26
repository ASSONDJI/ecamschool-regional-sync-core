package com.ecamschool.regional.repository;

import com.ecamschool.regional.entity.DataMatrix;
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

    List<DataMatrix> findBySyncStatus(String syncStatus);

    List<DataMatrix> findBySyncStatusAndUser_Id(String syncStatus, Long userId);

    List<DataMatrix> findBySyncStatusAndEstablishment_Id(String syncStatus, Long establishmentId);

    @Modifying
    @Transactional
    @Query("UPDATE DataMatrix d SET d.syncStatus = :status, d.syncedAt = :syncedAt WHERE d.clientKey = :clientKey")
    int updateSyncStatus(@Param("clientKey") String clientKey,
                         @Param("status") String status,
                         @Param("syncedAt") LocalDateTime syncedAt);

    long countBySyncStatus(String syncStatus);
}