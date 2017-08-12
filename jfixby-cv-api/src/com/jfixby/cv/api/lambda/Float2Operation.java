package com.jfixby.cv.api.lambda;

import com.jfixby.scarabei.api.floatn.Float2;
import com.jfixby.scarabei.api.floatn.ReadOnlyFloat2;

public interface Float2Operation {

	Float2 apply(ReadOnlyFloat2 xy, double delta_x, double delta_y);

}
