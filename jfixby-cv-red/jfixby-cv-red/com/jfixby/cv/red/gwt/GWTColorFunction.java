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

	static public int toInt(double x) {
		return (int) FloatMath.round(x);
	}

	@Override
	public LambdaImage getRedChannel() {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).red();
	}

	@Override
	public LambdaImage getGreenChannel() {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).green();
	}

	@Override
	public LambdaImage getBlueChannel() {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).blue();
	}

	@Override
	public LambdaImage getAlphaChannel() {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).alpha();
	}

	@Override
	public LambdaImage getGrayscale(float grayscale_alpha, float grayscale_betta, float grayscale_gamma) {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).getGrayscaleValue(grayscale_alpha, grayscale_betta, grayscale_gamma);
	}

	@Override
	public LambdaImage getGrayscale() {
		return (xy) -> this.getValue(toInt(xy.getX()), toInt(xy.getY())).getGrayscaleValue();
	}

}
