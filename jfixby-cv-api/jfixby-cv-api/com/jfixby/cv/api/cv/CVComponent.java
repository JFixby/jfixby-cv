package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.math.FixedInt2;

public interface CVComponent {

	λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> input);

	λFunction<FixedInt2, Color> invert(λFunction<FixedInt2, Color> input);

	λFunction<FixedInt2, Color> blur(λFunction<FixedInt2, Color> input, float radius, float image_width, float image_height);

	λFunctionCache<FixedInt2, Color> newImageCache(int width, int height);

	void averageColor(Collection<Color> collectedColors, CustomColor average);

}
