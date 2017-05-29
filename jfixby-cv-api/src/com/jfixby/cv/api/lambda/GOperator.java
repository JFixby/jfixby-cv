package com.jfixby.cv.api.lambda;

public interface GOperator<T> {
	Vector<T> apply(T element);
}
