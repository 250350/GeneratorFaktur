package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class InvoiceRequest {

    @NotBlank(message = "companyNameSeller is mandatory")
    private String companyNameSeller;

    @NotBlank(message = "addressSeller is mandatory")
    private String addressSeller;

    @NotBlank(message = "postalCodeAndCitySeller is mandatory")
    private String postalCodeAndCitySeller;

    @Pattern(regexp = "^([0-9]{8}[A-Z]|[A-Z][0-9]{7}[A-Z]|[A-Z][0-9]{8})$")
    @NotBlank(message = "nipSeller is mandatory")
    private String nipSeller;

    @NotBlank(message = "companyNameBuyer is mandatory")
    private String companyNameBuyer;

    @NotBlank(message = "addressBuyer is mandatory")
    private String addressBuyer;

    @NotBlank(message = "postalCodeAndCityBuyer is mandatory")
    private String postalCodeAndCityBuyer;

//    Dla Polskiej Wersji:
//    @Size(min = 13, max = 13, message = "nipBuyer musi mieć dokładnie 10 cyfr")
//    @NotBlank(message = "nipBuyer is mandatory")
//    private String nipBuyer;

    @Pattern(regexp = "^([0-9]{8}[A-Z]|[A-Z][0-9]{7}[A-Z]|[A-Z][0-9]{8})$")
    @NotBlank(message = "nipBuyer is mandatory")
    private String nipBuyer;

    @NotBlank(message = "stawkaVAT is mandatory")
    @NotNull
    private String stawkaVAT;

    @Min(1)
    private int invoiceNumber;

    @Pattern(regexp = "\\d{26}", message = "niepoprawny IBAN")
    @NotBlank
    private String bankAccountNumber;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date completionOfServiceDate;

    @Pattern(regexp = "^$|^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}[A-Z0-9]{3}$")
    private String swift;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;

    private List<InvoiceItem> items;

    public void getAllFields() {
        System.out.println("companyNameBuyer: " + companyNameBuyer);
        System.out.println("addressBuyer: " + addressBuyer);
        System.out.println("postalCodeAndCityBuyer: " + postalCodeAndCityBuyer);
        System.out.println("nipBuyer: " + nipBuyer);
        System.out.println("stawkaVAT: " + stawkaVAT);
        System.out.println("completionOfServiceDate: " + completionOfServiceDate);
    }

}
