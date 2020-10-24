/*
 * Copyright 2012 Kulikov Dmitriy
 * Copyright 2018 Nikita Shakarun
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

package javax.microedition.lcdui.list;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.microedition.lcdui.Image;

public class CompoundItem {
	private String stringPart;
	private Image imagePart;
	private Drawable imageDrawable;

	public CompoundItem(String stringPart, Image imagePart) {
		this.stringPart = stringPart;
		this.imagePart = imagePart;
	}

	public String getString() {
		return stringPart;
	}

	public Drawable getDrawable(int height) {
		if (imageDrawable == null && imagePart != null) {
			Pixmap bitmap = imagePart.getBitmap();
			int width = height / bitmap.getHeight() * bitmap.getWidth();
			Pixmap scaledBitmap =  new Pixmap(width, height ,Pixmap.Format.RGB888);
			scaledBitmap.drawPixmap(scaledBitmap,0,0,0,0,width,height);
			imageDrawable = new TextureRegionDrawable( new Texture(scaledBitmap));
		}
		return imageDrawable;
	}
