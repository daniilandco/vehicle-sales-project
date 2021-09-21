package com.github.daniilandco.vehicle_sales_project.service.image;

import com.github.daniilandco.vehicle_sales_project.exception.InvalidImageSizeException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ImageService {
    byte[] scaleImage(byte[] bytes) throws IOException, InvalidImageSizeException;

    byte[] convertImage(byte[] bytes) throws IOException;

    byte[] cropImage(byte[] bytes) throws IOException, InvalidImageSizeException;
}
