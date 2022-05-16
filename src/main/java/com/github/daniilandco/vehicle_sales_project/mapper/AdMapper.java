package com.github.daniilandco.vehicle_sales_project.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.AdDTO;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface which generates methods for mapping person DTO to person model.
 *
 * @author com.github.daniilandco
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface AdMapper {
    List<AdDTO> toAdDTOList(List<Ad> adList);

    AdDTO toAdDto(Ad ad);
}