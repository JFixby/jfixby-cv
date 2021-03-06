package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.geometry.Geometry;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.lambda.img.bin.λBinaryImage;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;

public class BatmanExample {

	private static List<Integer> prime_numbers;

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		ScarabeiDesktop.deploy();
		ImageAWT.installComponent(new RedImageAWT());
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
			ColoredλImage result = (x, y) -> {
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

	private static void saveResult(ColoredλImage image, Rectangle output_image_size, File output_image_file) throws IOException {

		ColorMapSpecs lambda_specs = ImageProcessing.newColorMapSpecs();
		int w = (int) output_image_size.getWidth();
		int h = (int) output_image_size.getHeight();
		lambda_specs.setColorMapWidth(w);
		lambda_specs.setColorMapHeight(h);
		lambda_specs.setLambdaColoredImage(image);
		ColorMap color_map = ImageProcessing.newColorMap(lambda_specs);
		BufferedImage gwt_bw = ImageAWT.toAWTImage(color_map);
		L.d("writing", output_image_file);
		ImageAWT.writeToFile(gwt_bw, output_image_file, "png");
	}

}
