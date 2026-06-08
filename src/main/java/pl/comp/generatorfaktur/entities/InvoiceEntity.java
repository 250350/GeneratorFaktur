package pl.comp.generatorfaktur.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
//@Entity
//@Table(name = "invoices")
//@Data
//public class InvoiceEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;
//
//    private String companyNameBuyer;
//    private String addressBuyer;
//    private String nipBuyer;
//
//    private BigDecimal amount;
//    private String currency = "EUR";
//
//    private double vatRate;
//    private double irpf;
//    private int invoiceNumber;
//
//    private String status = "UNPAID";
//
//    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
//    private List<InvoiceItemEntity> items;
//
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    private String stripeCheckoutSessionId;
//
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "seller_id")
//    private SellerEntity seller;


@Entity
@Table(name = "invoices")
@Data
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // SELLER

    private String companyNameSeller;
    private String addressSeller;
    private String postalCodeAndCitySeller;
    private String nipSeller;

    // BUYER

    private String companyNameBuyer;
    private String addressBuyer;
    private String postalCodeAndCityBuyer;
    private String nipBuyer;

    // TAXES

    private double vatRate;
    private double irpfRate;
    private boolean applyIRPF;

    // INVOICE

    private int invoiceNumber;

    private Date completionOfServiceDate;
    private Date paymentDate;

    // PAYMENT INFO

    private String bankAccountNumber;
    private String swift;

    // STRIPE

    private BigDecimal amount;

    private String currency = "EUR";

    private String status = "UNPAID";

    private String stripeCheckoutSessionId;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = true)
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    @OneToMany(mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<InvoiceItemEntity> items;
}