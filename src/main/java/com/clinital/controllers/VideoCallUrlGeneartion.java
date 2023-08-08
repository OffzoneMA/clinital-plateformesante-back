package com.clinital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinital.security.config.VideoCall.UrlVideoCallGenerator;

import net.andreinc.mockneat.unit.text.Strings;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.types.enums.StringType.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/conference")
public class VideoCallUrlGeneartion {

    @Autowired
    private UrlVideoCallGenerator urlVideoCallGenerator;

    @GetMapping("/joincall")
    public String joinConference() {
        // Generate a unique room name
        return "redirect:" + urlVideoCallGenerator.joinConference();
    }

}
