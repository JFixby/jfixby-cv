
package com.jfixby.cv.api;

import com.jfixby.scarabei.api.ComponentInstaller;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.CustomColor;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ColoredλImage;

public class CV {

	static private ComponentInstaller<CVComponent> componentInstaller = new ComponentInstaller<CVComponent>("CV");

	public static final void installComponent (final CVComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final CVComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final CVComponent component () {
		return componentInstaller.getComponent();
	}

	public static ColoredλImage grayScale (final ColoredλImage input) {
		return invoke().grayScale(input);
	}

	public static ColoredλImage invert (final ColoredλImage input) {
		return invoke().invert(input);
	}

	public static ColoredλImage blur (final ColoredλImage input, final float radius, final float image_width,
		final float image_height) {
		return invoke().blur(input, radius, image_width, image_height);
	}

	public static void averageColor (final Collection<Color> collectedColors, final CustomColor average) {
		invoke().averageColor(collectedColors, average);
	}

	public static ColoredλImage scale (final ColoredλImage λimage, final float scalefactor) {
		return invoke().scale(λimage, scalefactor);
	}

	public static ColoredλImage scale (final ColoredλImage λimage, final float scaleX, final float scaleY) {
		return invoke().scale(λimage, scaleX, scaleY);
	}

	public static ColoredλImage map (final ColoredλImage λimage, final Rectangle inputArea, final Rectangle outputArea) {
		return invoke().map(λimage, inputArea, outputArea);
	}

}
