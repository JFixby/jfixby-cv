package com.jfixby.cv.api.lambda;

import com.jfixby.cmns.api.floatn.FixedFloat2;
import com.jfixby.cmns.api.floatn.Float2;

public interface Float2Operation {

	Float2 apply(FixedFloat2 xy, double delta_x, double delta_y);

}
