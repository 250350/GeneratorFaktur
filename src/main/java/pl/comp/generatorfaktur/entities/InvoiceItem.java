package pl.comp.generatorfaktur.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InvoiceItem {

    private String description;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Positive(message = "La cantidad debe ser positiva")
    private int amount;

    @Positive(message = "El precio neto tiene que ser positivo")
    private double netPrice;
}
