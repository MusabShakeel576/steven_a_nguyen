package com.salesmanager.core.business.services.storecredit;

import com.salesmanager.core.business.repositories.storecredit.PayPalRepository;
import com.salesmanager.core.model.storecredit.PayPalStcr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayPalServiceImpl implements PayPalService {
    @Autowired
    private PayPalRepository payPalRepository;

    @Override
    public List<PayPalStcr> getAllPayPalStcr() {
        return payPalRepository.findAll();
    }

    @Override
    public void savePayPalStcr(PayPalStcr paypalstcr) {
        this.payPalRepository.save(paypalstcr);
    }

    @Override
    public PayPalStcr getPayPalStcrById(long id) {
        Optional<PayPalStcr> optional = payPalRepository.findById(id);
        PayPalStcr paypalstcr = null;
        if (optional.isPresent()) {
            paypalstcr = optional.get();
        } else {
            throw new RuntimeException(" PayPal not found for id :: " + id);
        }
        return paypalstcr;
    }

    @Override
    public void deletePayPalStcrById(long id) {
        this.payPalRepository.deleteById(id);
    }
}
