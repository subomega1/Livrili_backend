package com.sfar.livrili.Domains.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//@DiscriminatorValue("CLIENT")
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true)

public class Client  extends User{

    @OneToMany(mappedBy = "client" ,cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference // This is the "forward" reference

    private List<Pack> packList = new ArrayList<>();



}
