package com.jfixby.cv.api;

import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.CustomColor;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ColoredλImage;

public interface CVComponent {

    ColoredλImage grayScale(ColoredλImage input);

    ColoredλImage invert(ColoredλImage input);

    ColoredλImage blur(ColoredλImage input, float radius, float image_width, float image_height);

    void averageColor(Collection<Color> collectedColors, CustomColor average);

    ColoredλImage scale(ColoredλImage λimage, float scaleX, float scaleY);

    ColoredλImage scale(ColoredλImage λimage, float scalefactor);

    ColoredλImage map(ColoredλImage λimage, Rectangle inputArea, Rectangle outputArea);

}
