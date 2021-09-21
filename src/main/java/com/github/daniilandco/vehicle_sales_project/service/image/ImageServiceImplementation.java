package com.github.daniilandco.vehicle_sales_project.service.image;

import com.github.daniilandco.vehicle_sales_project.exception.InvalidImageSizeException;
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

    @Value("${cloud.photo.default-width}")
    private int defaultWidth;

    @Value("${cloud.photo.default-height}")
    private int defaultHeight;

    @Value("${cloud.photo.max-width}")
    private int maxWidth;

    @Value("${cloud.photo.max-height}")
    private int maxHeight;

    public byte[] scaleImage(byte[] bytes) throws IOException, InvalidImageSizeException {
        BufferedImage image = getBufferedImageFromBytes(cropImage(bytes));
        if (image.getWidth() > defaultWidth) {
            BufferedImage scaledImage = Scalr.resize(image, defaultWidth, defaultHeight);
            return convertImage(scaledImage);
        } else {
            return bytes;
        }
    }

    public byte[] cropImage(byte[] bytes) throws IOException, InvalidImageSizeException {
        BufferedImage image = getBufferedImageFromBytes(bytes);

        if (!isSizeValid(image)) {
            throw new InvalidImageSizeException();
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int sideSize = Math.min(width, height);

        int newWidth = sideSize;
        int newHeight = (int) Math.round(sideSize * 0.75); // make ratio of image 4 x 3

        int x0 = width / 2; // set x coordinate of image centre
        int y0 = height / 2; // set y coordinate of image centre

        BufferedImage croppedImage = image.getSubimage(x0 - newWidth / 2, y0 - newHeight / 2, newWidth, newHeight);

        return convertImage(croppedImage);
    }

    private boolean isSizeValid(BufferedImage image) {
        return image.getWidth() <= maxWidth && image.getHeight() <= maxHeight;
    }

    public byte[] convertImage(byte[] bytes) throws IOException {
        BufferedImage image = getBufferedImageFromBytes(bytes);
        return convertImage(image);
    }


    private BufferedImage getBufferedImageFromBytes(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(is);
        return bufferedImage;
    }

    private byte[] convertImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, defaultFormat, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
