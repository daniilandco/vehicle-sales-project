package com.github.daniilandco.vehicle_sales_project.service.image;

import java.io.IOException;

public interface ImageService {
    byte[] scaleImage(byte[] bytes) throws IOException;

    byte[] convertImage(byte[] bytes) throws IOException;

    byte[] cropImage(byte[] bytes) throws IOException;
}
