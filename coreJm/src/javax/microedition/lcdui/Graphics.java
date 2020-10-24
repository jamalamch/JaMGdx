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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ScreenUtils;


public class Graphics {
	public static final int HCENTER = 1;
	public static final int VCENTER = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int TOP = 16;
	public static final int BOTTOM = 32;
	public static final int BASELINE = 64;

	public static final int SOLID = 0;
	public static final int DOTTED = 1;

	private Canvas canvas;
	private Pixmap canvasBitmap;

	private ShapeRenderer drawRender = new ShapeRenderer();
	private SpriteBatch spriteBatch = new SpriteBatch();
	private Rectangle clipRect = new Rectangle();
	private int translateX;
	private int translateY;

	private FloatArray path = new FloatArray();

	private int stroke;

	private Font font = new Font();

	public Graphics() {
		setStrokeStyle(SOLID);
	}

	public void reset() {
		setColor(0);
		setFont(Font.getDefaultFont());
		setStrokeStyle(SOLID);
		resetClip();
		resetTranslation();
	}

	private void resetTranslation() {
		translateX = 0;
		translateY = 0;
		spriteBatch.getTransformMatrix().setTranslation(0,0,0);
	}

	private void resetClip() {
		setClip(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
	}

	public void setCanvas(Canvas canvas, Pixmap canvasBitmap) {
//		if (canvas.getSaveCount() > 1) {
//			canvas.restoreToCount(1);
//		}
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//			canvas.save();
//		}
//		canvas.save();
		clipRect.set(canvas.getRectangle());
		this.canvas = canvas;
		this.canvasBitmap = canvasBitmap;
	}

	public void setSurfaceCanvas(Canvas canvas) {
		clipRect.set(canvas.getRectangle());
		this.canvas = canvas;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public boolean hasCanvas() {
		return canvas != null;
	}

	public void fillPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints) {
		if (nPoints > 0) {
			FloatArray path = computePath(xPoints, xOffset, yPoints, yOffset, nPoints);
			drawRender.set(ShapeRenderer.ShapeType.Filled);
			drawRender.polygon(path.items);
		}
	}

	public void drawPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints) {
		if (nPoints > 0) {
			FloatArray path = computePath(xPoints, xOffset, yPoints, yOffset, nPoints);
			drawRender.set(ShapeRenderer.ShapeType.Line);
			drawRender.polygon(path.items);
		}
	}

	private FloatArray computePath(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints) {
		path.clear();
		path.add((float) xPoints[xOffset], (float) yPoints[yOffset]);
		for (int i = 1; i < nPoints; i++) {
			path.add((float) xPoints[xOffset + i], (float) yPoints[yOffset + i]);
		}
		return path;
	}

	public void setColor(int color) {
		setColorAlpha(color | 0xFF000000);
	}

	public void setColorAlpha(int colorAlpha) {
		drawRender.getColor().set(colorAlpha);
	}

	public void setColor(int r, int g, int b) {
		drawRender.setColor( r/255, g/255, b/255,1);
	}

	public void setGrayScale(int value) {
		setColor(value, value, value);
	}

	public int getGrayScale() {
		return (getRedComponent() + getGreenComponent() + getBlueComponent()) / 3;
	}

	public int getRedComponent() {
		return (int)(drawRender.getColor().r*255);
	}

	public int getGreenComponent() {
		return (int)(drawRender.getColor().g*255);
	}

	public int getBlueComponent() {
		return (int)(drawRender.getColor().b*255);
	}

	public int getColor() {
		return drawRender.getColor().toIntBits();
	}

	public int getDisplayColor(int color) {
		return color;
	}

	public void setStrokeStyle(int stroke) {
		this.stroke = stroke;

//		if (stroke == DOTTED) {
//			drawRender.set(ShapeRenderer.ShapeType.Line);
//		} else {
//			drawRender.setPathEffect(null);
//		}
	}

	public int getStrokeStyle() {
		return stroke;
	}

	public void setFont(Font font) {
		if (font == null) {
			font = Font.getDefaultFont();
		}
		this.font = font;
//		font.copyInto(drawRender);
	}

	public Font getFont() {
		return font;
	}

	public void setClip(int x, int y, int width, int height) {
		clipRect.set(x, y, width, height);
		spriteBatch.getProjectionMatrix().setToOrtho2D(x , y, width, height);
		spriteBatch.getTransformMatrix().setTranslation(translateX, translateY,0);
		drawRender.getProjectionMatrix().setToOrtho2D(x , y, width, height);
	}

	public void clipRect(int x, int y, int width, int height) {
		// Calculate the clip
		clipRect.set(x, y, width, height);
		canvas.setBounds(x, y,  width,  height);
	}

	public int getClipX() {
		return (int)clipRect.getX();
	}

	public int getClipY() {
		return (int)clipRect.getY();
	}

	public int getClipWidth() {
		return (int)clipRect.getWidth();
	}

	public int getClipHeight() {
		return (int)clipRect.getHeight();
	}

	public void translate(int dx, int dy) {
		translateX += dx;
		translateY += dy;

		spriteBatch.getProjectionMatrix().setTranslation(translateX, translateY,-1);
	}

	public int getTranslateX() {
		return translateX;
	}

	public int getTranslateY() {
		return translateY;
	}

	public void clear(int color) {
//		canvas.drawColor(color, PorterDuff.Mode.SRC);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		if (x2 >= x1) {
			x2++;
		} else {
			x1++;
		}

		if (y2 >= y1) {
			y2++;
		} else {
			y1++;
		}
		drawRender.line(x1, y1, x2, y2);
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawArc(rectF, -startAngle, -arcAngle, false, drawRender);
		drawRender.arc(x,y,1,1,1,1);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawArc(rectF, -startAngle, -arcAngle, true, fillPaint);
		drawRender.set(ShapeRenderer.ShapeType.Filled);
		drawRender.arc(x,y,1,1,1,1);
	}

	public void drawRect(int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		drawRender.rect(x, y, width, height);
	}

	public void fillRect(int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		drawRender.set(ShapeRenderer.ShapeType.Filled);
		drawRender.rect(x, y, width, height);
	}

	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawRoundRect(rectF, arcWidth, arcHeight, drawRender);
		drawRender.rectLine(x, y, width, height, arcWidth);
	}


	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawRoundRect(rectF, arcWidth, arcHeight, fillPaint);

		drawRender.set(ShapeRenderer.ShapeType.Filled);
		drawRender.rectLine(x, y, width, height, arcWidth);

	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		fillPolygon(new int[]{x1, x2, x3}, 0, new int[]{y1, y2, y3}, 0, 3);
	}

	public void drawChar(char character, int x, int y, int anchor) {
		drawChars(new char[]{character}, 0, 1, x, y, anchor);
	}

	public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
		drawString(new String(data, offset, length), x, y, anchor);
	}

	public void drawString(String text, int x, int y, int anchor) {
		if (anchor == 0) {
			anchor = LEFT | TOP;
		}

		if ((anchor & Graphics.LEFT) != 0) {
		} else if ((anchor & Graphics.RIGHT) != 0) {
			x += clipRect.width;
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x += clipRect.width/2;
		}

		if ((anchor & Graphics.TOP) != 0) {
			y += clipRect.height;
		} else if ((anchor & Graphics.BOTTOM) != 0) {
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y += clipRect.height/2;
		}

		font.getPaint().draw(spriteBatch,text, x, y);
	}

	public void drawImage(Image image, int x, int y, int anchor) {
		if ((anchor & Graphics.RIGHT) != 0) {
			x -= image.getWidth();
		} else if ((anchor & Graphics.HCENTER) != 0) {
			x -= image.getWidth() / 2;
		}

		if ((anchor & Graphics.BOTTOM) != 0) {
			y -= image.getHeight();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y -= image.getHeight() / 2;
		}
		spriteBatch.draw(image.getTexture(), x, y);
//		canvas.drawBitmap(image.getBitmap(), x, y, null);
	}

	public void drawImage(Image image, int x, int y, int width, int height, boolean filter, int alpha) {
//		imagePaint.setFilterBitmap(filter);
//		imagePaint.setAlpha(alpha);
//
//		if (width > 0 && height > 0) {
//			rectF.set(x, y, x + width, y + height);
//			canvas.drawBitmap(image.getBitmap(), null, rectF, imagePaint);
//		} else {
//			canvas.drawBitmap(image.getBitmap(), x, y, imagePaint);
//		}
		Color color = new Color(spriteBatch.getColor());
		spriteBatch.setColor(1,1,1,alpha);
		spriteBatch.draw(image.getTexture(), x, y, width, height);
		spriteBatch.setColor(color);
	}

	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
		drawString(str.substring(offset, len + offset), x, y, anchor);
	}

	public void drawRegion(Image image, int srcx, int srcy, int width, int height, int transform, int dstx, int dsty, int anchor) {
		if (width == 0 || height == 0) return;

//		if (transform != 0) {
//			Rect srcR = new Rect(srcx, srcy, srcx + width, srcy + height);
//			RectF dstR = new RectF(0, 0, width, height);
//			RectF deviceR = new RectF();
//			Matrix4 matrix = Sprite.transformMatrix(transform, width / 2, height / 2);
//			matrix.mapRect(deviceR, dstR);
//
//			if ((anchor & Graphics.RIGHT) != 0) {
//				dstx -= deviceR.width();
//			} else if ((anchor & Graphics.HCENTER) != 0) {
//				dstx -= deviceR.width() / 2;
//			}
//			if ((anchor & Graphics.BOTTOM) != 0) {
//				dsty -= deviceR.height();
//			} else if ((anchor & Graphics.VCENTER) != 0) {
//				dsty -= deviceR.height() / 2;
//			}
//
//			canvas.save();
//			canvas.translate(-deviceR.left + dstx, -deviceR.top + dsty);
//			canvas.concat(matrix);
//			canvas.drawBitmap(image.getBitmap(), srcR, dstR, null);
//			canvas.restore();
//		} else {
			if ((anchor & Graphics.RIGHT) != 0) {
				dstx -= width;
			} else if ((anchor & Graphics.HCENTER) != 0) {
				dstx -= width / 2;
			}
			if ((anchor & Graphics.TOP) != 0) {
				dsty += height;
			} else if ((anchor & Graphics.VCENTER) != 0) {
				dsty += height / 2;
			}

			spriteBatch.draw(image.getTexture(),dstx,dsty,srcx,srcy,width,height);

//			Rect srcR = new Rect(srcx, srcy, srcx + width, srcy + height);
//			RectF dstR = new RectF(dstx, dsty, dstx + width, dsty + height);
//			canvas.drawBitmap(image.getBitmap(), srcR, dstR, null);
//		}
	}

	public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
		if (width == 0 || height == 0) return;
		// MIDP allows almost any value of scanlength, drawBitmap is more strict with the stride
		if (scanlength < width) {
			scanlength = width;
		}
		int rows = rgbData.length / scanlength;
		if (rows < height) {
			height = rows;
		}

		Pixmap pixmap = Image.createRGBIPixmap(rgbData, offset, width, height, processAlpha);
		Texture texture = new Texture(pixmap);
		// Use deprecated method due to perfomance issues
		spriteBatch.draw(texture, x, y, width, height);
	}

	public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {
//		Bitmap bitmap = Bitmap.createBitmap(canvasBitmap, x_src, y_src, width, height);
//		drawImage(new Image(bitmap), x_dest, y_dest, anchor);
	}

	public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
		Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, width, height);
		// = new int[offset + width*height];
		int j = 0;
		for(int i=offset;i<pixels.length;i+=stride){
			y++;
			pixels[i] = pixmap.getPixel(x +(j%width),y +((j/width)));
		}
		pixmap.dispose();
	}

	public Pixmap getBitmap() {
		return canvasBitmap;
	}
}
