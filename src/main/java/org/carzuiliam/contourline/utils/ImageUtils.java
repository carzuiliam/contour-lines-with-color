package org.carzuiliam.contourline.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static byte[] readImageToByteArray(BufferedImage _image) {
        int width = _image.getWidth();
        int height = _image.getHeight();

        byte[] data = new byte[width * height * 3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = _image.getRGB(x, y);
                int idx = (y * width + x) * 3;

                data[idx] = (byte) ((rgb >> 16) & 0xff);
                data[idx + 1] = (byte) ((rgb >> 8) & 0xff);
                data[idx + 2] = (byte) (rgb & 0xff);
            }
        }

        return data;
    }

    public static void writeByteArrayToJPG(int _width, int _height, byte[] _image, String _filename) throws IOException {
        String outputDir = "target/output";
        new File(outputDir).mkdirs();

        File outputFile = new File(outputDir, _filename);
        BufferedImage img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < _height; y++) {
            for (int x = 0; x < _width; x++) {
                int idx = (y * _width + x) * 3;
                int r = Byte.toUnsignedInt(_image[idx]);
                int g = Byte.toUnsignedInt(_image[idx + 1]);
                int b = Byte.toUnsignedInt(_image[idx + 2]);
                int rgb = (r << 16) | (g << 8) | b;
                img.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(img, "jpg", outputFile);
    }
}