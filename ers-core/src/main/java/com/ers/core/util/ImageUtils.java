package com.ers.core.util;

import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author avillalobos
 */
public class ImageUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
    private static final int MAX_DIMENTION_VALUE = 1024;
    private static final String PNG_FORMAT_NAME = "png";

    /**
     * Private constructor to prevent instantiations.
     */
    private ImageUtils() {
    }

    /**
     * Verifies if the file represent an image.
     * Image I/O has built-in support for GIF, PNG, JPEG, BMP, and WBMP.
     * 
     * @param file picture file
     * @return
     */
    public static boolean isImage(File file) {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return isImage(inputStream);
        } catch (IOException e) {
            // It's not an image.
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifies if the bytes represent an image.
     * Image I/O has built-in support for GIF, PNG, JPEG, BMP, and WBMP.
     * 
     * @param picture
     * @return
     */
    public static boolean isImage(byte[] picture) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(picture)) {
            return isImage(bais);
        } catch (IOException ignore) {
            // Ignore it
        }

        return false;
    }

    /**
     * Verifies if the stream represent an image.
     * Image I/O has built-in support for GIF, PNG, JPEG, BMP, and WBMP.
     * 
     * @param inputStream
     * @return
     */
    private static boolean isImage(InputStream inputStream) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                return false;
            }

            // Read successfully: it's an image.
            return true;
        } catch (Exception e) {
            // It's not an image.
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Validates the value of the parameters
     * 
     * @param width
     * @param height
     * 
     * @throws ErsException if invalid
     */
    public static void validateDimentions(int width, int height) throws ErsException {
        if (width > MAX_DIMENTION_VALUE || height > MAX_DIMENTION_VALUE) {
            throw new ErsException("Unsupported width or height value. Maximum is " + MAX_DIMENTION_VALUE, ErsErrorCode.INVALID_PARAMETER);
        }
    }

    /**
     * Resizes an image.
     * Note the Image I/O has built-in support for GIF, PNG, JPEG, BMP, and WBMP.
     * 
     * If either width or height is a negative number then a value is substituted to maintain the aspect ratio of the original image dimensions.
     * If both width and height are negative, then the original image dimensions are used.
     * Neither of width or height can be 0.
     *
     * @param image The image bytes.
     * @param width
     * @param height
     * 
     * @return PNG image result
     */
    @SuppressWarnings("boxing")
    public static byte[] resizeImageToPng(byte[] image, int width, int height) {

        try {
            // The Image I/O has built-in support for GIF, PNG, JPEG, BMP, and WBMP
            // Read here:
            // http://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));

            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_FAST);

            int newWidth = resizedImage.getWidth(null);
            int newHeight = resizedImage.getHeight(null);
            int type;
            if (originalImage.getTransparency() == Transparency.OPAQUE) {
                type = BufferedImage.TYPE_INT_RGB;
            } else {
                type = BufferedImage.TYPE_INT_ARGB;
            }

            // Creating the new image
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, type);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizedImage, 0, 0, null);
            graphics.dispose();

            // Based on documentation,the following standard image format plugins are always be present:
            // JPEG, PNG, GIF, BMP and WBMP
            // See http://docs.oracle.com/javase/tutorial/2d/images/saveimage.html
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImage, PNG_FORMAT_NAME, baos);
            return baos.toByteArray();

        } catch (Exception e) {
            LOGGER.error("Unable to resize the image to {} width and {} height", width, height, e);
        }

        return image;
    }

}
