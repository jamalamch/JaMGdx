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

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.FloatArray;

import javax.microedition.lcdui.game.Sprite;

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

	private int translateX;
	private int translateY;

	private Rect clipRect = new Rect();
	private Rect canvasRect = new Rect();
	private RectF rectF = new RectF();
	private FloatArray path = new FloatArray();

	private DashPathEffect dpeffect = new DashPathEffect(new float[]{5, 5}, 0);
	private int stroke;

	private boolean drawAntiAlias;
	private boolean textAntiAlias;

	private Font font = Font.getDefaultFont();

	public Graphics() {
		setStrokeStyle(SOLID);
		setAntiAlias(false);
		setAntiAliasText(true);
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
	}

	private void resetClip() {
		setClip(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void setCanvas(Canvas canvas, Pixmap canvasBitmap) {
		if (canvas.getSaveCount() > 1) {
			canvas.restoreToCount(1);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			canvas.save();
		}
		canvas.save();
		canvas.getClipBounds(canvasRect);
		clipRect.set(canvasRect);
		this.canvas = canvas;
		this.canvasBitmap = canvasBitmap;
	}

	public void setSurfaceCanvas(Canvas canvas) {
		canvas.getClipBounds(canvasRect);
		clipRect.set(canvasRect);
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

	public void setColorAlpha(int colorf) {
		fillPaint.getColor().a=colorf/255;
		drawRender.getColor().a=colorf/255;
	}

	public void setColor(int r, int g, int b) {
		fillPaint.setColor( r/255, g/255, b/255,1);
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

	private void setAntiAlias(boolean aa) {
		drawAntiAlias = aa;
//		drawRender.setAntiAlias(aa);
	}

	private void setAntiAliasText(boolean aa) {
		textAntiAlias = aa;
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
		clipRect.set(x, y, x + width, y + height);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			canvas.restore();
			canvas.save();
			canvas.translate(translateX, translateY);
			canvas.clipRect(clipRect);
		} else {
			canvas.clipRect(clipRect, Region.Op.REPLACE);
		}
		// Calculate the clip
		clipRect.offset(translateX, translateY);
		clipRect.sort();
		if (!clipRect.intersect(canvasRect)) {
			clipRect.set(translateX, translateY, translateX, translateY);
		}
	}

	public void clipRect(int x, int y, int width, int height) {
		canvas.clipRect(x, y, x + width, y + height);
		// Calculate the clip
		clipRect.offset(-translateX, -translateY);
		clipRect.sort();
		clipRect.intersect(x, y, x + width, y + height);
		clipRect.offset(translateX, translateY);
	}

	public int getClipX() {
		return clipRect.left - translateX;
	}

	public int getClipY() {
		return clipRect.top - translateY;
	}

	public int getClipWidth() {
		return clipRect.width();
	}

	public int getClipHeight() {
		return clipRect.height();
	}

	public void translate(int dx, int dy) {
		translateX += dx;
		translateY += dy;

		canvas.translate(dx, dy);
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
		drawRender.arc(x,y,1,1,1,1);	}

	public void drawArc(RectF oval, int startAngle, int arcAngle) {
		canvas.drawArc(oval, -startAngle, -arcAngle, false, drawRender);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawArc(rectF, -startAngle, -arcAngle, true, fillPaint);
		drawRender.set(ShapeRenderer.ShapeType.Filled);
		drawRender.arc(x,y,1,1,1,1);
	}

	public void fillArc(RectF oval, int startAngle, int arcAngle) {
		canvas.drawArc(oval, -startAngle, -arcAngle, true, fillPaint);
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

	public void drawRoundRect(RectF rect, int arcWidth, int arcHeight) {
		canvas.drawRoundRect(rect, arcWidth, arcHeight, drawRender);
	}

	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (width <= 0 || height <= 0) return;
//		rectF.set(x, y, x + width, y + height);
//		canvas.drawRoundRect(rectF, arcWidth, arcHeight, fillPaint);

		drawRender.set(ShapeRenderer.ShapeType.Filled);
		drawRender.rectLine(x, y, width, height, arcWidth);

	}

	public void fillRoundRect(RectF rect, int arcWidth, int arcHeight) {
		canvas.drawRoundRect(rect, arcWidth, arcHeight, fillPaint);
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
			drawRender.setTextAlign(Paint.Align.LEFT);
		} else if ((anchor & Graphics.RIGHT) != 0) {
			drawRender.setTextAlign(Paint.Align.RIGHT);
		} else if ((anchor & Graphics.HCENTER) != 0) {
			drawRender.setTextAlign(Paint.Align.CENTER);
		}

		if ((anchor & Graphics.TOP) != 0) {
			y -= drawRender.ascent();
		} else if ((anchor & Graphics.BOTTOM) != 0) {
			y -= drawRender.descent();
		} else if ((anchor & Graphics.VCENTER) != 0) {
			y -= drawRender.ascent() + (drawRender.descent() - drawRender.ascent()) / 2;
		}

		drawRender.setAntiAlias(textAntiAlias);
		drawRender.setStyle(Paint.Style.FILL);
		canvas.drawText(text, x, y, drawRender);
		drawRender.setStyle(Paint.Style.STROKE);
		drawRender.setAntiAlias(drawAntiAlias);
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

		canvas.drawBitmap(image.getBitmap(), x, y, null);
	}

	public void drawImage(Image image, int x, int y, int width, int height, boolean filter, int alpha) {
		imagePaint.setFilterBitmap(filter);
		imagePaint.setAlpha(alpha);

		if (width > 0 && height > 0) {
			rectF.set(x, y, x + width, y + height);
			canvas.drawBitmap(image.getBitmap(), null, rectF, imagePaint);
		} else {
			canvas.drawBitmap(image.getBitmap(), x, y, imagePaint);
		}
	}

	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
		drawString(str.substring(offset, len + offset), x, y, anchor);
	}

	public void drawRegion(Image image, int srcx, int srcy, int width, int height, int transform, int dstx, int dsty, int anchor) {
		if (width == 0 || height == 0) return;

		if (transform != 0) {
			Rect srcR = new Rect(srcx, srcy, srcx + width, srcy + height);
			RectF dstR = new RectF(0, 0, width, height);
			RectF deviceR = new RectF();
			Matrix matrix = Sprite.transformMatrix(transform, width / 2, height / 2);
			matrix.mapRect(deviceR, dstR);

			if ((anchor & Graphics.RIGHT) != 0) {
				dstx -= deviceR.width();
			} else if ((anchor & Graphics.HCENTER) != 0) {
				dstx -= deviceR.width() / 2;
			}
			if ((anchor & Graphics.BOTTOM) != 0) {
				dsty -= deviceR.height();
			} else if ((anchor & Graphics.VCENTER) != 0) {
				dsty -= deviceR.height() / 2;
			}

			canvas.save();
			canvas.translate(-deviceR.left + dstx, -deviceR.top + dsty);
			canvas.concat(matrix);
			canvas.drawBitmap(image.getBitmap(), srcR, dstR, null);
			canvas.restore();
		} else {
			if ((anchor & Graphics.RIGHT) != 0) {
				dstx -= width;
			} else if ((anchor & Graphics.HCENTER) != 0) {
				dstx -= width / 2;
			}
			if ((anchor & Graphics.BOTTOM) != 0) {
				dsty -= height;
			} else if ((anchor & Graphics.VCENTER) != 0) {
				dsty -= height / 2;
			}

			Rect srcR = new Rect(srcx, srcy, srcx + width, srcy + height);
			RectF dstR = new RectF(dstx, dsty, dstx + width, dsty + height);
			canvas.drawBitmap(image.getBitmap(), srcR, dstR, null);
		}
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
		if (!processAlpha) {
			for (int iy = 0; iy < height; iy++) {
				for (int ix = 0; ix < width; ix++) {
					rgbData[offset + ix + iy * scanlength] |= (0xFF << 24);
				}
			}
		}
		// Use deprecated method due to perfomance issues
		canvas.drawBitmap(rgbData, offset, scanlength, x, y, width, height, processAlpha, null);
	}

	public void copyArea(int x_src, int y_src, int width, int height,
						 int x_dest, int y_dest, int anchor) {
		Bitmap bitmap = Bitmap.createBitmap(canvasBitmap, x_src, y_src, width, height);
		drawImage(new Image(bitmap), x_dest, y_dest, anchor);
	}

	public void getPixels(int[] pixels, int offset, int stride,
						  int x, int y, int width, int height) {
		canvasBitmap.getPixels(pixels, offset, stride, x, y, width, height);
	}

	public Bitmap getBitmap() {
		return canvasBitmap;
	}
}
