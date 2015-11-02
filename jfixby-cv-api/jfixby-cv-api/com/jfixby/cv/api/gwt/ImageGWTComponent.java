package com.jfixby.cv.api.gwt;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.image.ColorFunction;
import com.jfixby.cmns.api.io.Buffer;

public interface ImageGWTComponent {

	BufferedImage readJavaImage(File image_file) throws IOException;

	void writeJavaFile(java.awt.Image java_img_icon, File file, String file_type) throws IOException;

	ColorFunction newColorFunction(BufferedImage image);

	ColorFunction newColorFunction(Buffer buffer) throws IOException;

}
