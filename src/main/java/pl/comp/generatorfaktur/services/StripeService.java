package pl.comp.generatorfaktur.services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.comp.generatorfaktur.entities.InvoiceEntity;
import pl.comp.generatorfaktur.entities.SellerEntity;
import pl.comp.generatorfaktur.repositories.InvoiceRepository;
import pl.comp.generatorfaktur.repositories.SellerRepository;

import java.math.RoundingMode;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeKey;

    private final InvoiceRepository invoiceRepository;
    private final SellerRepository sellerRepository;

    public StripeService(InvoiceRepository invoiceRepository, SellerRepository sellerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.sellerRepository = sellerRepository;
    }

    public String createCheckout(String invoiceId) throws Exception {

        Stripe.apiKey = stripeKey;
        InvoiceEntity invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        SellerEntity seller = invoice.getSeller();

        invoice.setSeller(seller);
        invoiceRepository.save(invoice);

        if (seller == null) {
            throw new RuntimeException("Invoice has no seller");
        }

        if (seller.getStripeAccountId() == null) {
            throw new RuntimeException("Seller has no Stripe account");
        }

        long totalLong = invoice.getAmount()
                .movePointRight(2)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        long fee = Math.round(totalLong * 0.05);

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)

                        .setSuccessUrl("http://localhost:8080/payment-success")
                        .setCancelUrl("http://localhost:8080/payment-cancel")

                        .putMetadata("invoiceId", invoiceId)
                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .setApplicationFeeAmount(fee)
                                        .setTransferData(
                                                SessionCreateParams.PaymentIntentData.TransferData.builder()
                                                        .setDestination(seller.getStripeAccountId())
                                                        .build()
                                        )
                                        .build()
                        )

                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)

                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(invoice.getCurrency())

                                                        .setUnitAmount(totalLong)

                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Factura " + invoice.getInvoiceNumber())
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);

        invoice.setStripeCheckoutSessionId(session.getId());

        invoiceRepository.save(invoice);

        return session.getUrl();
    }
}