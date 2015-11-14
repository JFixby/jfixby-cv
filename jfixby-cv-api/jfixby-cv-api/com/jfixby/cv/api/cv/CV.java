package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.color.Color;
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

	public static λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> λimage) {
		return invoke().grayScale(λimage);
	}

	public static λFunctionCache<FixedInt2, Color> newImageCache(int width, int height) {
		return invoke().newImageCache(width, height);
	}

}
