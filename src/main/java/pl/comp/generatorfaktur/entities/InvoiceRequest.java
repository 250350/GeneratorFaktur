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


    @Size(min = 13, max = 13, message = "NIP musi mieć dokładnie 10 cyfr")
    @NotBlank(message = "nip is mandatory")
    private String nip;

    @NotBlank(message = "stawkaVAT is mandatory")
    @NotNull
    private String stawkaVAT;

    @Min(1)
    private int invoiceNumber;

    @NotBlank
    @DateTimeFormat
    private String completionOfServiceDate;

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
