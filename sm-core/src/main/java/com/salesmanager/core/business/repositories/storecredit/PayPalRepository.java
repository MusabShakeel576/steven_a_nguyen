package com.salesmanager.core.business.repositories.storecredit;

import com.salesmanager.core.model.storecredit.PayPalStcr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayPalRepository extends JpaRepository<PayPalStcr, Long> {

}
