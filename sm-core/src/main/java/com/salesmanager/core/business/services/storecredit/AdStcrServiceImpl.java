package com.salesmanager.core.business.services.storecredit;

import com.salesmanager.core.business.repositories.storecredit.AdStcrRepository;
import com.salesmanager.core.model.storecredit.AdStcr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdStcrServiceImpl implements AdStcrService {
    @Autowired
    private AdStcrRepository adStcrRepository;

    @Override
    public List<AdStcr> getAllAdStcr() {
        return adStcrRepository.findAll();
    }

    @Override
    public void saveAdStcr(AdStcr adstcr) {
        this.adStcrRepository.save(adstcr);
    }

    @Override
    public AdStcr getAdStcrById(long id) {
        Optional<AdStcr> optional = adStcrRepository.findById(id);
        AdStcr adstcr = null;
        if (optional.isPresent()) {
            adstcr = optional.get();
        } else {
            throw new RuntimeException(" AdStcr not found for id :: " + id);
        }
        return adstcr;
    }

    @Override
    public void deleteAdStcrById(long id) {
        this.adStcrRepository.deleteById(id);
    }
}