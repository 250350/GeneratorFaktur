package pl.comp.generatorfaktur.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.comp.generatorfaktur.dto.InvoiceRequest;
import pl.comp.generatorfaktur.entities.InvoiceEntity;
import pl.comp.generatorfaktur.mapper.InvoiceMapper;
import pl.comp.generatorfaktur.services.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public InvoiceEntity create(@RequestBody InvoiceRequest request) {

        InvoiceEntity entity = InvoiceMapper.toEntity(request);
        return invoiceService.save(entity);
    }

    @GetMapping("/{id}")
    public  InvoiceEntity get(@PathVariable String id) {
        return invoiceService.getById(id);
    }
}
