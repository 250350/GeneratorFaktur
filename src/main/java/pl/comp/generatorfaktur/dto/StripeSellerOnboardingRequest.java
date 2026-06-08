package pl.comp.generatorfaktur.dto;

public record StripeSellerOnboardingRequest(
    String email,
    String country,
    String businessName
) {}
