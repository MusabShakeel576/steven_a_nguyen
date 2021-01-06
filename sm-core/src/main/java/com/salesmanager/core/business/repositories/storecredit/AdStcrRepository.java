package com.salesmanager.core.business.repositories.storecredit;

import com.salesmanager.core.model.storecredit.AdStcr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdStcrRepository extends JpaRepository<AdStcr, Long> {

}