package com.ecamschool.regional.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_matrices")
public class DataMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_key", nullable = false, unique = true, length = 255)
    private String clientKey;

    @Column(name = "data_value", nullable = false, columnDefinition = "TEXT")
    private String dataValue;

    @Column(name = "sync_status", nullable = false, length = 20)
    private String syncStatus = "PENDING";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "data_type", length = 50)
    private String dataType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id")
    private EducationalEstablishment establishment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (syncStatus == null) {
            syncStatus = "PENDING";
        }
        if (version == null) {
            version = 1;
        }
    }
}