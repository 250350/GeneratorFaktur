package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class InvoiceRequest {

    @NotBlank(message = "email is mandatory")
    private String companyName;

    @NotBlank(message = "address is mandatory")
    private String address;

    @NotBlank(message = "postalCodeAndCity is mandatory")
    private String postalCodeAndCity;

//    Dla Polskiej Wersji:
//    @Size(min = 13, max = 13, message = "NIP musi mieć dokładnie 10 cyfr")
//    @NotBlank(message = "nip is mandatory")
//    private String nip;

    @Pattern(regexp = "[A-Z][0-9]{7}[A-Z0-9]")
    @NotBlank(message = "nip is mandatory")
    private String nip;

    @NotBlank(message = "stawkaVAT is mandatory")
    @NotNull
    private String stawkaVAT;

    @Min(1)
    private int invoiceNumber;

    @Pattern(regexp = "\\d{26}", message = "niepoprawny IBAN")
    @NotBlank
    private String bankAccountNumber;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String completionOfServiceDate;

    @Pattern(regexp = "^$|^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}[A-Z0-9]{3}$")
    private String swift;

    @NotBlank
    @DateTimeFormat
    private String paymentDate;

    private List<InvoiceItem> items;

    public void getAllFields() {
        System.out.println("companyName: " + companyName);
        System.out.println("address: " + address);
        System.out.println("postalCodeAndCity: " + postalCodeAndCity);
        System.out.println("nip: " + nip);
        System.out.println("stawkaVAT: " + stawkaVAT);
    }

}
