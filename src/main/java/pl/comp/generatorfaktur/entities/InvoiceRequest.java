package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class InvoiceRequest {

    @NotBlank(message = "Nombre de empresa es obligatoria")
    private String companyNameSeller;

    @NotBlank(message = "La dirección es obligatoria")
    private String addressSeller;

    @NotBlank(message = "Código postal y ciudad es obligatoria")
    private String postalCodeAndCitySeller;

    @Pattern(regexp = "^([0-9]{8}[A-Z]|[A-Z][0-9]{7}[A-Z]|[A-Z][0-9]{8})$",
            message = "NIF inválido. Ejemplo: 12345678Z o X1234567L")
    @NotBlank(message = "NIF es obligatoria")
    private String nipSeller;

    @NotBlank(message = "Nombre de empresa es obligatoria")
    private String companyNameBuyer;

    @NotBlank(message = "La dirección es obligatoria")
    private String addressBuyer;

    @NotBlank(message = "Código postal y ciudad es obligatoria")
    private String postalCodeAndCityBuyer;

//    Dla Polskiej Wersji:
//    @Size(min = 13, max = 13, message = "nipBuyer musi mieć dokładnie 10 cyfr")
//    @NotBlank(message = "nipBuyer is mandatory")
//    private String nipBuyer;

    @Pattern(regexp = "^([0-9]{8}[A-Z]|[A-Z][0-9]{7}[A-Z]|[A-Z][0-9]{8})$",
            message = "NIF inválido. Ejemplo: 12345678Z o X1234567L")
    @NotBlank(message = "NIF es obligatoria")
    private String nipBuyer;

    @NotBlank(message = "IVA es obligatoria")
    @NotNull(message = "IVA es obligatoria")
    private String stawkaVAT;

    @NotNull
    private double irpf;

    @Min(value = 1, message = "El número mínimo de facturas es 1")
    @Positive(message = "El número de factura debe ser positivo")
    private int invoiceNumber;

    @Pattern(regexp = "\\d{26}", message = "IBAN incorrecto")
    @NotBlank(message = "IBAN es obligatoria")
    private String bankAccountNumber;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date completionOfServiceDate;

    @Pattern(regexp = "^$|^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}[A-Z0-9]{3}$",
            message = "SWIFT incorrecto")
    private String swift;

    @NotNull(message = "La fecha es obligatoria")
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
