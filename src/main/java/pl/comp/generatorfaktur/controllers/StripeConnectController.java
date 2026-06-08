package pl.comp.generatorfaktur.controllers;

import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.comp.generatorfaktur.entities.SellerEntity;
import pl.comp.generatorfaktur.repositories.SellerRepository;

@Controller
@RequestMapping("/stripe/connect")
public class StripeConnectController {

    @Value("${stripe.secret.key}")
    private String stripeKey;

    private final SellerRepository sellerRepository;

    public StripeConnectController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @GetMapping("/onboard")
    public String onboardSeller(
            @RequestParam String email,
            @RequestParam String businessName,
            @RequestParam String country,
            @RequestParam String invoiceId
    ) throws Exception {

        Stripe.apiKey = stripeKey;

        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .setCountry(country)
                        .setEmail(email)
                        .setBusinessType(
                                AccountCreateParams.BusinessType.COMPANY
                        )
                        .build();

        Account account = Account.create(params);

        SellerEntity seller = new SellerEntity();

        seller.setEmail(email);
        seller.setBusinessName(businessName);
        seller.setStripeAccountId(account.getId());

        sellerRepository.save(seller);

        AccountLinkCreateParams linkParams =
                AccountLinkCreateParams.builder()
                        .setAccount(account.getId())
                        .setRefreshUrl(
                                "http://localhost:8080/connect-account?invoiceId=" + invoiceId
                        )
                        .setReturnUrl(
                                "http://localhost:8080/payment-link/"
                                        + invoiceId + "/"
                                        + seller.getId()
                        )
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();

        AccountLink accountLink =
                AccountLink.create(linkParams);

        return "redirect:" + accountLink.getUrl();
    }
}