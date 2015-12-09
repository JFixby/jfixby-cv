package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.lambda.λImage;
import com.jfixby.cmns.api.lambda.λImageCache;

public class CV {

	static private ComponentInstaller<CVComponent> componentInstaller = new ComponentInstaller<CVComponent>("CV");

	public static final void installComponent(CVComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final CVComponent invoke() {
		return componentInstaller.invokeComponent();
	}

	public static final CVComponent component() {
		return componentInstaller.getComponent();
	}

	public static λImage grayScale(λImage input) {
		return invoke().grayScale(input);
	}

	public static λImage invert(λImage input) {
		return invoke().invert(input);
	}

	public static λImage blur(λImage input, float radius, float image_width, float image_height) {
		return invoke().blur(input, radius, image_width, image_height);
	}

	public static λImageCache newImageCache(int width, int height) {
		return invoke().newImageCache(width, height);
	}

	public static void averageColor(Collection<Color> collectedColors, CustomColor average) {
		invoke().averageColor(collectedColors, average);
	}

	public static λImage scale(λImage λimage, float scalefactor) {
		return invoke().scale(λimage, scalefactor);
	}

	public static λImage scale(λImage λimage, float scaleX, float scaleY) {
		return invoke().scale(λimage, scaleX, scaleY);
	}

	public static λImage map(λImage λimage, Rectangle inputArea, Rectangle outputArea) {
		return invoke().map(λimage, inputArea, outputArea);
	}

	public static λImage cache(λImage image, λImageCache cache) {
		return invoke().cache(image, cache);
	}

}
