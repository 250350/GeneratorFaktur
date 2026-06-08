package pl.comp.generatorfaktur.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.comp.generatorfaktur.services.StripeService;

@Controller
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping("/payment-link/{invoiceId}")
    public String generateLink(
            @PathVariable String invoiceId
    ) throws Exception {

        String url = stripeService
                .createCheckout(invoiceId);

        return "redirect:" + url;
    }
}