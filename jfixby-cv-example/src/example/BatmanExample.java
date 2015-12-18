package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.angles.Angles;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.image.LambdaColorMap;
import com.jfixby.cmns.api.image.LambdaColorMapSpecs;
import com.jfixby.cmns.api.lambda.img.λImage;
import com.jfixby.cmns.api.lambda.img.bin.λBinaryImage;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cmns.api.math.MathTools;
import com.jfixby.cmns.api.math.VectorTool;
import com.jfixby.cmns.desktop.DesktopAssembler;
import com.jfixby.cv.api.cv.CV;
import com.jfixby.cv.api.gwt.ImageGWT;
import com.jfixby.cv.red.gwt.RedCV;
import com.jfixby.cv.red.gwt.RedImageGWT;

public class BatmanExample {

	private static List<Integer> prime_numbers;

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		DesktopAssembler.setup();
		ImageGWT.installComponent(new RedImageGWT());
		CV.installComponent(new RedCV());

		// ---Соберём немного простых чисел-----------------------------

		prime_numbers = collect_primes(512);
		// prime_numbers.print("простые числа");

		// ---Задаём размер изображения-----------------------------
		Rectangle image_dimentions = Geometry.newRectangle(512, 512);
		int W = (int) image_dimentions.getWidth();
		int H = (int) image_dimentions.getHeight();
		image_dimentions.setOriginRelative(0.5, 0.5);

		{

			// --- Создаём текстуру--------------------------
			λBinaryImage pattern = generatePattern(W, H);

			// --- Раскрашиваем её ---------------------------
			λImage result = (x, y) -> {
				int X = (int) FloatMath.round(x);
				int Y = (int) FloatMath.round(y);
				if (pattern.valueAt(X, Y)) {
					return Colors.GREEN();
				} else {
					return Colors.BLACK();
				}
			};

			// --- Сохраняем результат -------------------
			Rectangle output_image_size = Geometry.newRectangle(512, 512);
			result = CV.map(result, image_dimentions, output_image_size);
			File output_folder = LocalFileSystem.ApplicationHome().child("output");
			File result_file = output_folder.child("pattern-batman" + ".png");
			saveResult(result, output_image_size, result_file);
		}

	}

	static λBinaryImage generatePattern(int W, int H) {

		int img_center_horizontal = W / 2;
		int img_center_vertical = H / 2;

		int patch_size = 16;
		int patch_width = patch_size;
		int patch_center_horizontal = patch_width / 2;
		int patch_height = patch_size;
		int patch_center_vertical = patch_height / 2;

		return (a, b) -> {

			int x = (int) FloatMath.round(b + patch_center_horizontal);
			int y = (int) FloatMath.round(a + patch_center_vertical);

			// --- Разбиваем ковёр на заплатки--------------------------
			int Xp = (int) x % patch_width;
			int Yp = (int) y % patch_height;

			int i = (int) x / patch_width;
			int j = (int) y / patch_height;

			int iMax = (int) W / patch_width;
			int jMax = (int) H / patch_height;

			// --- Делаем заплатки симметричными--------------------------
			if (i % 2 == 0 && j % 2 == 0) {
				return true;
			}

			if (i % 2 != 0 && j % 2 == 0) {
				if (Xp > Yp) {
					return Xp - patch_width / 2 > Yp;
				} else {
					return Xp + patch_width / 2 > Yp;
				}
			}

			if (i % 2 == 0 && j % 2 != 0) {
				if (Xp > Yp) {
					return Xp - patch_width / 2 < Yp;
				} else {
					return Xp + patch_width / 2 < Yp;
				}
			}

			return false;
		};
	}

	private static List<Integer> collect_primes(int N) {

		List<Integer> list = Collections.newList();

		boolean NOT_PRIME = true;

		BooleanArray array = new BooleanArray(N);
		// long prime = 1;
		for (int i = 2; i < N; i++) {
			if (array.get(i) == false) {
				// L.d("prime[" + prime + "]", i);

				list.add(i);
				// prime++;
				long val = 2 * i;
				for (long k = 2; val < N;) {
					array.set((int) val, NOT_PRIME);
					k++;
					val = i * k;
				}
			}

		}

		return list;
	}

	private static void saveResult(λImage image, Rectangle output_image_size, File output_image_file) throws IOException {

		LambdaColorMapSpecs lambda_specs = ImageProcessing.newLambdaColorMapSpecs();
		int w = (int) output_image_size.getWidth();
		int h = (int) output_image_size.getHeight();
		lambda_specs.setColorMapWidth(w);
		lambda_specs.setColorMapHeight(h);
		lambda_specs.setLambdaColoredImage(image);
		LambdaColorMap color_map = ImageProcessing.newLambdaColorMap(lambda_specs);
		BufferedImage gwt_bw = ImageGWT.toGWTImage(color_map);
		L.d("writing", output_image_file);
		ImageGWT.writeToFile(gwt_bw, output_image_file, "png");
	}

}
