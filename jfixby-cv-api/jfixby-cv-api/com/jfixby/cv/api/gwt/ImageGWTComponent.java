package com.jfixby.cv.api.gwt;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;

public interface ImageGWTComponent {

	BufferedImage readFromFile(File image_file) throws IOException;

	void writeToFile(java.awt.Image java_img_icon, File file, String file_type) throws IOException;

	ArrayColorMap newGWTColorMap(BufferedImage image);

	BufferedImage toGWTImage(ColorMap image_function);

	ArrayColorMap newGWTColorMap(java.io.InputStream java_is) throws IOException;

	ArrayColorMap readGWTColorMap(File image_file) throws IOException;

	

}
