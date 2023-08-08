package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinital.models.TypeDocument;
@Repository
public interface TypeDocumentRepository extends JpaRepository<TypeDocument, Long> {

}
