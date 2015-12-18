package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.geometry.Geometry;
import com.jfixby.cmns.api.geometry.Rectangle;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.image.LambdaColorMap;
import com.jfixby.cmns.api.image.LambdaColorMapSpecs;
import com.jfixby.cmns.api.lambda.img.λImage;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.desktop.DesktopAssembler;
import com.jfixby.cv.api.cv.CV;
import com.jfixby.cv.api.gwt.ImageGWT;
import com.jfixby.cv.red.gwt.RedCV;
import com.jfixby.cv.red.gwt.RedImageGWT;

public class MultiplyImages {

	public static final void main(String[] args) throws IOException {

		// ---Устанавливаем и инициализируем компоненты------------
		DesktopAssembler.setup();
		ImageGWT.installComponent(new RedImageGWT());
		CV.installComponent(new RedCV());

		// ---Читаем файлы-----------------------------
		File input_folder = LocalFileSystem.ApplicationHome().child("input");
		File output_folder = LocalFileSystem.ApplicationHome().child("output");

		File file_1 = input_folder.child("1.jpg");
		File file_2 = input_folder.child("2.jpg");

		ColorMap color_map_1 = readImage(file_1);
		ColorMap color_map_2 = readImage(file_2);

		// ---Конвертируем пикчи в λ-изображения---------------

		λImage image_1 = color_map_1.getLambdaImage();
		λImage image_2 = color_map_2.getLambdaImage();

		// ---Поменяем все размеры пикч на 512x512-------------

		Rectangle image_1_size = Geometry.newRectangle(color_map_1.getWidth(), color_map_1.getHeight());
		Rectangle image_2_size = Geometry.newRectangle(color_map_2.getWidth(), color_map_2.getHeight());

		Rectangle output_image_size = Geometry.newRectangle(512, 512);
		image_1 = CV.map(image_1, image_1_size, output_image_size);
		image_2 = CV.map(image_2, image_2_size, output_image_size);

		// --- Обрабатываем--------------------------
		λImage result = process(image_1, image_2, output_image_size);

		// --- Сохраняем результат -------------------
		File result_file = output_folder.child("result.png");
		saveResult(result, output_image_size, result_file);
	}

	private static λImage process(final λImage image_1, final λImage image_2, Rectangle output_image_size) {

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
		ArrayColorMap color_map = ImageGWT.readGWTColorMap(image_file);
		return color_map;
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
