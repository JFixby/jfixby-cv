package com.jfixby.cv.red.test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.filesystem.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMapSpecs;
import com.jfixby.cmns.api.image.EditableColorMap;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cmns.desktop.DesktopAssembler;
import com.jfixby.cv.api.gwt.ImageGWT;
import com.jfixby.cv.red.gwt.RedImageGWT;

public class LambdaImage {

	public interface F {
		double value(double x, double y);
	}

	public interface A {
		F apply(F f);
	}

	public interface FVec {
		F index(int i);
	}

	public interface FUnitVec {
		FVec index(int i);
	}

	public interface G {
		FVec apply(F f);
	}

	public interface FSwitch {
		F value(boolean k);
	}

	public interface FVecOP {
		FVec apply(FVec a, FVec b);
	}

	public interface FVecF {
		F apply(FVec v);
	}

	public interface FVecScalarOP {
		FVec apply(FVec v, F k);
	}

	// /////////////////////////

	public interface Fop {
		F apply(F a, F b);
	}

	public interface Aop {
		A apply(A a, A b);
	}

	public interface Aswitch {
		A value(boolean value);
	}

	public interface AVec {
		A index(int i);
	}

	public interface AUnitVector {
		AVec index(int k);
	}

	public interface AVecScalarOp {
		AVec apply(AVec vector, A k);
	}

	public interface AVecVectorOp {
		AVec apply(AVec a, AVec b);
	}

	public interface Operator {
		AVec apply(F f);
	}

	public interface AVecProjector {
		double value(AVec V);
	}

	public interface Aproperty {
		double value(A a);
	}

	public static int H = 768;
	public static int W = 1024;
	private static double PI = 0;

	public static void main(String[] args) throws IOException {
		DesktopAssembler.setup();
		ImageGWT.installComponent(new RedImageGWT());
		PI = FloatMath.PI();

		F image = (x, y) -> (sin(x / PI) + sin(y / PI));

		// drop("input.png", image);
		image = load("input.png");

		F FZero = (x, y) -> 0f;
		F FOne = (x, y) -> 1f;

		A ddx = f -> ((x, y) -> (f.value(x + 1, y) - f.value(x - 1, y)) / 2f);
		A ddy = f -> ((x, y) -> (f.value(x, y + 1) - f.value(x, y - 1)) / 2f);

		FUnitVec Ek = k -> (i -> (i == k) ? FOne : FZero);
		FVec E0 = Ek.index(0);
		FVec E1 = Ek.index(1);

		Fop FplusF = (a, b) -> ((x, y) -> (a.value(x, y) + b.value(x, y)));
		Fop FmuliplyF = (a, b) -> ((x, y) -> (a.value(x, y) * b.value(x, y)));

		FVecOP FVecSum = (a, b) -> (i -> (FplusF.apply(a.index(i), b.index(i))));
		FVecOP FVecMul = (a, b) -> (i -> (FmuliplyF.apply(a.index(i), b.index(i))));
		FVecScalarOP FVecMulS = (V, s) -> (i -> (FmuliplyF.apply(V.index(i), s)));

		G gradient = f -> FVecSum.apply(FVecMulS.apply(E0, ddx.apply(f)), FVecMulS.apply(E1, ddy.apply(f)));

		FVec gradF = gradient.apply(image);

		F gradX = ddx.apply(image);
		F gradY = ddy.apply(image);

		F Go = gradF.index(0);
		F G1 = gradF.index(1);

		FVecF norm2_a = v -> ((x, y) -> sqrt(square(v.index(0).value(x, y)) + square(v.index(1).value(x, y))));
		// FVecF norm2_b = v -> ((x, y) -> sqrt(square(gradX.value(x, y)) +
		// square(gradY.value(x, y))));

		F sketchy = norm2_a.apply(gradF);
		F sketchy2 = ((x, y) -> sqrt(square(gradX.value(x, y)) + square(gradY.value(x, y))));
		sketchy = enhance(sketchy);
		sketchy2 = enhance(sketchy2);

		drop("sketchy.png", sketchy);
		drop("sketchy2.png", sketchy2);

		L.d("exit");

	}

	private static F enhance(F sketchy) {
		sketchy = invert(sketchy);
		sketchy = power2(sketchy);
		return sketchy;
	}

	private static F power2(F sketchy) {
		return (x, y) -> square(sketchy.value(x, y));
	}

	private static F invert(F sketchy) {
		return (x, y) -> (1 - sketchy.value(x, y));
	}

	private static F load(String file_name) throws IOException {
		File image_file = LocalFileSystem.ApplicationHome().child(file_name);
		L.d("reading", image_file);
		EditableColorMap image = ImageGWT.readGWTColorMap(image_file);
		H = image.getHeight();
		W = image.getWidth();
		return (x, y) -> image.getValue((int) x, (int) y).getGrayscaleValue();
	}

	private static void drop(String file_name, F image) throws IOException {

		ArrayColorMapSpecs img_specs = ImageProcessing.newArrayColorMapSpecs();
		img_specs.setHeight(H);
		img_specs.setWidth(W);
		EditableColorMap image_F = ImageProcessing.newArrayColorMap(img_specs);
		draw(image, image_F);
		save(image_F, file_name);

	}

	private static void save(EditableColorMap image_F, String file_name) throws IOException {
		File image_file = LocalFileSystem.ApplicationHome().child(file_name);
		L.d("writing", image_file);
		BufferedImage javaImage = ImageGWT.toGWTImage(image_F);
		ImageGWT.writeToFile(javaImage, image_file, "png");

	}

	private static void draw(F image, EditableColorMap image_F) {
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				double x = i;
				double y = j;
				float gray = (float) FloatMath.limit(0, image.value(x, y), 1);
				Color color_value = Colors.newGray(gray);
				image_F.setValue(i, j, color_value);

			}
		}
	}

	private static double sqrt(double f) {
		return Math.sqrt(f);
	}

	private static double square(double value) {
		return value * value;
	}

	static double cos(double x) {
		return Math.cos(x);
	}

	static double sin(double x) {
		return Math.sin(x);
	}

	private static double abs(double value) {
		return Math.abs(value);
	}
}
