package com.sfar.livrili.Domains.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @DiscriminatorValue("DELIVERY_PERSON")
@Table(name = "delivery_persons")
@EqualsAndHashCode(callSuper = true)
public class DeliveryPerson extends User {

    private float rating;
    private int ratingCount;

    @OneToMany(mappedBy = "deliveryPerson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Offer> offers = new ArrayList<>();

}
