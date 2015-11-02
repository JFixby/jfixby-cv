package com.jfixby.cv.api.lambda;

import com.jfixby.cmns.api.floatn.FixedFloat2;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.image.LambdaImage;
import com.jfixby.cmns.api.math.FloatMath;

public class IMAGE_OPERATIONS {

	public static final BinaryOperation<LambdaImage> ADD = (a, b) -> (xy -> (a.value(xy) + b.value(xy)));
	public static final BinaryOperation<LambdaImage> SUBTRACT = (a, b) -> (xy -> (a.value(xy) - b.value(xy)));
	public static final BinaryOperation<LambdaImage> MULTIPLY = (a, b) -> (xy -> (a.value(xy) * b.value(xy)));
	public static final UnaryOperation<LambdaImage> INVERT = a -> (xy -> (1f - a.value(xy)));
	public static final UnaryOperation<LambdaImage> LIMIT = a -> (xy -> ((float) (FloatMath.limit(0, a.value(xy), 1))));
	public static final UnaryOperation<LambdaImage> SQUARE = a -> MULTIPLY.apply(a, a);
	public static final Norm<LambdaImage> NORM = f -> 0;

	public static final Float2Operation shiftX = (xy, dx, dy) -> Geometry.newFloat2(xy.getX() + dx, xy.getY() + dy);;

	/*
	 * Partial derivative d/dx
	 */
	public static final UnaryOperation<LambdaImage> ddx = f -> (xy -> (f.value(shiftX.apply(xy, +1f, +0f)) - f.value(shiftX.apply(xy, -1f, +0f))) / 2f);

	/*
	 * Partial derivative d/dy
	 */
	public static final UnaryOperation<LambdaImage> ddy = f -> (xy -> (f.value(shiftX.apply(xy, +0f, +1f)) - f.value(shiftX.apply(xy, 0f, -1f))) / 2f);

	public static final LambdaImage ZERO = xy -> 0f;
	public static final LambdaImage ONE = xy -> 1f;

	public static final VectorBasis<LambdaImage> LambdaImageVectorBasis = k -> (i -> ((i == k) ? ONE : ZERO));

	public static final Vector<LambdaImage> E0 = LambdaImageVectorBasis.vector(0);
	public static final Vector<LambdaImage> E1 = LambdaImageVectorBasis.vector(1);

	public static final BinaryOperation<Vector<LambdaImage>> VECTOR_ADD = (a, b) -> (i -> ADD.apply(a.at(i), b.at(i)));;
	public static final VectorScalarOperation<LambdaImage> VECTOR_MULTIPLY = (vector, scalar) -> (i -> MULTIPLY.apply(vector.at(i), scalar));;

	public static final GOperator<LambdaImage> GRADIENT = f -> VECTOR_ADD.apply(VECTOR_MULTIPLY.apply(E0, ddx.apply(f)), VECTOR_MULTIPLY.apply(E1, ddy.apply(f)));

	public static final VectorNorm<LambdaImage> VECTOR_NORM = v -> NORM.norm(v.at(0));
}
