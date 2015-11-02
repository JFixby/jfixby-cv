package com.jfixby.cv.api.gwt;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.components.ComponentInstaller;
import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.image.ColorFunction;
import com.jfixby.cmns.api.io.Buffer;

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

	public static BufferedImage readJavaImage(File image_file) throws IOException {
		return invoke().readJavaImage(image_file);
	}

	public static void writeJavaFile(java.awt.Image javaImage, File image_file, String file_type) throws IOException {
		invoke().writeJavaFile(javaImage, image_file, file_type);
	}

	public static ColorFunction newColorFunction(BufferedImage image) {
		return invoke().newColorFunction(image);
	}

	public static ColorFunction newColorFunction(Buffer buffer) throws IOException {
		return invoke().newColorFunction(buffer);
	}

}
