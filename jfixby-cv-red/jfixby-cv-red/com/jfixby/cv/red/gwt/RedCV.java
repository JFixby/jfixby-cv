package com.jfixby.cv.red.gwt;

import com.jfixby.cmns.api.collections.JUtils;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.floatn.FixedFloat2;
import com.jfixby.cmns.api.floatn.Float2;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.math.FixedInt2;
import com.jfixby.cv.api.cv.CVComponent;

public class RedCV implements CVComponent {

	public λFunctionCache<FixedInt2, Color> newImageCache(int width, int height) {
		return new RedImageCache(width, height);
	}

	public λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> λimage) {
		return xy -> {
			Color color = λimage.val(xy);
			float gray_value = color.getGrayscaleValue();
			Color gray = Colors.newColor(gray_value, gray_value, gray_value);
			return gray;
		};
	}

	public λFunction<FixedFloat2, Color> BLUR(final λFunction<FixedFloat2, Color> input, final int radius, final Rectangle area) {
		λFunction<FixedFloat2, Color> result = input;
		for (int i = 0; i < radius; i++) {
			result = blur(result, area);
		}
		return result;
	}

	private λFunction<FixedFloat2, Color> blur(final λFunction<FixedFloat2, Color> input, final Rectangle area) {
		return XY -> {
			final List<FixedFloat2> neighbours = collectPointsOfInterest(XY, 1, area);
			return averageColor(neighbours, input);
		};
	}

	static final public Color averageColor(List<FixedFloat2> points, λFunction<FixedFloat2, Color> input) {
		if (points.size() == 0) {
			throw new Error("Empty input");
		}
		float r = 0;
		float g = 0;
		float b = 0;
		for (FixedFloat2 neighbour : points) {
			Color color = input.val(neighbour);
			r = r + color.red();
			g = g + color.green();
			b = b + color.blue();
		}
		r = r / points.size();
		g = g / points.size();
		b = b / points.size();
		return Colors.newColor(r, g, b);
	}

	static final private List<FixedFloat2> collectPointsOfInterest(FixedFloat2 XY, float radius, Rectangle area) {
		List<FixedFloat2> points = JUtils.newList();
		double x0 = XY.getX();
		double y0 = XY.getY();
		for (double x = x0 - radius; x <= x0 + radius; x = x + 1f) {
			for (double y = y0 - radius; y <= y0 + radius; y = y + 1f) {
				Float2 other = Geometry.newFloat2(x, y);
				float distance = (float) XY.distanceTo(other);
				if (distance <= radius) {
					if (area.containsPoint(other)) {
						points.add(other);
					}
				}
			}
		}
		return points;
	}

}
