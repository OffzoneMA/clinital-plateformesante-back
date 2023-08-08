package com.clinital.models;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.Version;

import com.clinital.enums.ERole;
import com.clinital.enums.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "users")
//		uniqueConstraints = { 
//			@UniqueConstraint(columnNames = "username"),
//			@UniqueConstraint(columnNames = "email") 
//		})
//@Inheritance(strategy =InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING, length = 20)
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String telephone;
	@JsonIgnore
	private String password;
	private boolean isEnabled = false;
	

	@Column(nullable = false)
	private Boolean emailVerified = false;

	// @ManyToMany
	// @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	// private Set<Role> roles = new HashSet<>();

	
	@Enumerated(EnumType.STRING)
	private ERole role;

	@Enumerated(EnumType.STRING)
	private ProviderEnum provider;

	@CreationTimestamp
	@Column(name = "creation_date_time")
	private Date creationDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login")
	private Date lastLogin;

	// @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
	// private List<Patient> patients;

	public User() {
		super();
	}

	public User(@NotBlank @Size(max = 50) @Email String email, @NotNull String telephone,
			@NotBlank @Size(max = 120) String password) {
		super();
		this.email = email;
		this.telephone = telephone;
		this.password = password;
	}

	public User(@NotBlank @Size(max = 50) @Email String email, @NotNull String telephone,
			@NotBlank @Size(max = 120) String password, ERole role) {
		super();
		this.email = email;
		this.telephone = telephone;
		this.password = password;
		this.role = role;
	}

}
