package pl.comp.generatorfaktur.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StripeConnectPageController {

    @GetMapping("/connect-account")
    public String connectPage(
            @RequestParam String invoiceId,
            Model model
    ) {

        model.addAttribute("invoiceId", invoiceId);

        return "stripe/connect";
    }
}