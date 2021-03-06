
package com.jfixby.cv.red.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ArrayColorMap;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;

public class ImageTest {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		ImageAWT.installComponent(new RedImageAWT());
		CV.installComponent(new RedCV());

		final ColorMap color_map = readImage("input.png");
		final int w = color_map.getWidth();
		final int h = color_map.getHeight();
		ColoredλImage λimage = color_map.getLambdaImage();
		// λImage grayscale = CV.grayScale().apply(λimage);
		// grayscale = Lambda.cache(grayscale, CV.newImageCache(w, h));
		// grayscale = CV.invert().apply(grayscale);
		λimage = CV.blur(λimage, 3f, w, h);
		// λimage = CV.cache(λimage, ImageProcessing.newImageCache(w, h));

		// grayscale = CV.cache(grayscale,).apply(λimage);

		// grayscale = Lambda.newFunction(xy -> grayscale.val(xy),
		// CV.newImageCache(w, h));

		saveResult(λimage, w, h, "bw.png");
		saveResult(λimage, w, h, "bw2.png");
		// saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.ddx.apply(grayscale)),
		// w, h, "ddx.png");
		// saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.ddy.apply(grayscale)),
		// w, h, "ddy.png");
		// saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.MULTIPLY.apply(sketchy(grayscale),
		// xy -> 2f)), w, h, "sketchy.png");

	}

	//
	// private static LambdaImage sketchy(LambdaImage grayscale) {
	// Vector<LambdaImage> gradF = IMAGE_OPERATIONS.GRADIENT.apply(grayscale);
	// LambdaImage normGrad = IMAGE_OPERATIONS.VECTOR_NORM_2.norm(gradF);
	// return xy -> (float) FloatMath.pow(normGrad.value(xy), 0.5f);
	//
	// }

	private static void saveResult (final ColoredλImage image, final int w, final int h, final String filename)
		throws IOException {

		final ColorMapSpecs lambda_specs = ImageProcessing.newColorMapSpecs();

		lambda_specs.setColorMapWidth(w);
		lambda_specs.setColorMapHeight(h);
		lambda_specs.setLambdaColoredImage(image);
		final ColorMap bw = ImageProcessing.newColorMap(lambda_specs);

		writeImage(bw, filename);
	}

	private static void writeImage (final ColorMap image, final String file_name) throws IOException {
		final BufferedImage gwt_bw = ImageAWT.toAWTImage(image);
		final File output_image_file = LocalFileSystem.ApplicationHome().child(file_name);
		L.d("writing", output_image_file);
		ImageAWT.writeToFile(gwt_bw, output_image_file, "png");
	}

	private static ColorMap readImage (final String file_name) throws IOException {
		final File image_file = LocalFileSystem.ApplicationHome().child(file_name);
		L.d("reading", image_file);
		final ArrayColorMap color_map = ImageAWT.readAWTColorMap(image_file);
		return color_map;
	}
}
