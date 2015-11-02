package com.jfixby.cv.red.gwt;

import java.awt.image.BufferedImage;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.red.image.ImageSupply;

public class GWTImageSupply implements ImageSupply {

	private BufferedImage gwt_image;

	public GWTImageSupply(BufferedImage img) {
		gwt_image = img;
	}

	@Override
	public Color get(int x, int y) {
		Color color = color(gwt_image.getRGB(x, y));
		return color;
	}

	private Color color(int argb) {
		return Colors.newColor(argb);
	}

	@Override
	public void set(int x, int y, Color color_value) {
		int argb = color_value.toInteger();
		gwt_image.setRGB(x, y, argb);
	}

}
