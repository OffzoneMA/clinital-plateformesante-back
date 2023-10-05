package com.clinital.controllers;

import javax.validation.Valid;

import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.models.Medecin;
import com.clinital.models.SharingHistory;
import com.clinital.payload.request.SharingHistoryRequest;
import com.clinital.repository.sharingHistoryrepository;
import com.clinital.services.SharingHistoryServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/shares")
public class SharingHistoryController {

    @Autowired
    public SharingHistoryServiceImpl shareservices;
    @Autowired
    public sharingHistoryrepository sharingHistoryrepository;

    @PostMapping("/addshare")
    public ResponseEntity<?> AddShare(@Valid @RequestBody SharingHistoryRequest HistoryRequest) throws Exception{
        SharingHistory shareHistory=shareservices.addSharingHistory(HistoryRequest);
        return ResponseEntity.ok(shareHistory);
    }
     @PutMapping("/updateshare")
    public ResponseEntity<?> UpdateShare(@Valid @RequestBody SharingHistoryRequest HistoryRequest) throws Exception{
        return ResponseEntity.ok(shareservices.updateSharingHistory(HistoryRequest));
    }

    @PostMapping("/deleteteshare/{idshare}")
    public ResponseEntity<?> DeleteShare(@Valid @PathVariable Long idshare) throws Exception{
        return ResponseEntity.ok(shareservices.deleteSharingHistory(idshare));
    }

    // get history of sharing between patient and doctors.
    @GetMapping("/Medecins")
    @ResponseBody
    public ResponseEntity<?> MedecinSharingHistory(@RequestParam Long idpat,@RequestParam Long id_med) throws Exception{
        return ResponseEntity.ok(shareservices.findAllSharingHistoryByMedecinId(id_med,idpat));
    }

    // get history of sharing of a document.
    @GetMapping("/documents/{iddoc}")
    @ResponseBody
    public ResponseEntity<?> documentSharingHistory(@PathVariable("iddoc") Long iddoc) throws Exception{
        return ResponseEntity.ok(shareservices.findAllSharingHistoryByDocId(iddoc));
    }

    // get history of sharing of a document between current user and Medecin.
    @GetMapping("/users/{idmed}")
    @ResponseBody
    public ResponseEntity<?> UserandMedSharingHistory(@PathVariable("idmed") Long idmed) throws Exception{
        return ResponseEntity.ok(shareservices.findAllSharingHistoryByMedecinIdAndUserId(idmed));
    }

    // get history of sharing of a document between a patient and Medecin.
    @GetMapping("/Patients/{idpat}/{idmed}")
    @ResponseBody
    public ResponseEntity<?> PatientandMedSharingHistory(@PathVariable("idpat") Long idpat,@PathVariable("idmed") Long idmed) throws Exception{
        return ResponseEntity.ok(shareservices.findAllSharingHistoryByMedecinIdAndPatientId(idmed,idpat));
    }
    
}
