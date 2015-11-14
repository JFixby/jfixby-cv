package com.jfixby.cv.api.cv;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.lambda.λFunction;
import com.jfixby.cmns.api.math.FixedInt2;

public interface λOperator {
	public λFunction<FixedInt2, Color> apply(λFunction<FixedInt2, Color> input, float... params);
}
