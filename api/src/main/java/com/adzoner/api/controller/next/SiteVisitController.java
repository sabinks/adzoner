package com.adzoner.api.controller.next;

import com.adzoner.api.dto.monitor.SiteVisitDto;
import com.adzoner.api.service.monitor.SiteVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/api/next")
public class SiteVisitController {
    @Autowired
    private SiteVisitService siteVisitService;
    @PostMapping("site-visit-count")
    public ResponseEntity<String> siteVisit(@RequestBody SiteVisitDto siteVisitDto){
        siteVisitService.addCount(siteVisitDto);
        return new ResponseEntity<>("Ok", HttpStatus.NO_CONTENT);
    }

}
