package pl.comp.generatorfaktur.controllers;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.comp.generatorfaktur.entities.InvoiceEntity;
import pl.comp.generatorfaktur.repositories.InvoiceRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final InvoiceRepository invoiceRepository;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleWebhooks(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader
    ) throws Exception {

        Event event = Webhook.constructEvent(
                payload,
                signatureHeader,
                endpointSecret
        );

        System.out.println("EVENT: " + event.getType());

        if ("checkout.session.completed".equals(event.getType())) {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);

            String invoiceId = root
                    .path("data")
                    .path("object")
                    .path("metadata")
                    .path("invoiceId")
                    .asText();

            System.out.println("INVOICE ID: " + invoiceId);

            InvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow();

            invoice.setStatus("PAID");
            invoiceRepository.save(invoice);

            System.out.println("INVOICE PAID: " + invoiceId);
        }

        return ResponseEntity.ok("success");
    }
}
