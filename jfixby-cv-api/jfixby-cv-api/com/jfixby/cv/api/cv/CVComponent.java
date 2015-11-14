package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.math.FixedInt2;

public interface CVComponent {

	public λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> λimage);

	public λFunctionCache<FixedInt2, Color> newImageCache(int width, int height);

}
