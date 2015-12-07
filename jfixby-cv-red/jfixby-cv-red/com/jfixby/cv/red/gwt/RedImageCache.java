package com.jfixby.cv.red.gwt;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.lambda.λImageCache;
import com.jfixby.cmns.api.log.L;

public class RedImageCache implements λImageCache {

	final private int width;
	final private int height;
	final private Color[][] array;

	public RedImageCache(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.array = new Color[width][height];

	}

	@Override
	public Color get(float x, float y) {
		return this.array[(int) x][(int) y];
	}

	@Override
	public void put(float x, float y, Color value) {
		this.array[(int) x][(int) y] = value;
	}

	@Override
	public void print(String tag) {
		L.d("RedImageCache");
	}

}
