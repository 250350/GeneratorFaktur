package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.*;
import lombok.Data;

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

    private String description;

    @Min(1)
    private int amount;

    @Positive(message = "netPrice is mandatory")
    private double netPrice;

    @NotBlank(message = "stawkaVAT is mandatory")
    private String stawkaVAT;

    public void getAllFields() {
        System.out.println("companyName: " + companyName);
        System.out.println("address: " + address);
        System.out.println("postalCodeAndCity: " + postalCodeAndCity);
        System.out.println("nip: " + nip);
        System.out.println("description: " + description);
        System.out.println("amount: " + amount);
        System.out.println("netPrice: " + netPrice);
        System.out.println("stawkaVAT: " + stawkaVAT);

    }

}
