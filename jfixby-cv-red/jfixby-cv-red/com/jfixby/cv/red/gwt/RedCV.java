package com.jfixby.cv.red.gwt;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.floatn.FixedFloat2;
import com.jfixby.cmns.api.floatn.Float2;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.math.FixedInt2;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cmns.api.math.Int2;
import com.jfixby.cmns.api.math.IntegerMath;
import com.jfixby.cmns.api.util.JUtils;
import com.jfixby.cv.api.cv.CVComponent;
import com.jfixby.cv.api.cv.λOperator;

public class RedCV implements CVComponent {

	static final private λOperator grayscale = (λimage, params) -> (xy -> {
		Color color = λimage.val(xy);
		float gray_value = color.getGrayscaleValue();
		Color gray = Colors.newColor(gray_value, gray_value, gray_value);
		return gray;
	});
	private final static Int2 tmp = IntegerMath.newInt2();

	@Override
	public λFunction<FixedInt2, Color> grayScale(λFunction<FixedInt2, Color> input) {
		return grayscale.apply(input);
	}

	static final private λOperator invert = (λimage, params) -> (xy -> {
		return λimage.val(xy).invert();
	});

	@Override
	public λFunction<FixedInt2, Color> invert(λFunction<FixedInt2, Color> input) {
		return invert.apply(input);
	}

	static final private λOperator blur = (λimage, params) -> (xy -> {
		long radius = FloatMath.round(params[0]);
		long W = FloatMath.round(params[1]);
		long H = FloatMath.round(params[2]);
		float r = 0;
		float g = 0;
		float b = 0;

		final long x0 = xy.getX();
		final long y0 = xy.getY();

		int points = 0;

		for (long x = x0 - radius; x <= x0 + radius; x = x + 1) {
			for (long y = y0 - radius; y <= y0 + radius; y = y + 1) {
				if (x >= 0 && x < W && y >= 0 && y < H) {
					double distance = FloatMath.distance(x, y, x0, y0);
					if (distance <= radius) {
						tmp.setXY(x, y);
						final Color color = λimage.val(tmp);
						points++;
						r = r + color.red();
						g = g + color.green();
						b = b + color.blue();
					}
				}
			}
		}
		r = r / points;
		g = g / points;
		b = b / points;
		final Color color_value = Colors.newColor(r, g, b);
		return color_value;
		// return Colors.BLACK();

	});

	@Override
	public λFunction<FixedInt2, Color> blur(λFunction<FixedInt2, Color> input, float radius, float image_width, float image_height) {
		return blur.apply(input, radius, image_width, image_height);
	}

	public λFunctionCache<FixedInt2, Color> newImageCache(int width, int height) {
		return new RedImageCache(width, height);
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

	@Override
	public void averageColor(Collection<Color> collectedColors, CustomColor average) {
		JUtils.checkTrue("collectedColors.isEmpty()", collectedColors.size() != 0);
		float r = 0;
		float g = 0;
		float b = 0;
		for (Color color : collectedColors) {
			r = r + color.red();
			g = g + color.green();
			b = b + color.blue();
		}
		r = r / collectedColors.size();
		g = g / collectedColors.size();
		b = b / collectedColors.size();
		average.setRed(r).setBlue(b).setGreen(g).setAlpha(1);
	}

	@Override
	public λFunction<FixedInt2, Color> scale(λFunction<FixedInt2, Color> λimage, float scalefactor) {
		λFunction<FixedInt2, Color> scaled = xy -> {
			final FixedInt2 scaled_xy = IntegerMath.newInt2(FloatMath.round(xy.getX() / scalefactor), FloatMath.round(xy.getY() / scalefactor));
			return λimage.val(scaled_xy);
		};
		return scaled;
	}

	@Override
	public λFunction<FixedInt2, Color> scale(λFunction<FixedInt2, Color> λimage, float scaleX, float scaleY) {
		λFunction<FixedInt2, Color> scaled = xy -> {
			final FixedInt2 scaled_xy = IntegerMath.newInt2(FloatMath.round(xy.getX() / scaleX), FloatMath.round(xy.getY() / scaleY));
			return λimage.val(scaled_xy);
		};
		return scaled;
	}
}
