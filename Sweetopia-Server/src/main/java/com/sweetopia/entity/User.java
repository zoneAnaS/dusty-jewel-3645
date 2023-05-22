package com.sweetopia.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 3,max = 20)
	private String userName;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{6,12}$")
	private String userPassword;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<Order> orders=new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<>();

	@Email
	private String email;
	@Enumerated(EnumType.STRING)
	private Role role;
	

}