//package com.jfixby.cv.red.test;
//
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//import com.jfixby.cmns.api.filesystem.File;
//import com.jfixby.cmns.api.filesystem.LocalFileSystem;
//import com.jfixby.cmns.api.geometry.Geometry;
//import com.jfixby.cmns.api.geometry.Rectangle;
//import com.jfixby.cmns.api.image.ArrayColorMap;
//import com.jfixby.cmns.api.image.ColorMap;
//import com.jfixby.cmns.api.image.ImageProcessing;
//import com.jfixby.cmns.api.image.LambdaColorMap;
//import com.jfixby.cmns.api.image.LambdaColorMapSpecs;
//import com.jfixby.cmns.api.image.LambdaImage;
//import com.jfixby.cmns.api.log.L;
//import com.jfixby.cmns.api.math.FloatMath;
//import com.jfixby.cmns.desktop.DesktopAssembler;
//import com.jfixby.cv.api.gwt.ImageGWT;
//import com.jfixby.cv.api.lambda.IMAGE_OPERATIONS;
//import com.jfixby.cv.api.lambda.Vector;
//import com.jfixby.cv.red.gwt.RedImageGWT;
//
//public class ImageTest {
//
//	public static void main(String[] args) throws IOException {
//		DesktopAssembler.setup();
//		ImageGWT.installComponent(new RedImageGWT());
//
//		ColorMap color_map = readImage("input.png");
//		int w = color_map.getWidth();
//		int h = color_map.getHeight();
//		LambdaImage grayscale = color_map.getGrayscale();
//
//		saveResult(grayscale, w, h, "bw.png");
//		// saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.ddx.apply(grayscale)),
//		// w, h, "ddx.png");
//		// saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.ddy.apply(grayscale)),
//		// w, h, "ddy.png");
//		saveResult(IMAGE_OPERATIONS.INVERT.apply(IMAGE_OPERATIONS.MULTIPLY.apply(sketchy(grayscale), xy -> 2f)), w, h, "sketchy.png");
//
//	}
//
//	private static LambdaImage sketchy(LambdaImage grayscale) {
//		Vector<LambdaImage> gradF = IMAGE_OPERATIONS.GRADIENT.apply(grayscale);
//		LambdaImage normGrad = IMAGE_OPERATIONS.VECTOR_NORM_2.norm(gradF);
//		return xy -> (float) FloatMath.pow(normGrad.value(xy), 0.5f);
//
//	}
//
//	private static void saveResult(LambdaImage image, int w, int h, String filename) throws IOException {
//
//		LambdaColorMapSpecs lambda_specs = ImageProcessing.newLambdaColorMapSpecs();
//		lambda_specs.setAlphaChannel(null);
//		lambda_specs.setBlueChannel(IMAGE_OPERATIONS.LIMIT.apply(image));
//		lambda_specs.setRedChannel(IMAGE_OPERATIONS.LIMIT.apply(image));
//		lambda_specs.setGreenChannel(IMAGE_OPERATIONS.LIMIT.apply(image));
//		Rectangle rectangle = Geometry.newRectangle(w, h);
//		lambda_specs.setLambdaArea(rectangle);
//		lambda_specs.setColorMapWidth(w);
//		lambda_specs.setColorMapHeight(h);
//
//		LambdaColorMap bw = ImageProcessing.newLambdaColorMap(lambda_specs);
//
//		writeImage(bw, filename);
//	}
//
//	private static void writeImage(ColorMap image, String file_name) throws IOException {
//		BufferedImage gwt_bw = ImageGWT.toGWTImage(image);
//		File output_image_file = LocalFileSystem.ApplicationHome().child(file_name);
//		L.d("writing", output_image_file);
//		ImageGWT.writeToFile(gwt_bw, output_image_file, "png");
//	}
//
//	private static ColorMap readImage(String file_name) throws IOException {
//		File image_file = LocalFileSystem.ApplicationHome().child(file_name);
//		L.d("reading", image_file);
//		ArrayColorMap color_map = ImageGWT.readGWTColorMap(image_file);
//		return color_map;
//	}
// }
