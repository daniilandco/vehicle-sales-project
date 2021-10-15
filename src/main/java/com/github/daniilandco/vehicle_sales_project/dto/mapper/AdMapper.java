package com.github.daniilandco.vehicle_sales_project.dto.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AdMapper {

    public Iterable<AdDto> toAdDtoSet(Iterable<Ad> adSet) {
        if (adSet != null) {
            Set<AdDto> adDtoSet = new HashSet<>();
            for (Ad ad : adSet) {
                adDtoSet.add(toAdDto(ad));
            }
            return adDtoSet;
        }
        return null;
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