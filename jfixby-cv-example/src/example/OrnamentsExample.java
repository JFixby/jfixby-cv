package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.DesktopSetup;
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
import com.jfixby.scarabei.api.lambda.img.bin.λBinaryImageOperation;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;

public class OrnamentsExample {

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		DesktopSetup.deploy();
		ImageAWT.installComponent(new RedImageAWT());
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
