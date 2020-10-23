/*
 * Copyright 2012 Kulikov Dmitriy
 * Copyright 2017-2018 Nikita Shakarun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class Image {
	private Pixmap bitmap;

	public Image(Pixmap bitmap) {
		if (bitmap == null) {
			throw new NullPointerException();
		}
		this.bitmap = bitmap;
	}

	public static Image createImage(int width, int height, Image reuse) {
		Pixmap bitmap = new Pixmap(width, height, Pixmap.Format.RGB888);
		if (reuse == null && reuse.bitmap == null) {
			return new Image(bitmap);
		}
		bitmap.drawPixmap(reuse.bitmap, 0, 0);
		return new Image(bitmap);
	}

	public static Image createTransparentImage(int width, int height) {
		return new Image(new Pixmap(width, height, Pixmap.Format.RGB888));
	}

	public Pixmap getBitmap() {
		return bitmap;
	}

	public static Image createImage(int width, int height) {
		Pixmap b = new Pixmap(width, height, Pixmap.Format.RGB888);
		b.setColor(Color.WHITE);
		b.fill();
		return new Image(b);
	}

	public static Image createImage(String resname) throws IOException {
			Pixmap b = new Pixmap(Gdx.files.absolute(resname));
			return new Image(b);
	}

	public static Image createImage(InputStream stream) throws IOException {
		int l = stream.read();
		byte[] pixmapb = new byte[l];
		stream.read(pixmapb);
		return createImage(pixmapb, 0, l);
	}

	public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
		Pixmap b = new Pixmap(imageData, imageOffset, imageLength);
		if (b == null) {
			throw new IllegalArgumentException("Can't decode image");
		}
		return new Image(b);
	}

	public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
		Image img = createTransparentImage(width,  height);
		img.bitmap.drawPixmap(image.bitmap, x, y);
		return img;
//		return new Image(new Pixmap(image.bitmap, x, y, width, height, Sprite.transformMatrix(transform, width / 2f, height / 2f), false));
	}

	public static Image createImage(Image image) {
		Pixmap pixImg = new Pixmap(image.bitmap.getWidth(), image.bitmap.getHeight(), Pixmap.Format.RGB888);
		pixImg.drawPixmap(image.bitmap, 0, 0);
		return new Image(pixImg);
	}

	public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
		Pixmap px = new Pixmap(width, height, Pixmap.Format.RGB888);
		if (!processAlpha) {
			final int length = width * height;
			int[] rgbCopy = new int[length];
			System.arraycopy(rgb, 0, rgbCopy, 0, length);
			for (int i = 0; i < length; i++) {
				rgbCopy[i] |= 0xFF << 24;
				px.drawPixel(i%width,i/width,rgbCopy[i]);
			}
		}
		return new Image(px);
	}

	public Graphics getGraphics() {
		Graphics graphics = new Graphics();
		graphics.setCanvas(new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage();
			}
		}, bitmap);
		return graphics;
	}

	public boolean isMutable() {
		return false;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {

	}
}
