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
import com.badlogic.gdx.graphics.Texture;

public class Image {
	private Pixmap bitmap;
	private Texture texture;

	public Texture getTexture() {
		return texture;
	}
	public Pixmap getBitmap() {
		return bitmap;
	}

	public Image(Pixmap bitmap) {
		if (bitmap == null) {
			throw new NullPointerException();
		}
		this.bitmap = bitmap;
		this.texture = new Texture(bitmap);
	}

	public static Image createImage(int width, int height, Image reuse) {
		Pixmap bitmap = new Pixmap(width, height, Pixmap.Format.RGB888);
		if (reuse == null) {
			reuse = new Image(bitmap);
		}else {
			reuse.texture = new Texture(bitmap);
			reuse.bitmap = bitmap;
		}
		return reuse;
	}

	public static Image createTransparentImage(int width, int height) {
		return new Image(new Pixmap(width, height, Pixmap.Format.RGB888));
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
		return img;
	}

	public static Image createImage(Image image) {
		Pixmap pixImg = new Pixmap(image.bitmap.getWidth(), image.bitmap.getHeight(), Pixmap.Format.RGB888);
		return new Image(pixImg);
	}

	public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
		return new Image(createRGBIPixmap(rgb, 0,width, height, processAlpha));
	}

	public static Pixmap createRGBIPixmap(int[] rgb,int offset, int width, int height, boolean processAlpha){
		Pixmap px = new Pixmap(width, height, Pixmap.Format.RGB888);
		if (!processAlpha) {
			final int length = width * height;
			int[] rgbCopy = new int[length];
			System.arraycopy(rgb, 0, rgbCopy, 0, length);
			for (int i = offset; i < length; i++) {
				rgbCopy[i] |= 0xFF << 24;
				px.drawPixel(i%width,i/width,rgbCopy[i]);
			}
		}else{
			for (int i = offset; i < rgb.length; i++) {
				px.drawPixel(i%width,i/width,rgb[i]);
			}
		}
		return px;
	}

	public Graphics getGraphics() {
		Graphics graphics = new Graphics();
//		graphics.setCanvas(new Canvas() {
//			@Override
//			public void paint(Graphics g) {
//				g.drawImage(this,getX(),getY(),0);
//			}
//		}, bitmap);
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
	public void copyPixels(Image image){
		this.bitmap.drawPixmap(image.bitmap,0,0);
	}
}
