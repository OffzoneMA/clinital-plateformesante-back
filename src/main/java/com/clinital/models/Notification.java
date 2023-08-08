package com.clinital.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_notif;
	private String title;
	private String message;
	private Date createdAt;
	private String url;
	private boolean isRead;
	@ManyToOne
	@JoinColumn(name = "id")
	private User user;

	public Notification() {
	}

}
