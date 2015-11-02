package com.jfixby.cv.api.gwt;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.components.ComponentInstaller;
import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.image.ColorMap;

public class ImageGWT {

	static private ComponentInstaller<ImageGWTComponent> componentInstaller = new ComponentInstaller<ImageGWTComponent>("ImageGWT");

	public static final void installComponent(ImageGWTComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final ImageGWTComponent invoke() {
		return componentInstaller.invokeComponent();
	}

	public static final ImageGWTComponent component() {
		return componentInstaller.getComponent();
	}

	public static BufferedImage readFromFile(File image_file) throws IOException {
		return invoke().readFromFile(image_file);
	}

	public static void writeToFile(java.awt.Image javaImage, File image_file, String file_type) throws IOException {
		invoke().writeToFile(javaImage, image_file, file_type);
	}

	public static ColorMap newGWTColorMap(BufferedImage image) {
		return invoke().newGWTColorMap(image);
	}

	public static ColorMap newGWTColorMap(java.io.InputStream java_input_stream) throws IOException {
		return invoke().newGWTColorMap(java_input_stream);
	}

	public static BufferedImage toGWTImage(ColorMap image_function) {
		return invoke().toGWTImage(image_function);

	}

	public static ColorMap readGWTColorMap(File image_file) throws IOException {
		return invoke().readGWTColorMap(image_file);
	}

}
