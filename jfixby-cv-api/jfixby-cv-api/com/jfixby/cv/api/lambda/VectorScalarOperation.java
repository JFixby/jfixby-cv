package com.jfixby.cv.api.lambda;

public interface VectorScalarOperation<T> {

	Vector<T> apply(Vector<T> vector, T scalar);

}
