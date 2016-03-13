package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColorMapSpecs;
import com.jfixby.cmns.api.image.ColoredλImage;
import com.jfixby.cmns.api.lambda.img.bin.λBinaryImage;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cv.api.CV;
import com.jfixby.cv.red.awt.RedCV;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.red.desktop.image.RedImageAWT;

public class OrnamentsExample2 {

	private static List<Integer> prime_numbers;

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		DesktopAssembler.setup();
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
					return Colors.PURPLE();
				} else {
					return Colors.BLACK();
				}
			};

			// --- Сохраняем результат -------------------
			Rectangle output_image_size = Geometry.newRectangle(512, 512);
			result = CV.map(result, image_dimentions, output_image_size);
			File output_folder = LocalFileSystem.ApplicationHome().child("output");
			File result_file = output_folder.child("pattern-x" + ".png");
			saveResult(result, output_image_size, result_file);
		}

	}

	static λBinaryImage generatePattern(int W, int H) {

		int img_center_horizontal = W / 2;
		int img_center_vertical = H / 2;

		int patch_size = 128;
		int patch_width = patch_size;
		int patch_center_horizontal = patch_width / 2;
		int patch_height = patch_size;
		int patch_center_vertical = patch_height / 2;

		return (x, y) -> {
			if (x >= img_center_horizontal) {
				x = W - 1 - x;
			}
			if (y >= img_center_vertical) {
				y = H - 1 - y;
			}

			// --- Разбиваем ковёр на заплатки--------------------------
			int Xp = (int) x % patch_width;
			int Yp = (int) y % patch_height;

			int i = (int) x / patch_width;
			int j = (int) y / patch_height;

			int iMax = (int) W / patch_width;
			int jMax = (int) H / patch_height;

			// --- Делаем заплатки симметричными--------------------------
			if (Xp >= patch_center_horizontal) {
				Xp = patch_width - 1 - Xp;
			}
			if (Yp >= patch_center_vertical) {
				Yp = patch_height - 1 - Yp;
			}

			if (prime_numbers.contains(Xp * 2 + Yp)) {
				return true;
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
