package com.adzoner.api.service.monitor;

import com.adzoner.api.dto.monitor.SiteVisitDto;
import com.adzoner.api.entity.monitor.SiteVisit;
import com.adzoner.api.repository.monitor.SiteVisitRepository;
import com.adzoner.api.utility.MyUtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SiteVisitService {
    @Autowired
    private SiteVisitRepository siteVisitRepository;
    public void addCount(SiteVisitDto siteVisitDto) {
        Optional<SiteVisit> siteVisit = siteVisitRepository.findByIpAddress(siteVisitDto.getIpAddress());
        if(siteVisit.isPresent()){
            siteVisit.get().setCount(siteVisit.get().getCount() + 1);
            siteVisit.get().setUpdatedAt(MyUtilityClass.currentDateTime());
            siteVisitRepository.save(siteVisit.get());
        }else{
            SiteVisit siteVisit1 = new SiteVisit();
            siteVisit1.setCount(1L);
            siteVisit1.setIpAddress(siteVisitDto.getIpAddress());
            siteVisit1.setCreatedAt(MyUtilityClass.currentDateTime());
            siteVisit1.setUpdatedAt(MyUtilityClass.currentDateTime());
            siteVisitRepository.save(siteVisit1);
        }
    }
}
