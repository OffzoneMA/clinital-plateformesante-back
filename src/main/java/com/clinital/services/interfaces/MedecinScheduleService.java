package com.clinital.services.interfaces;



import com.clinital.dto.MedecinScheduleDto;
import com.clinital.models.MedecinSchedule;
import com.clinital.payload.request.MedecinScheduleRequest;


public interface MedecinScheduleService {

    MedecinSchedule create(MedecinScheduleRequest medecinScheduledrequest,Long id) throws Exception;

    MedecinSchedule update(MedecinScheduleRequest medecinScheduledrequest, Long id) throws Exception;

    void deleteById(Long id) throws Exception;

}
