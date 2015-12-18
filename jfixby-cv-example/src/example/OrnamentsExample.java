package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
import com.jfixby.cmns.api.lambda.img.bin.λBinaryImageOperation;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cmns.desktop.DesktopAssembler;
import com.jfixby.cv.api.cv.CV;
import com.jfixby.cv.api.gwt.ImageGWT;
import com.jfixby.cv.red.gwt.RedCV;
import com.jfixby.cv.red.gwt.RedImageGWT;

public class OrnamentsExample {

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		DesktopAssembler.setup();
		ImageGWT.installComponent(new RedImageGWT());
		CV.installComponent(new RedCV());

		// ---Задаём размер изображения-----------------------------
		Rectangle image_dimentions = Geometry.newRectangle(128, 128);
		int W = (int) image_dimentions.getWidth();
		int H = (int) image_dimentions.getHeight();
		image_dimentions.setOriginRelative(0.5, 0.5);

		// ---Список "интересных" параметров
//		List<Integer> interesting = Collections.newList(3 * 2 * 2, 3 * 2 * 2 * 2);

		for (int divisor = 2; divisor < 30; divisor++) {

			// --- Создаём текстуру--------------------------
			int DIV = divisor;
			λBinaryImage pattern = generatePattern(W, H, DIV);

			// --- Раскрашиваем её ---------------------------
			λImage result = (x, y) -> {
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
			File result_file = output_folder.child("pattern-" + (DIV) + ".png");
			saveResult(result, output_image_size, result_file);
		}

	}

	private static λBinaryImage generatePattern(int w, int h, int DIV) {
		λBinaryImage result = (x, y) -> false;

		result = XOR.apply(result, div(64));
		result = XOR.apply(result, div(32));
		result = XOR.apply(result, div(16));
		result = XOR.apply(result, div(8));
		result = XOR.apply(result, div(DIV));

		final λBinaryImage tmp = result;

		result = (x, y) -> {
			int offset_x = (y / DIV) * DIV;
			int offset_y = (x / DIV) * DIV;
			return tmp.valueAt(x + offset_x, y + offset_y);
		};

		return result;
	}

	private static λBinaryImage div(int DIV) {
		return (x, y) -> x * y % (DIV) == 0;
	}

	static λBinaryImageOperation XOR = (a, b) -> (x, y) -> a.valueAt(x, y) ^ b.valueAt(x, y);

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
