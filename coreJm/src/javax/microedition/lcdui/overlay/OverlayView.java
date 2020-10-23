/*
 * Copyright 2019 Kharchenko Yury
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
package javax.microedition.lcdui.overlay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.siemens.mp.lcdui.Canvas;


import java.util.ArrayList;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class OverlayView extends Actor {

	private final Graphics graphics;
	private final Rectangle surfaceRect = new Rectangle();
	private ArrayList<Layer> layers = new ArrayList<>();

	public OverlayView() {
		super();
		graphics = new Graphics();
		graphics.setFont(new Font());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		for (Layer layer : layers) {
			layer.paint(graphics);
		}
	}

	protected void onDraw(Canvas canvas) {
		float x = canvas.getX();
		float y = canvas.getX();
		canvas.setPosition(surfaceRect.x,surfaceRect.y);
		graphics.setSurfaceCanvas(canvas);
		for (Layer layer : layers) {
			layer.paint(graphics);
		}
		canvas.setPosition(x,y);
	}

	public void setTargetBounds(Rectangle bounds) {
		surfaceRect.set(bounds);
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
	}

	public void removeLayer(Layer layer) {
		layers.remove(layer);
	}

	public void setVisibility(boolean visibility) {
		setVisible(visibility && !layers.isEmpty());
	}
}
