package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.lambda.λImage;
import com.jfixby.cmns.api.lambda.λImageCache;

public interface CVComponent {

	λImage grayScale(λImage input);

	λImage invert(λImage input);

	λImage blur(λImage input, float radius, float image_width, float image_height);

	λImageCache newImageCache(int width, int height);

	void averageColor(Collection<Color> collectedColors, CustomColor average);

	λImage scale(λImage λimage, float scaleX, float scaleY);

	λImage scale(λImage λimage, float scalefactor);

	λImage map(λImage λimage, Rectangle inputArea, Rectangle outputArea);

	λImage cache(λImage image, λImageCache cache);

}
