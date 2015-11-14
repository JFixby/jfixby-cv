package com.jfixby.cv.red.gwt;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.lambda.λFunctionCache;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FixedInt2;

public class RedImageCache implements λFunctionCache<FixedInt2, Color> {

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
	public Color get(FixedInt2 key) {
		return this.array[(int) key.getX()][(int) key.getY()];
	}

	@Override
	public void put(FixedInt2 key, Color value) {
		this.array[(int) key.getX()][(int) key.getY()] = value;
	}

	@Override
	public void print(String tag) {
		L.d("RedImageCache");
	}

}
