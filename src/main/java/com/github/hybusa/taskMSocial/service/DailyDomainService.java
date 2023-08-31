package com.github.hybusa.taskMSocial.service;

import com.github.hybusa.taskMSocial.dto.DailyDomainDto;
import com.github.hybusa.taskMSocial.entity.DailyDomain;
import com.github.hybusa.taskMSocial.mapper.DailyDomainMapper;
import com.github.hybusa.taskMSocial.repository.DailyDomainRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class DailyDomainService {
    private final DailyDomainRepository dailyDomainRepository;
    private final DailyDomainMapper dailyDomainMapper;

    public int updateAndCount(List<DailyDomainDto> dailyDomainDtoList){
        dailyDomainRepository.deleteAll();
        for (DailyDomainDto dailyDomainDto : dailyDomainDtoList) {
            dailyDomainRepository.save(dailyDomainMapper.entityFromDto(dailyDomainDto));
        }
        return dailyDomainRepository.countAll();
    }

}
