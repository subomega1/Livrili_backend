package com.sfar.livrili.Domains.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DiscriminatorValue("DELIVERY_PERSON")
@EqualsAndHashCode(callSuper = true)
public class DeliveryPerson extends User {


    private float rating;

    @OneToMany(mappedBy = "deliveryPerson",cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();


}


