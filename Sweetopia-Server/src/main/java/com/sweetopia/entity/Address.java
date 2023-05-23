package com.sweetopia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addId;
    @Pattern(regexp = "^[a-zA-Z]{3,20}$")
    private String street;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{3,20}$")
    private String city;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{3,20}$")
    private String state;
    @NotNull
    @Pattern(regexp = "^[0-9]{5}$")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonIgnore
    private User user;


}
