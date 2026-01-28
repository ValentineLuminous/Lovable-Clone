package com.BlueFlare.Lovable.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @Column(unique = true)

    String stripePriceId;
    Integer maxTokenPerDay;
    Integer maxPreviews;
    Integer maxProjects;
    Boolean unlimitedAi;

    Boolean active;
}
