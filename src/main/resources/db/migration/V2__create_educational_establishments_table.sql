CREATE TABLE IF NOT EXISTS educational_establishments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    delegation_region VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_establishments_delegation_region ON educational_establishments(delegation_region);
CREATE INDEX idx_establishments_department ON educational_establishments(department);
