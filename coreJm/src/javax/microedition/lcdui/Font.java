/*
 * Copyright 2012 Kulikov Dmitriy
 * Copyright 2017 Nikita Shakarun
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

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import ja.mgdx.JaMGDx;

import java.util.Arrays;

import javax.microedition.util.ContextHolder;

public class Font {
	public static final int FACE_MONOSPACE = 32;
	public static final int FACE_PROPORTIONAL = 64;
	public static final int FACE_SYSTEM = 0;

	public static final int SIZE_LARGE = 16;
	public static final int SIZE_MEDIUM = 0;
	public static final int SIZE_SMALL = 8;

	public static final int STYLE_BOLD = 1;
	public static final int STYLE_ITALIC = 2;
	public static final int STYLE_PLAIN = 0;
	public static final int STYLE_UNDERLINED = 4;

	private static final int SIZE_KEYBOARD = 22;

	private static float[] sizes = new float[]{18, 22, 26};

	public static void setSize(int size, float value) {
		switch (size) {
			case SIZE_SMALL:
				sizes[0] = value;
				break;

			case SIZE_MEDIUM:
				sizes[1] = value;
				break;

			case SIZE_LARGE:
				sizes[2] = value;
				break;

			default:
				return;
		}
	}

	public BitmapFont getPaint() {
		return paint;
	}

	private BitmapFont paint;
	private int face, style ,size;

	public Font(BitmapFont paint,int face, int style ,int size, boolean underline) {
		this.paint =paint;
		this.style = style;
		this.face = face;
		this.size = size;
//		paint.setTypeface(Typeface.create(face, style));
//		paint.setUnderlineText(underline);
//
//		paint.setTextSize(size);                                             // at first, just set the size (no matter what is put here)
//		paint.setTextSize(size * size / (paint.descent() - paint.ascent())); // and now we set the size equal to the given one (in pixels)
	}

	// Font for keyboard
	public Font() {
//		AssetManager manager = ContextHolder.getAppContext().getAssets();
//		Typeface typeface = Typeface.createFromAsset(manager, "Roboto-Regular.ttf");
//		paint.setTypeface(typeface);
//		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, SIZE_KEYBOARD,
//				ContextHolder.getAppContext().getResources().getDisplayMetrics());
//		paint.setTextSize(size);
		paint = new BitmapFont();
		paint.getData().scale(SIZE_KEYBOARD/14);
	}

	public static Font getFont(int fontSpecifier) {
		return getDefaultFont();
	}

	public static Font getFont(int face, int style, int size) {

			BitmapFont typeface;
			boolean underline;

			switch (face) {
				case FACE_MONOSPACE:
					typeface = JaMGDx.skin.getFont("FACE_MONOSPACE");
					break;

				case FACE_PROPORTIONAL:
					typeface = JaMGDx.skin.getFont("FACE_PROPORTIONAL");
					break;

				default:
				case FACE_SYSTEM:
					typeface = JaMGDx.skin.getFont("default-font");
					break;
			}

			if ((style & STYLE_BOLD) != 0) {

			}

			if ((style & STYLE_ITALIC) != 0) {

			}

			underline = (style & STYLE_UNDERLINED) != 0;
			float fsize;
			switch (size) {
				case SIZE_SMALL:
					fsize = sizes[0];
					break;

				default:
				case SIZE_MEDIUM:
					fsize = sizes[1];
					break;

				case SIZE_LARGE:
					fsize = sizes[2];
					break;
			}

			Font font = new Font(typeface, face, style, size, underline);
			font.setTextSize(fsize);

		return font;
	}

	public static Font getDefaultFont() {
		return getFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
	}

	public float getTextSize() {
		return paint.getData().xHeight;
	}
	public void setTextSize(float size) {
		paint.getData().scale(size/paint.getXHeight());
	}

	public int getFace() {
		return face;
	}

	public int getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}

	public int getHeight() {
		return (int)paint.getXHeight();
	}

	public int getBaselinePosition() {
		return (int)paint.getData().padBottom;
	}

	public int charWidth(char c) {
		return paint.getData().getGlyph(c).width;
	}

	public int charsWidth(char[] ch, int offset, int length) {
		int charsWidth = 0;
		for(int i = offset;i < length; i++)
			charsWidth += charWidth(ch[i]);
		return charsWidth;
	}

	public int stringWidth(String text) {
		return charsWidth(text.toCharArray(),0,text.length());
	}

	public int substringWidth(String str, int i, int i2) {
		return stringWidth(str.substring(i,i2));
	}

	public static int getFontStyle(int index) {
		return index & 7;
	}

	public boolean isBold() {
		return style == STYLE_BOLD;
	}

	public boolean isPlain() {
		return style == STYLE_PLAIN;
	}

	public boolean isUnderlined() {
		return style == STYLE_UNDERLINED;
	}

	public boolean isItalic() {
		return style == STYLE_ITALIC;
	}
}
