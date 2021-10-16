package com.github.daniilandco.vehicle_sales_project.dto.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdMapper {

    public List<AdDto> toAdDtoList(Iterable<Ad> adList) {
        List<AdDto> adDtoList = new ArrayList<>();
        adList.forEach(ad -> adDtoList.add(toAdDto(ad)));
        return adDtoList;
    }

    public AdDto toAdDto(Ad ad) {
        return new AdDto()
                .setId(ad.getId())
                .setTitle(ad.getTitle())
                .setCategory(ad.getCategory())
                .setDescription(ad.getDescription())
                .setPrice(ad.getPrice())
                .setCreatedAt(ad.getCreatedAt())
                .setReleaseYear(ad.getReleaseYear())
                .setStatus(ad.getStatus());
    }
}