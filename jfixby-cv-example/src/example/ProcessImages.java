package example;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.desktop.DesktopSetup;
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
import com.jfixby.cmns.api.math.MathTools;
import com.jfixby.cmns.api.math.Matrix;
import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.red.desktop.image.RedImageAWT;

public class ProcessImages {

	public static final void main(String[] args) throws IOException {

		// ---Устанавливаем и инициализируем компоненты------------
		DesktopSetup.deploy();
		ImageAWT.installComponent(new RedImageAWT());
		CV.installComponent(new RedCV());

		// ---Читаем файлы-----------------------------
		File input_folder = LocalFileSystem.ApplicationHome().child("input");
		File output_folder = LocalFileSystem.ApplicationHome().child("output");

		File file_1 = input_folder.child("1.jpg");
		File file_2 = input_folder.child("2.jpg");

		ColorMap color_map_1 = readImage(file_1);
		ColorMap color_map_2 = readImage(file_2);

		// ---Конвертируем пикчи в λ-изображения---------------

		ColoredλImage image_1 = color_map_1.getLambdaImage();
		ColoredλImage image_2 = color_map_2.getLambdaImage();

		// ---Поменяем все размеры пикч на 512x512-------------

		Rectangle image_1_size = Geometry.newRectangle(color_map_1.getWidth(), color_map_1.getHeight());
		Rectangle image_2_size = Geometry.newRectangle(color_map_2.getWidth(), color_map_2.getHeight());

		Rectangle output_image_size = Geometry.newRectangle(512, 512);
		image_1 = CV.map(image_1, image_1_size, output_image_size);
		image_2 = CV.map(image_2, image_2_size, output_image_size);

		// --- Обрабатываем--------------------------
		ColoredλImage result = process(image_1, image_2, output_image_size);

		// --- Сохраняем результат -------------------
		File result_file = output_folder.child("result.png");
		saveResult(result, output_image_size, result_file);
	}

	private static ColoredλImage process(final ColoredλImage image_1, final ColoredλImage image_2, Rectangle output_image_size) {

		long seed = System.currentTimeMillis();
		L.d("seed", seed);
		Random random = new Random(seed);
		Matrix operator = MathTools.newMatrix(6, 3);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				double value = random.nextFloat() / 4;
				if (i == j) {
					value = 1;
				}

				operator.setValue(i, j, value);
			}
		}

		return (x, y) -> {
			Color color_1 = image_1.valueAt(x, y);
			Color color_2 = image_2.valueAt(x, y);

			int x_i = (int) x;
			int y_i = (int) y;

			float[] channels = new float[3];

			operate(x_i, y_i, channels, color_1, color_2, operator);

			float red = channels[0];
			float green = channels[1];
			float blue = channels[2];

			CustomColor color_result = Colors.newColor();
			color_result.setAlpha(1);
			color_result.setRed(red);
			color_result.setGreen(green);
			color_result.setBlue(blue);

			return color_result;
		};
	}

	private static void operate(int x, int y, float[] channels, Color color_1, Color color_2, Matrix operator) {

		if (x % 3 == 0 && y % 2 == 0) {
			channels[0] = (float) (color_2.red());
			channels[1] = (float) (color_2.green());
			channels[2] = (float) (color_2.blue());
		} else {
			if (x % 2 != 0 && y % 3 == 0) {

				channels[0] = (float) (color_1.red() * color_2.red());
				channels[1] = (float) (color_1.green() * color_2.green());
				channels[2] = (float) (color_1.blue() * color_2.blue());

			} else {
				channels[0] = (float) FloatMath.power(color_1.red(), 0.5);
				channels[1] = (float) (color_1.green());
				channels[2] = (float) (color_1.blue());
			}

		}
	}

	private static float operation(Rectangle output_image_size, float x, float y, float a, float b) {
		// return (((int) (a * 255)) + ((int) (b * 255))) / 255;
		double relative_x = x / output_image_size.getWidth();
		double relative_y = y / output_image_size.getHeight();
		int x_i = (int) x;
		int y_i = (int) y;
		if (x_i * y_i % 4 == 0) {
			return a;
		} else {
			return b;
		}
		// double result = (a * FloatMath.power(relative_x, 100) + b * (1 -
		// FloatMath.power(relative_x, 100))) / 2;
		// return (float) result;

	}

	private static ColorMap readImage(File image_file) throws IOException {
		L.d("reading", image_file);
		ArrayColorMap color_map = ImageAWT.readAWTColorMap(image_file);
		return color_map;
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
