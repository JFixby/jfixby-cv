package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cv.api.CV;
import com.jfixby.cv.argb.red.RedCV;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.geometry.Geometry;
import com.jfixby.scarabei.api.geometry.Rectangle;
import com.jfixby.scarabei.api.image.ArrayColorMap;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;

public class MultiplyImages {

	public static final void main(String[] args) throws IOException {

		// ---Устанавливаем и инициализируем компоненты------------
		ScarabeiDesktop.deploy();
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

		return (x, y) -> {
			Color color_2 = image_1.valueAt(x, y);
			Color color_1 = image_2.valueAt(x, y);

			double red = color_1.red() * color_2.red();
			double green = color_1.green() * color_2.green();
			double blue = color_1.blue() * color_2.blue();

			Color color_result = Colors.newColor((float) 1, (float) red, (float) green, (float) blue);

			return color_result;
		};
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
