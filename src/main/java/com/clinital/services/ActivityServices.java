package com.clinital.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.clinital.dao.IDao;
import com.clinital.dto.ActivityDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.models.LogActivityUser;
import com.clinital.models.User;
import com.clinital.repository.ActivityRespository;
import com.clinital.services.interfaces.historyservices;
import com.clinital.util.ClinitalModelMapper;
@Transactional
@Service
@Primary
public class ActivityServices implements historyservices {
    @Autowired
    private ActivityRespository activityRespository;

    @Autowired
	private ClinitalModelMapper clinitalModelMapper;

    

    public ActivityServices() {

    }
    // @Override
	// public List<LogActivityUser> findActivityByIdUsers(long id) {
	// 	return activityRespository.findActivityByIdUsers(id);
	// }
    // @Override
	// public Optional<LogActivityUser> findById(long id) {
	// 	return activityRespository.findById(id);
	// }
    // @Override
    // public void create(LogActivityUser o) {
    //     activityRespository.save(o);
    // }
    
    public <T> void createActivity(Date timeActivity, String typeActivity, String description, T user) {
        LogActivityUser activity = new LogActivityUser();
        activity.setTimeActivity(timeActivity);
        activity.setTypeActivity(typeActivity);
        activity.setDescription(description);
    
        // Set the user if it's not null
        if (user != null) {
            if (user instanceof User) {
                activity.setUser((User) user);
            }
        }
    
        activityRespository.save(activity);
    }
    


    // @Override
    // public void update(LogActivityUser o) {
    //     // TODO Auto-generated method stub
        
    // }


    // @Override
    // public void delete(LogActivityUser o) {
    //     // TODO Auto-generated method stub
        
    // }


    @Override
    public List<ActivityDTO> findAll()
    {
        return activityRespository
                .findAll()
                .stream()
                .map(activity->clinitalModelMapper.map(activity, ActivityDTO.class))
                .collect(Collectors.toList());
    }

    @Override
	public ActivityDTO getactivityByIdUser(Long id) throws Exception {
		 List<LogActivityUser> activity= activityRespository.findActivityByIdUsers(id);
		 return clinitalModelMapper.map(activity, ActivityDTO.class);
	}
    @Override
    public ActivityDTO create(ActivityDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ActivityDTO update(ActivityDTO dto, Long id) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void deleteById(Long id) throws Exception {
        // TODO Auto-generated method stub
        
    }
}
