package com.jfixby.cv.api;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.image.ColoredλImage;
import com.jfixby.cmns.api.image.ColoredλImageCache;

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

    public static ColoredλImage grayScale(ColoredλImage input) {
	return invoke().grayScale(input);
    }

    public static ColoredλImage invert(ColoredλImage input) {
	return invoke().invert(input);
    }

    public static ColoredλImage blur(ColoredλImage input, float radius, float image_width, float image_height) {
	return invoke().blur(input, radius, image_width, image_height);
    }

    public static void averageColor(Collection<Color> collectedColors, CustomColor average) {
	invoke().averageColor(collectedColors, average);
    }

    public static ColoredλImage scale(ColoredλImage λimage, float scalefactor) {
	return invoke().scale(λimage, scalefactor);
    }

    public static ColoredλImage scale(ColoredλImage λimage, float scaleX, float scaleY) {
	return invoke().scale(λimage, scaleX, scaleY);
    }

    public static ColoredλImage map(ColoredλImage λimage, Rectangle inputArea, Rectangle outputArea) {
	return invoke().map(λimage, inputArea, outputArea);
    }

    

}
