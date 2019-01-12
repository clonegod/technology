package clonegod.spider.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class ImageUtil {
	
	public static String toBase64(byte[] bytes) {
		String base64Str = Base64.encodeBase64String(bytes);
		return base64Str;
	}
	
	public static String toBase64(InputStream in) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			String base64Str = Base64.encodeBase64String(baos.toByteArray());
			return base64Str;
		} catch (IOException e) {
			throw new RuntimeException("转化图片base64出错",e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static byte[] fromBase64(String base64Str) {
		byte[] data = Base64.decodeBase64(base64Str);
		return data;
	}
	
	public static byte[] bufferedImageToBytes(BufferedImage bufferedImage) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return output.toByteArray();
	}
	
	public static String bufferedImageToBase64(BufferedImage bufferedImage) {
		return toBase64(bufferedImageToBytes(bufferedImage));
	}
	
	
	public static void bufferedImageToFile(BufferedImage bufferedImage, File target, String imageType) {
		if(StringUtils.isEmpty(imageType)) {
			imageType = "png";
		}
		byte[] bytes = bufferedImageToBytes(bufferedImage);
		try {
			FileUtils.writeByteArrayToFile(target, bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
