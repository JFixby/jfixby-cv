package com.jfixby.cv.api.lambda;

import com.jfixby.cmns.api.image.LambdaImage;

public interface VectorNorm<T> {

	LambdaImage norm(Vector<T> vector);

}
