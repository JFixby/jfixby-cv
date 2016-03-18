package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColorMapSpecs;
import com.jfixby.cmns.api.image.ColoredλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.math.FloatMath;
import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.red.desktop.image.RedImageAWT;

public class EdgesExample {

	public static void main(String[] args) throws IOException {
		// ---Устанавливаем и инициализируем компоненты------------
		DesktopAssembler.setup();
		ImageAWT.installComponent(new RedImageAWT());
		CV.installComponent(new RedCV());

		// ---Читаем файлы-----------------------------
		File input_folder = LocalFileSystem.ApplicationHome().child("input");
		File output_folder = LocalFileSystem.ApplicationHome().child("output");

		File file_1 = input_folder.child("3.jpg");

		ColorMap color_map_1 = readImage(file_1);

		// ---Конвертируем пикчу в λ-изображение---------------
		ColoredλImage image_1 = color_map_1.getLambdaImage();
		Rectangle image_1_size = Geometry.newRectangle(color_map_1.getWidth(), color_map_1.getHeight());

		// --- Обрабатываем--------------------------
		ColoredλImage result = производная(image_1, image_1_size);

		// --- Добавим яркости
		result = bright(result);

		// --- Инвертируем ----------------
		// result = CV.invert(result); // для этого надо разкоментировать эту
		// строку убрав палки // в начале

		// --- Сохраняем результат -------------------
		File result_file = output_folder.child("result.png");
		saveResult(result, image_1_size, result_file);

		// На выходе видны артефакты сжатия
	}

	private static ColoredλImage bright(ColoredλImage image_1) {
		return (x, y) -> {
			Color pixel = image_1.valueAt(x, y);
			float multiplier = 8;
			double R = pixel.red() * multiplier;
			double G = pixel.green() * multiplier;
			double B = pixel.blue() * multiplier;

			if (R > 1) {
				R = 1;
			}
			if (G > 1) {
				G = 1;
			}
			if (B > 1) {
				B = 1;
			}

			return Colors.newColor((float) R, (float) G, (float) B);
		};
	}

	public static ColoredλImage производная(ColoredλImage input_image, Rectangle input_image_dimentions) {
		return (x, y) -> {

			// Модули производных по x и y для красного канала
			double Rx = abs(input_image.valueAt(x + 1, y).red() - input_image.valueAt(x - 1, y).red()) / 2;
			double Ry = abs(input_image.valueAt(x, y + 1).red() - input_image.valueAt(x, y - 1).red()) / 2;

			// для зелёного
			double Gx = abs(input_image.valueAt(x + 1, y).green() - input_image.valueAt(x - 1, y).green()) / 2;
			double Gy = abs(input_image.valueAt(x, y + 1).green() - input_image.valueAt(x, y - 1).green()) / 2;

			// и для синего
			double Bx = abs(input_image.valueAt(x + 1, y).blue() - input_image.valueAt(x - 1, y).blue()) / 2;
			double By = abs(input_image.valueAt(x, y + 1).blue() - input_image.valueAt(x, y - 1).blue()) / 2;

			double R = Rx + Ry;
			double G = Gx + Gy;
			double B = Bx + By;

			return Colors.newColor((float) R, (float) G, (float) B);
		};
	}

	private static double abs(double f) {
		return FloatMath.abs(f);
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

	private static ColorMap readImage(File image_file) throws IOException {
		L.d("reading", image_file);
		ArrayColorMap color_map = ImageAWT.readAWTColorMap(image_file);
		return color_map;
	}

}
