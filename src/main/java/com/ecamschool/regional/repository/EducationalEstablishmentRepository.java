package com.ecamschool.regional.repository;

import com.ecamschool.regional.entity.EducationalEstablishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EducationalEstablishmentRepository extends JpaRepository<EducationalEstablishment, Long> {
    Optional<EducationalEstablishment> findByCode(String code);
    List<EducationalEstablishment> findByDelegationRegion(String delegationRegion);
    List<EducationalEstablishment> findByDepartment(String department);
    List<EducationalEstablishment> findByIsActiveTrue();
}