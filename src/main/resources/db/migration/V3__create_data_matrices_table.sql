CREATE TABLE IF NOT EXISTS data_matrices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_key VARCHAR(255) NOT NULL UNIQUE,
    data_value TEXT NOT NULL,
    sync_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    synced_at TIMESTAMP NULL,
    version INT NOT NULL DEFAULT 1,
    error_message TEXT NULL,
    data_type VARCHAR(50) NULL,
    user_id BIGINT NULL,
    establishment_id BIGINT NULL,
    CONSTRAINT fk_data_matrices_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_data_matrices_establishment FOREIGN KEY (establishment_id) REFERENCES educational_establishments(id) ON DELETE SET NULL
);

CREATE INDEX idx_data_matrices_sync_status ON data_matrices(sync_status);
CREATE INDEX idx_data_matrices_user_id ON data_matrices(user_id);
CREATE INDEX idx_data_matrices_establishment_id ON data_matrices(establishment_id);
CREATE INDEX idx_data_matrices_client_key ON data_matrices(client_key);
