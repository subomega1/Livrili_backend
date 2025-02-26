package com.sfar.livrili.Domains.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "packs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pack {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String weight;

    @Column(nullable = false)
    private String pickUpLocation;

    @Column(nullable = false)
    private String dropOffLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false ,name = "client_id")
    @JsonBackReference // This is the "back" reference
    private Client client;

    @OneToMany(mappedBy = "pack",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();



    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, weight, pickUpLocation, dropOffLocation, status, createdAt, updatedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Pack other = (Pack) obj;
        return Objects.equals(id, other.id) && Objects.equals(description, other.description);
    }
}
