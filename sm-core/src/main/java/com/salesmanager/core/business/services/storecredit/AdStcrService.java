package com.salesmanager.core.business.services.storecredit;

import com.salesmanager.core.model.storecredit.AdStcr;

import java.util.List;

public interface AdStcrService {
    List<AdStcr> getAllAdStcr();
    void saveAdStcr(AdStcr adstcr);
    AdStcr getAdStcrById(long id);
    void deleteAdStcrById(long id);
}
