
package com.jfixby.cv.argb.red;

import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cv.api.CVComponent;
import com.jfixby.cv.api.λOperator;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.color.CustomColor;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.floatn.Float2;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;
import com.jfixby.scarabei.api.geometry.Geometry;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.math.FloatMath;
import com.jfixby.scarabei.api.math.Int2;
import com.jfixby.scarabei.api.math.IntegerMath;

public class RedCV implements CVComponent {

	static final private λOperator grayscale = (λimage, params) -> ( (x, y) -> {
		final Color color = λimage.valueAt(x, y);
		final float gray_value = color.gray();
		final Color gray = Colors.newColor(gray_value, gray_value, gray_value);
		return gray;
	});
	private final static Int2 tmp = IntegerMath.newInt2();

	@Override
	public ColoredλImage grayScale (final ColoredλImage input) {
		return grayscale.apply(input);
	}

	static final private λOperator invert = (λimage, params) -> ( (x, y) -> {
		return λimage.valueAt(x, y).invert();
	});

	@Override
	public ColoredλImage invert (final ColoredλImage input) {
		return invert.apply(input);
	}

	static final private λOperator blur = (λimage, params) -> ( (X, Y) -> {
		final long radius = FloatMath.round(params[0]);
		final long W = FloatMath.round(params[1]);
		final long H = FloatMath.round(params[2]);
		float r = 0;
		float g = 0;
		float b = 0;

		final long x0 = (long)X;
		final long y0 = (long)Y;

		int points = 0;

		for (long x = x0 - radius; x <= x0 + radius; x = x + 1) {
			for (long y = y0 - radius; y <= y0 + radius; y = y + 1) {
				if (x >= 0 && x < W && y >= 0 && y < H) {
					final double distance = FloatMath.distance(x, y, x0, y0);
					if (distance <= radius) {
						final Color color = λimage.valueAt(x, y);
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
	public ColoredλImage blur (final ColoredλImage input, final float radius, final float image_width, final float image_height) {
		return blur.apply(input, radius, image_width, image_height);
	}

	public λFunction<ReadOnlyFloat2, Color> BLUR (final λFunction<ReadOnlyFloat2, Color> input, final int radius, final Rectangle area) {
		λFunction<ReadOnlyFloat2, Color> result = input;
		for (int i = 0; i < radius; i++) {
			result = this.blur(result, area);
		}
		return result;
	}

	private λFunction<ReadOnlyFloat2, Color> blur (final λFunction<ReadOnlyFloat2, Color> input, final Rectangle area) {
		return XY -> {
			final List<ReadOnlyFloat2> neighbours = collectPointsOfInterest(XY, 1, area);
			return averageColor(neighbours, input);
		};
	}

	static final public Color averageColor (final List<ReadOnlyFloat2> points, final λFunction<ReadOnlyFloat2, Color> input) {
		if (points.size() == 0) {
			Err.reportError("Empty input");
		}
		float r = 0;
		float g = 0;
		float b = 0;
		for (final ReadOnlyFloat2 neighbour : points) {
			final Color color = input.val(neighbour);
			r = r + color.red();
			g = g + color.green();
			b = b + color.blue();
		}
		r = r / points.size();
		g = g / points.size();
		b = b / points.size();
		return Colors.newColor(r, g, b);
	}

	static final private List<ReadOnlyFloat2> collectPointsOfInterest (final ReadOnlyFloat2 XY, final float radius,
		final Rectangle area) {
		final List<ReadOnlyFloat2> points = Collections.newList();
		final double x0 = XY.getX();
		final double y0 = XY.getY();
		for (double x = x0 - radius; x <= x0 + radius; x = x + 1f) {
			for (double y = y0 - radius; y <= y0 + radius; y = y + 1f) {
				final Float2 other = Geometry.newFloat2(x, y);
				final float distance = (float)XY.distanceTo(other);
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
	public void averageColor (final Collection<Color> collectedColors, final CustomColor average) {
		Debug.checkTrue("collectedColors.isEmpty()", collectedColors.size() != 0);
		float r = 0;
		float g = 0;
		float b = 0;
		for (final Color color : collectedColors) {
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
	public ColoredλImage scale (final ColoredλImage λimage, final float scalefactor) {

		return this.scale(λimage, scalefactor, scalefactor);
	}

	@Override
	public ColoredλImage scale (final ColoredλImage λimage, final float scaleX, final float scaleY) {
		final ColoredλImage scaled = (x, y) -> {
			// final FixedInt2 scaled_xy =
			// IntegerMath.newInt2(FloatMath.floorDown(xy.getX() / scaleX),
			// FloatMath.floorDown(xy.getY() / scaleY));
			return λimage.valueAt(x / scaleX, y / scaleY);
		};
		return scaled;
	}

	@Override
	public ColoredλImage map (final ColoredλImage λimage, final Rectangle inputArea, final Rectangle outputArea) {
		return (x, y) -> {
			final Float2 input = Geometry.newFloat2(x, y);
			outputArea.toRelative(input);
			inputArea.toAbsolute(input);
			return λimage.valueAt((float)input.getX(), (float)input.getY());
		};
	}

}
