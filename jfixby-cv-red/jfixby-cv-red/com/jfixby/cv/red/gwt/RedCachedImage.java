package com.jfixby.cv.red.gwt;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.floatn.FixedFloat2;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.lambda.Lambda;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λImage;
import com.jfixby.cmns.api.lambda.λImageCache;

public class RedCachedImage implements λImage {
	λFunction<FixedFloat2, Color> exe;

	public RedCachedImage(λImage image, λImageCache cache) {
		exe = (xy) -> image.val((float) xy.getX(), (float) xy.getY());
		exe = Lambda.cache(exe);
	}

	@Override
	public Color val(float x, float y) {
		FixedFloat2 input = Geometry.newFloat2(x, y);
		return exe.val(input);
	}

}
