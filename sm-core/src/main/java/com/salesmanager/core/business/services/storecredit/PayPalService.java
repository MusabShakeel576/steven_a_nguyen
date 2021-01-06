package com.salesmanager.core.business.services.storecredit;

import com.salesmanager.core.model.storecredit.PayPalStcr;

import java.util.List;

public interface PayPalService {
    List<PayPalStcr> getAllPayPalStcr();
    void savePayPalStcr(PayPalStcr paypalstcr);
    PayPalStcr getPayPalStcrById(long id);
    void deletePayPalStcrById(long id);
}
