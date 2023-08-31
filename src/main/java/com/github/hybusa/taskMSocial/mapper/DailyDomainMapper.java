package com.github.hybusa.taskMSocial.mapper;

import com.github.hybusa.taskMSocial.dto.DailyDomainDto;
import com.github.hybusa.taskMSocial.entity.DailyDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailyDomainMapper {
    DailyDomain entityFromDto(DailyDomainDto dailyDomainDto);
}
