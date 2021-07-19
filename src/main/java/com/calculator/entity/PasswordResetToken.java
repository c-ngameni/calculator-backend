package com.calculator.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.calculator.utils.DateUtils;

import lombok.Data;

@Entity
@Data
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "token_id")
	private Long id;

	private String token;

	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
	private User user;

	@NotNull
	private Date expiryDate;

	public PasswordResetToken() {

		super();
	}

	public PasswordResetToken(User user, String token) {

		this.user = user;
		this.token = token;
	}

	@PrePersist
	public void initExpiryDate() {

		LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
		this.setExpiryDate(DateUtils.convertToDate(expiryDate));
	}

}
