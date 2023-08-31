package com.github.hybusa.taskMSocial.repository;

import com.github.hybusa.taskMSocial.entity.DailyDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDomainRepository extends JpaRepository<DailyDomain, Long> {
    @Query(value = "COUNT * FROM daily_domains", nativeQuery = true)
    int countAll();
}

