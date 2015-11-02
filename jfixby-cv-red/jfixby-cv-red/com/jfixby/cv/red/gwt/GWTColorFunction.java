package com.jfixby.cv.red.gwt;

import java.awt.image.BufferedImage;

import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.image.LambdaImage;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cv.api.gwt.GwtColorMap;
import com.jfixby.red.image.RedImage;

public class GWTColorFunction extends RedImage implements GwtColorMap {

	public GWTColorFunction(BufferedImage img) {
		super(img.getWidth(), img.getHeight(), Colors.BLACK(), new GWTImageSupply(img));
	}

	private int toInt(float x) {
		return (int) FloatMath.round(x);
	}

	@Override
	public LambdaImage getRedChannel() {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).red();
	}

	@Override
	public LambdaImage getGreenChannel() {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).green();
	}

	@Override
	public LambdaImage getBlueChannel() {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).blue();
	}

	@Override
	public LambdaImage getAlphaChannel() {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).alpha();
	}

	@Override
	public LambdaImage getGrayscale(float grayscale_alpha, float grayscale_betta, float grayscale_gamma) {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).getGrayscaleValue(grayscale_alpha, grayscale_betta, grayscale_gamma);
	}

	@Override
	public LambdaImage getGrayscale() {
		return (x, y) -> this.getValue(toInt(x), toInt(y)).getGrayscaleValue();
	}

}
