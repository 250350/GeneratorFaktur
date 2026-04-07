package pl.comp.generatorfaktur.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.comp.generatorfaktur.entities.InvoiceRequest;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
public class HomeController {
    private final TemplateEngine templateEngine;
    private double vat;
    private double vatValue;
    private double grossPrice;
    private double netPrice;

    public HomeController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/show-invoice")
    public String showInvoice(
            @Valid @ModelAttribute InvoiceRequest invoiceRequest,
            BindingResult result,
            HttpSession session,
            Model model

    ) {
        setVAT(invoiceRequest.getStawkaVAT());
        double netPriceForOne = invoiceRequest.getNetPrice();
        netPrice = invoiceRequest.getNetPrice();
        setNetPrice(calculateNetPrice(netPrice, invoiceRequest.getAmount()));
        invoiceRequest.setNetPrice(netPrice);
        setVatValue(calculateValueVAT(invoiceRequest.getNetPrice()));
        setGrossPrice(calculateGrossPrice(invoiceRequest.getNetPrice()));
        session.setAttribute("companyName", invoiceRequest.getCompanyName());
        session.setAttribute("address", invoiceRequest.getAddress());
        session.setAttribute("postalCodeAndCity", invoiceRequest.getPostalCodeAndCity());
        session.setAttribute("nip", invoiceRequest.getNip());
        session.setAttribute("description", invoiceRequest.getDescription());
        session.setAttribute("amount", invoiceRequest.getAmount());
        session.setAttribute("netPriceForOne", netPriceForOne);
        session.setAttribute("netPrice", invoiceRequest.getNetPrice());
        session.setAttribute("stawkaVAT", invoiceRequest.getStawkaVAT());
        session.setAttribute("vatValue", vatValue);
        session.setAttribute("grossPrice", grossPrice);

        model.addAttribute("companyName", invoiceRequest.getCompanyName());
        model.addAttribute("address", invoiceRequest.getAddress());
        model.addAttribute("postalCodeAndCity", invoiceRequest.getPostalCodeAndCity());
        model.addAttribute("nip", invoiceRequest.getNip());
        model.addAttribute("description", invoiceRequest.getDescription());
        model.addAttribute("amount", invoiceRequest.getAmount());
        model.addAttribute("netPriceForOne", netPriceForOne);
        model.addAttribute("netPrice", invoiceRequest.getNetPrice());
        model.addAttribute("stawkaVAT", invoiceRequest.getStawkaVAT());
        model.addAttribute("vatValue", vatValue);
        model.addAttribute("grossPrice", grossPrice);

        if (result.hasErrors()) {
            invoiceRequest.getAllFields();
            return "index";
        }

        return "webpage";
    }

@GetMapping("/generate-invoice")
public ResponseEntity<byte[]> generate(HttpSession session) {

    String companyName = (String) session.getAttribute("companyName");
    String address = (String) session.getAttribute("address");
    String postalCodeAndCity = (String) session.getAttribute("postalCodeAndCity");
    String nip = (String) session.getAttribute("nip");
    String description = (String) session.getAttribute("description");
    Integer amountObj = (Integer) session.getAttribute("amount");
    int amount = amountObj != null ? amountObj : 0;
    Double netPriceForOneObj = (Double) session.getAttribute("netPriceForOne");
    double netPriceForOne = netPriceForOneObj != null ? netPriceForOneObj : 0.0;
    Double netPriceObj = (Double) session.getAttribute("netPrice");
    double netPrice = netPriceObj != null ? netPriceObj : 0.0;
    String stawkaVAT = (String) session.getAttribute("stawkaVAT");

    Context context = new Context();
    context.setVariable("companyName", companyName);
    context.setVariable("address", address);
    context.setVariable("postalCodeAndCity", postalCodeAndCity);
    context.setVariable("nip", nip);
    context.setVariable("description", description);
    context.setVariable("amount", amount);
    context.setVariable("netPriceForOne", netPriceForOne);
    context.setVariable("netPrice", netPrice);
    context.setVariable("stawkaVAT", stawkaVAT);
    context.setVariable("vatValue", vatValue);
    context.setVariable("grossPrice", grossPrice);

    String html = templateEngine.process("invoice-pdf", context);

    String css = loadCss();
    html = html.replace("</head>", "<style>" + css + "</style></head>");

    byte[] pdf = generatePdfWithPuppeteer(html);

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=fakturaa.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
}

    private String   loadCss() {
        try (InputStream is = getClass().getResourceAsStream("/static/style.css")) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Nie można załadować CSS", e);
        }
    }

    private byte[] generatePdfWithPuppeteer(String html) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(
                    Map.of("html", html)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3001/pdf"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<byte[]> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double calculateGrossPrice(double netPrice) {
        return netPrice + vatValue;
    }

    private double calculateValueVAT(double netPrice) {
        return netPrice * vat;
    }

    private double calculateNetPrice(double netPrice, int amount) {
        return netPrice * amount;
    }

    private void setVAT(String stawkaVAT) {
        this.vat = Double.parseDouble(stawkaVAT) / 100.0;
    }

    public void setVatValue(double vatValue) {
        this.vatValue = Math.round(vatValue * 100.0) / 100.0;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = Math.round(netPrice * 100.0) / 100.0;

    }

    public void setGrossPrice(double grossPrice) {
        this.grossPrice = Math.round(grossPrice * 100.0) / 100.0;
    }
}
