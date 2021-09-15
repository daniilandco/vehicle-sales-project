package com.github.daniilandco.vehicle_sales_project.service.image;

import lombok.Data;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@Service
public class ImageServiceImplementation implements ImageService {
    @Value("${cloud.default.photo.format}")
    private String defaultFormat;

    @Value("${cloud.default.photo.width}")
    private int defaultWidth;

    @Value("${cloud.default.photo.height}")
    private int defaultHeight;

    public byte[] scaleImage(byte[] bytes) throws IOException {
        bytes = cropImage(bytes);
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(is);
        if (image.getWidth() > defaultWidth) {
            BufferedImage scaledImage = Scalr.resize(image, defaultWidth, defaultHeight);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, defaultFormat, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return bytes;
        }
    }

    public byte[] cropImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(is);

        int width = image.getWidth();
        int height = image.getHeight();

        int sideSize = Math.min(width, height);

        int newWidth = sideSize;
        int newHeight = (int) Math.round(sideSize * 0.75); // make ratio of image 4 x 3

        int x0 = width / 2; // set x coordinate of image centre
        int y0 = height / 2; // set y coordinate of image centre

        BufferedImage croppedImage = image.getSubimage(x0 - newWidth / 2, y0 - newHeight / 2, newWidth, newHeight);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(croppedImage, defaultFormat, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] convertImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(is);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, defaultFormat, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
