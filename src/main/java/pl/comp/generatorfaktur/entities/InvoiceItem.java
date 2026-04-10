package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InvoiceItem {

    private String description;

    @Min(1)
    private int amount;

    @Positive(message = "netPrice is mandatory")
    private double netPrice;
}
