package com.jfixby.cv.red.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.scarabei.api.desktop.DesktopSetup;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ArrayColorMap;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;

public class ImageTest {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	ImageAWT.installComponent(new RedImageAWT());
	CV.installComponent(new RedCV());

	ColorMap color_map = readImage("input.png");
	int w = color_map.getWidth();
	int h = color_map.getHeight();
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

    private static void saveResult(ColoredλImage image, int w, int h, String filename) throws IOException {

	ColorMapSpecs lambda_specs = ImageProcessing.newColorMapSpecs();

	lambda_specs.setColorMapWidth(w);
	lambda_specs.setColorMapHeight(h);
	lambda_specs.setLambdaColoredImage(image);
	ColorMap bw = ImageProcessing.newColorMap(lambda_specs);

	writeImage(bw, filename);
    }

    private static void writeImage(ColorMap image, String file_name) throws IOException {
	BufferedImage gwt_bw = ImageAWT.toAWTImage(image);
	File output_image_file = LocalFileSystem.ApplicationHome().child(file_name);
	L.d("writing", output_image_file);
	ImageAWT.writeToFile(gwt_bw, output_image_file, "png");
    }

    private static ColorMap readImage(String file_name) throws IOException {
	File image_file = LocalFileSystem.ApplicationHome().child(file_name);
	L.d("reading", image_file);
	ArrayColorMap color_map = ImageAWT.readAWTColorMap(image_file);
	return color_map;
    }
}
