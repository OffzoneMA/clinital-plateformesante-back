package com.clinital.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "types_doc")
@Data
public class TypeDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_typedoc")
	private Long typeDocId;

	@Column(name = "code_type")
	private String codeType;

	@Column(name = "type_doc")
	private String docType;

}
