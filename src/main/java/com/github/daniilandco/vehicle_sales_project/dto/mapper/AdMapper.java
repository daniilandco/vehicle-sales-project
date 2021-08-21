package com.github.daniilandco.vehicle_sales_project.dto.mapper;

import com.github.daniilandco.vehicle_sales_project.dto.model.ad.AdDto;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AdMapper {

    public static Iterable<AdDto> toAdDtoSet(Iterable<Ad> adSet) {
        Set<AdDto> adDtoSet = new HashSet<>();
        for (Ad ad : adSet) {
            adDtoSet.add(AdMapper.toAdDto(ad));
        }
        return adDtoSet;
    }

    public static AdDto toAdDto(Ad ad) {
        return new AdDto()
                .setId(ad.getId())
                //.setOwner(UserMapper.toUserDto(ad.getAuthor()))
                .setTitle(ad.getTitle())
                .setDescription(ad.getDescription())
                .setPrice(ad.getPrice())
                .setMakeId(ad.getMakeId())
                .setCreatedAt(ad.getCreatedAt())
                .setReleaseYear(ad.getReleaseYear())
                .setStatus(ad.getStatus());
    }
}