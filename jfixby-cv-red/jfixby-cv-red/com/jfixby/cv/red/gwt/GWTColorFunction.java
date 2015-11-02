package com.jfixby.cv.red.gwt;

import java.awt.image.BufferedImage;

import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.red.image.RedImage;

public class GWTColorFunction extends RedImage implements ColorMap {

	public GWTColorFunction(BufferedImage img) {
		super(img.getWidth(), img.getHeight(), Colors.BLACK(), new GWTImageSupply(img));
	}

}
