package com.clinital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinital.models.Rendezvous;
import com.clinital.models.SharingHistory;

@Repository
public interface sharingHistoryrepository extends JpaRepository<SharingHistory, Long> {

    @Modifying
	@Query(value = "DELETE FROM sharing_history WHERE id = :idshare", nativeQuery = true)
	void Deletsharehstory(@Param("idshare") Long idshare) throws Exception;
    
}
