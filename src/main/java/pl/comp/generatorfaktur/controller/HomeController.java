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
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.comp.generatorfaktur.entities.InvoiceItem;
import pl.comp.generatorfaktur.entities.InvoiceRequest;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final TemplateEngine templateEngine;
//    private double vat;
//    private double vatValue;
//    private double grossPrice;
//    private double netPrice;

    public HomeController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "ok";
    }

    @RequestMapping("/")
    public String index() {
        return "landingPage";
    }

    @RequestMapping("/create-invoice")
    public String createInvoice() {
        return "spanish/index_es";
    }

    @RequestMapping("/show-invoice")
    public String showInvoice(
            @Valid @ModelAttribute InvoiceRequest invoiceRequest,
            BindingResult result,
            HttpSession session,
            Model model

    ) {
        if (result.hasErrors()) {
            invoiceRequest.getAllFields();
            return "spanish/index_es";
        }
//        setVAT(invoiceRequest.getStawkaVAT());
//        double netPriceForOne = invoiceRequest.getNetPrice();
//        netPrice = invoiceRequest.getNetPrice();
//        setNetPrice(calculateNetPrice(netPrice, invoiceRequest.getAmount()));
//        invoiceRequest.setNetPrice(netPrice);
//        setVatValue(calculateValueVAT(invoiceRequest.getNetPrice()));
//        setGrossPrice(calculateGrossPrice(invoiceRequest.getNetPrice()));

        double totalNet = 0;
        for (InvoiceItem item : invoiceRequest.getItems()) {
            double itemTotal = item.getNetPrice() * item.getAmount();
            totalNet += itemTotal;
        }

        double vat = Double.parseDouble(invoiceRequest.getStawkaVAT()) / 100.0;
        double vatValue = totalNet * vat;
        double grossPrice = totalNet + vatValue;

        session.setAttribute("companyNameSeller", invoiceRequest.getCompanyNameSeller());
        session.setAttribute("addressSeller", invoiceRequest.getAddressSeller());
        session.setAttribute("postalCodeAndCitySeller", invoiceRequest.getPostalCodeAndCitySeller());
        session.setAttribute("nipSeller", invoiceRequest.getNipSeller());
        session.setAttribute("companyNameBuyer", invoiceRequest.getCompanyNameBuyer());
        session.setAttribute("addressBuyer", invoiceRequest.getAddressBuyer());
        session.setAttribute("postalCodeAndCityBuyer", invoiceRequest.getPostalCodeAndCityBuyer());
        session.setAttribute("nipBuyer", invoiceRequest.getNipBuyer());
        session.setAttribute("items", invoiceRequest.getItems());
        session.setAttribute("stawkaVAT", vat);
        session.setAttribute("vatValue", vatValue);
        session.setAttribute("grossPrice", grossPrice);
        session.setAttribute("netPrice", totalNet);
        session.setAttribute("completionOfServiceDate", invoiceRequest.getCompletionOfServiceDate());
        session.setAttribute("today", LocalDate.now());
        session.setAttribute("invoiceNumber", invoiceRequest.getInvoiceNumber());
        session.setAttribute("paymentDate", invoiceRequest.getPaymentDate());
        session.setAttribute("bankAccountNumber", formatBankAccountNumber(invoiceRequest.getBankAccountNumber()));
        if (invoiceRequest.getSwift() != null) {
            session.setAttribute("swift", invoiceRequest.getSwift());
            model.addAttribute("swift", invoiceRequest.getSwift());
        }

        model.addAttribute("companyNameSeller", invoiceRequest.getCompanyNameSeller());
        model.addAttribute("addressSeller", invoiceRequest.getAddressSeller());
        model.addAttribute("postalCodeAndCitySeller", invoiceRequest.getPostalCodeAndCitySeller());
        model.addAttribute("nipSeller", invoiceRequest.getNipSeller());
        model.addAttribute("companyNameBuyer", invoiceRequest.getCompanyNameBuyer());
        model.addAttribute("addressBuyer", invoiceRequest.getAddressBuyer());
        model.addAttribute("postalCodeAndCityBuyer", invoiceRequest.getPostalCodeAndCityBuyer());
        model.addAttribute("nipBuyer", invoiceRequest.getNipBuyer());
        model.addAttribute("items", invoiceRequest.getItems());
        model.addAttribute("netPrice", String.format("%.2f", totalNet));
        model.addAttribute("vatValue", String.format("%.2f", vatValue));
        model.addAttribute("grossPrice", String.format("%.2f", grossPrice));
        model.addAttribute("stawkaVAT", vat);
        model.addAttribute("completionOfServiceDate", invoiceRequest.getCompletionOfServiceDate());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("invoiceNumber", invoiceRequest.getInvoiceNumber());
        model.addAttribute("paymentDate", invoiceRequest.getPaymentDate());
        model.addAttribute("bankAccountNumber", formatBankAccountNumber(invoiceRequest.getBankAccountNumber()));

        return "spanish/faktura_es";
    }

@GetMapping("/generate-invoice")
public ResponseEntity<byte[]> generate(HttpSession session) {

    String companyNameSeller = (String) session.getAttribute("companyNameSeller");
    String addressSeller = (String) session.getAttribute("addressSeller");
    String postalCodeAndCitySeller = (String) session.getAttribute("postalCodeAndCitySeller");
    String nipSeller = (String) session.getAttribute("nipSeller");
    String companyNameBuyer = (String) session.getAttribute("companyNameBuyer");
    String addressBuyer = (String) session.getAttribute("addressBuyer");
    String postalCodeAndCityBuyer = (String) session.getAttribute("postalCodeAndCityBuyer");
    String nipBuyer = (String) session.getAttribute("nipBuyer");
    Date completionOfServiceDate = (Date) session.getAttribute("completionOfServiceDate");
    Date paymentDate = (Date) session.getAttribute("paymentDate");
    String bankAccountNumber = (String) session.getAttribute("bankAccountNumber");
    int invoiceNumber = (int) session.getAttribute("invoiceNumber");
    LocalDate today = (LocalDate) session.getAttribute("today");
//    String description = (String) session.getAttribute("description");
//    Integer amountObj = (Integer) session.getAttribute("amount");
//    int amount = amountObj != null ? amountObj : 0;
//    Double netPriceForOneObj = (Double) session.getAttribute("netPriceForOne");
//    double netPriceForOne = netPriceForOneObj != null ? netPriceForOneObj : 0.0;
    List<InvoiceItem> items = (List<InvoiceItem>) session.getAttribute("items");
    Double netPriceObj = (Double) session.getAttribute("netPrice");
    double netPrice = netPriceObj != null ? netPriceObj : 0.0;
    Double vatObj = (Double) session.getAttribute("stawkaVAT");
    double stawkaVAT = vatObj != null ? vatObj : 0.0;

    Double vatValueObj = (Double) session.getAttribute("vatValue");
    double vatValue = vatValueObj != null ? vatValueObj : 0.0;
    Double grossPriceObj = (Double) session.getAttribute("grossPrice");
    double grossPrice = grossPriceObj != null ? grossPriceObj : 0.0;



    Context context = new Context();
    context.setVariable("companyNameSeller", companyNameSeller);
    context.setVariable("addressSeller", addressSeller);
    context.setVariable("postalCodeAndCitySeller", postalCodeAndCitySeller);
    context.setVariable("nipSeller", nipSeller);
    context.setVariable("companyNameBuyer", companyNameBuyer);
    context.setVariable("addressBuyer", addressBuyer);
    context.setVariable("postalCodeAndCityBuyer", postalCodeAndCityBuyer);
    context.setVariable("nipBuyer", nipBuyer);
//    context.setVariable("description", description);
//    context.setVariable("amount", amount);
//    context.setVariable("netPriceForOne", String.format("%.2f", netPriceForOne));
    context.setVariable("items", items);
    context.setVariable("netPrice", String.format("%.2f", netPrice));
    context.setVariable("stawkaVAT", stawkaVAT);
    context.setVariable("vatValue", String.format("%.2f", vatValue));
    context.setVariable("grossPrice", String.format("%.2f", grossPrice));
    context.setVariable("completionOfServiceDate", completionOfServiceDate);
    context.setVariable("paymentDate", paymentDate);
    context.setVariable("bankAccountNumber", bankAccountNumber);
    context.setVariable("today", today);
    context.setVariable("invoiceNumber", invoiceNumber);
//    context.setVariable("logoBase64", getBase64Image());
    if (session.getAttribute("swift") != null) {
        String swift = (String) session.getAttribute("swift");
        context.setVariable("swift", swift);
    }

    String html = templateEngine.process("spanish/faktura-pdf_es", context);

    byte[] pdf = generatePdfWithPuppeteer(html);

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=factura.pdf")
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
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .build();

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(
                    Map.of("html", html)
            );

            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:3001/pdf"))
                    .uri(URI.create("https://pdfservice-5rfv.onrender.com/pdf"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            System.out.println("WYSYŁAM REQUEST DO PDF SERVICE...");
            HttpResponse<byte[]> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );
            System.out.println("PDF HTML SIZE: " + html.length());
            System.out.println("FINAL URL: " + request.uri());
            System.out.println("STATUS: " + response.statusCode());
            System.out.println("JSON: " + json);
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getBase64Image() {
        try (InputStream is = getClass().getResourceAsStream("/static/navLogo.png")) {
            byte[] bytes = is.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String formatBankAccountNumber(String bankAccountNumber) {
        String clean =  bankAccountNumber.replaceAll("\\s+", "");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < clean.length(); i++) {
            if (i == 2 || i > 2 && (i - 2) % 4 == 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(clean.charAt(i));
        }

        return stringBuilder.toString();
    }
}
