package pl.comp.generatorfaktur.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sellers")
@Getter
@Setter
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;

    private String businessName;

    private String stripeAccountId;

    private Boolean stripeOnboarded = false;
}
