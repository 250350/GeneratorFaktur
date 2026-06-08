package pl.comp.generatorfaktur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.comp.generatorfaktur.entities.InvoiceEntity;
import pl.comp.generatorfaktur.repositories.InvoiceRepository;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceEntity save(InvoiceEntity invoice) {
        return invoiceRepository.save(invoice);
    }

    public InvoiceEntity getById(String id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}
