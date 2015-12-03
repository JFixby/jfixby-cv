package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.components.ComponentInstaller;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.math.FixedInt2;

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

	public static λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> input) {
		return invoke().grayScale(input);
	}

	public static λFunction<FixedInt2, Color> invert(λFunction<FixedInt2, Color> input) {
		return invoke().invert(input);
	}

	public static λFunction<FixedInt2, Color> blur(λFunction<FixedInt2, Color> input, float radius, float image_width, float image_height) {
		return invoke().blur(input, radius, image_width, image_height);
	}

	public static λFunctionCache<FixedInt2, Color> newImageCache(int width, int height) {
		return invoke().newImageCache(width, height);
	}

	public static void averageColor(Collection<Color> collectedColors, CustomColor average) {
		invoke().averageColor(collectedColors, average);
	}

	public static λFunction<FixedInt2, Color> scale(λFunction<FixedInt2, Color> λimage, float scalefactor) {
		return invoke().scale(λimage, scalefactor);
	}

	public static λFunction<FixedInt2, Color> scale(λFunction<FixedInt2, Color> λimage, float scaleX, float scaleY) {
		return invoke().scale(λimage, scaleX, scaleY);
	}

}
