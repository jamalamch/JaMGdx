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



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;

import javax.microedition.lcdui.event.CanvasEvent;
import javax.microedition.lcdui.event.Event;
import javax.microedition.lcdui.event.EventFilter;
import javax.microedition.lcdui.event.EventQueue;
import javax.microedition.lcdui.overlay.FpsCounter;
import javax.microedition.lcdui.overlay.Overlay;
import javax.microedition.lcdui.pointer.FixedKeyboard;
import javax.microedition.util.ContextHolder;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Canvas extends Displayable {
	public static final int KEY_POUND = 35;
	public static final int KEY_STAR = 42;
	public static final int KEY_NUM0 = 48;
	public static final int KEY_NUM1 = 49;
	public static final int KEY_NUM2 = 50;
	public static final int KEY_NUM3 = 51;
	public static final int KEY_NUM4 = 52;
	public static final int KEY_NUM5 = 53;
	public static final int KEY_NUM6 = 54;
	public static final int KEY_NUM7 = 55;
	public static final int KEY_NUM8 = 56;
	public static final int KEY_NUM9 = 57;

	public static final int KEY_UP = -1;
	public static final int KEY_DOWN = -2;
	public static final int KEY_LEFT = -3;
	public static final int KEY_RIGHT = -4;
	public static final int KEY_FIRE = -5;
	public static final int KEY_SOFT_LEFT = -6;
	public static final int KEY_SOFT_RIGHT = -7;
	public static final int KEY_CLEAR = -8;
	public static final int KEY_SEND = -10;
	public static final int KEY_END = -11;

	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 5;
	public static final int DOWN = 6;
	public static final int FIRE = 8;
	public static final int GAME_A = 9;
	public static final int GAME_B = 10;
	public static final int GAME_C = 11;
	public static final int GAME_D = 12;

	private static final int DEFAULT_LAYOUT = 0;
	private static final int SIEMENS_LAYOUT = 1;
	private static final int MOTOROLA_LAYOUT = 2;

	private static final int SIEMENS_KEY_UP = -59;
	private static final int SIEMENS_KEY_DOWN = -60;
	private static final int SIEMENS_KEY_LEFT = -61;
	private static final int SIEMENS_KEY_RIGHT = -62;
	private static final int SIEMENS_KEY_SOFT_LEFT = -1;
	private static final int SIEMENS_KEY_SOFT_RIGHT = -4;

	private static final int MOTOROLA_KEY_UP = -1;
	private static final int MOTOROLA_KEY_DOWN = -6;
	private static final int MOTOROLA_KEY_LEFT = -2;
	private static final int MOTOROLA_KEY_RIGHT = -5;
	private static final int MOTOROLA_KEY_FIRE = -20;
	private static final int MOTOROLA_KEY_SOFT_LEFT = -21;
	private static final int MOTOROLA_KEY_SOFT_RIGHT = -22;

	private static IntIntMap keyCodeToSiemensCode = new IntIntMap();
	private static IntIntMap keyCodeToMotorolaCode = new IntIntMap();
	private static IntIntMap androidToMIDP;
	private static IntIntMap keyCodeToGameAction = new IntIntMap();
	private static IntIntMap gameActionToKeyCode = new IntIntMap();
	private static IntMap keyCodeToKeyName = new IntMap<String>();

	static {
		mapKeyCode(KEY_NUM0, 0, "0");
		mapKeyCode(KEY_NUM1, 0, "1");
		mapKeyCode(KEY_NUM2, UP, "2");
		mapKeyCode(KEY_NUM3, 0, "3");
		mapKeyCode(KEY_NUM4, LEFT, "4");
		mapKeyCode(KEY_NUM5, FIRE, "5");
		mapKeyCode(KEY_NUM6, RIGHT, "6");
		mapKeyCode(KEY_NUM7, GAME_A, "7");
		mapKeyCode(KEY_NUM8, DOWN, "8");
		mapKeyCode(KEY_NUM9, GAME_B, "9");
		mapKeyCode(KEY_STAR, GAME_C, "ASTERISK");
		mapKeyCode(KEY_POUND, GAME_D, "POUND");
		mapKeyCode(KEY_UP, UP, "UP");
		mapKeyCode(KEY_DOWN, DOWN, "DOWN");
		mapKeyCode(KEY_LEFT, LEFT, "LEFT");
		mapKeyCode(KEY_RIGHT, RIGHT, "RIGHT");
		mapKeyCode(KEY_FIRE, FIRE, "SELECT");
		mapKeyCode(KEY_SOFT_LEFT, 0, "SOFT1");
		mapKeyCode(KEY_SOFT_RIGHT, 0, "SOFT2");
		mapKeyCode(KEY_CLEAR, 0, "CLEAR");
		mapKeyCode(KEY_SEND, 0, "SEND");
		mapKeyCode(KEY_END, 0, "END");

		mapGameAction(UP, KEY_UP);
		mapGameAction(LEFT, KEY_LEFT);
		mapGameAction(RIGHT, KEY_RIGHT);
		mapGameAction(DOWN, KEY_DOWN);
		mapGameAction(FIRE, KEY_FIRE);
		mapGameAction(GAME_A, KEY_NUM7);
		mapGameAction(GAME_B, KEY_NUM9);
		mapGameAction(GAME_C, KEY_STAR);
		mapGameAction(GAME_D, KEY_POUND);
	}



	private static void remapKeys() {
		if (layoutType == SIEMENS_LAYOUT) {
			keyCodeToSiemensCode.put(KEY_LEFT, SIEMENS_KEY_LEFT);
			keyCodeToSiemensCode.put(KEY_RIGHT, SIEMENS_KEY_RIGHT);
			keyCodeToSiemensCode.put(KEY_UP, SIEMENS_KEY_UP);
			keyCodeToSiemensCode.put(KEY_DOWN, SIEMENS_KEY_DOWN);
			keyCodeToSiemensCode.put(KEY_SOFT_LEFT, SIEMENS_KEY_SOFT_LEFT);
			keyCodeToSiemensCode.put(KEY_SOFT_RIGHT, SIEMENS_KEY_SOFT_RIGHT);

			mapGameAction(LEFT, SIEMENS_KEY_LEFT);
			mapGameAction(RIGHT, SIEMENS_KEY_RIGHT);
			mapGameAction(UP, SIEMENS_KEY_UP);
			mapGameAction(DOWN, SIEMENS_KEY_DOWN);

			mapKeyCode(SIEMENS_KEY_UP, UP, "UP");
			mapKeyCode(SIEMENS_KEY_DOWN, DOWN, "DOWN");
			mapKeyCode(SIEMENS_KEY_LEFT, LEFT, "LEFT");
			mapKeyCode(SIEMENS_KEY_RIGHT, RIGHT, "RIGHT");
			mapKeyCode(SIEMENS_KEY_SOFT_LEFT, 0, "SOFT1");
			mapKeyCode(SIEMENS_KEY_SOFT_RIGHT, 0, "SOFT2");
		} else if (layoutType == MOTOROLA_LAYOUT) {
			keyCodeToMotorolaCode.put(KEY_UP, MOTOROLA_KEY_UP);
			keyCodeToMotorolaCode.put(KEY_DOWN, MOTOROLA_KEY_DOWN);
			keyCodeToMotorolaCode.put(KEY_LEFT, MOTOROLA_KEY_LEFT);
			keyCodeToMotorolaCode.put(KEY_RIGHT, MOTOROLA_KEY_RIGHT);
			keyCodeToMotorolaCode.put(KEY_FIRE, MOTOROLA_KEY_FIRE);
			keyCodeToMotorolaCode.put(KEY_SOFT_LEFT, MOTOROLA_KEY_SOFT_LEFT);
			keyCodeToMotorolaCode.put(KEY_SOFT_RIGHT, MOTOROLA_KEY_SOFT_RIGHT);

			mapGameAction(LEFT, MOTOROLA_KEY_LEFT);
			mapGameAction(RIGHT, MOTOROLA_KEY_RIGHT);
			mapGameAction(UP, MOTOROLA_KEY_UP);
			mapGameAction(DOWN, MOTOROLA_KEY_DOWN);
			mapGameAction(FIRE, MOTOROLA_KEY_FIRE);

			mapKeyCode(MOTOROLA_KEY_UP, UP, "UP");
			mapKeyCode(MOTOROLA_KEY_DOWN, DOWN, "DOWN");
			mapKeyCode(MOTOROLA_KEY_LEFT, LEFT, "LEFT");
			mapKeyCode(MOTOROLA_KEY_RIGHT, RIGHT, "RIGHT");
			mapKeyCode(MOTOROLA_KEY_FIRE, FIRE, "SELECT");
			mapKeyCode(MOTOROLA_KEY_SOFT_LEFT, 0, "SOFT1");
			mapKeyCode(MOTOROLA_KEY_SOFT_RIGHT, 0, "SOFT2");
		}
	}

	private static void mapKeyCode(int midpKeyCode, int gameAction, String keyName) {
		keyCodeToGameAction.put(midpKeyCode, gameAction);
		keyCodeToKeyName.put(midpKeyCode, keyName);
	}

	private static void mapGameAction(int gameAction, int keyCode) {
		gameActionToKeyCode.put(gameAction, keyCode);
	}

	private static int convertAndroidKeyCode(int keyCode) {
		return androidToMIDP.get(keyCode, Integer.MAX_VALUE);
	}

	public static int convertKeyCode(int keyCode) {
		int result;
		if (layoutType == SIEMENS_LAYOUT) {
			result = keyCodeToSiemensCode.get(keyCode, keyCode);
		} else if (layoutType == MOTOROLA_LAYOUT) {
			result = keyCodeToMotorolaCode.get(keyCode, keyCode);
		} else {
			result = keyCode;
		}
		return result;
	}

	public int getKeyCode(int gameAction) {
		int res = gameActionToKeyCode.get(gameAction, Integer.MAX_VALUE);
		if (res != Integer.MAX_VALUE) {
			return res;
		} else {
			throw new IllegalArgumentException("unknown game action " + gameAction);
		}
	}

	public int getGameAction(int keyCode) {
		int res = keyCodeToGameAction.get(keyCode, Integer.MAX_VALUE);
		if (res != Integer.MAX_VALUE) {
			return res;
		} else {
			throw new IllegalArgumentException("unknown keycode " + keyCode);
		}
	}

	public String getKeyName(int keyCode) {
		String res = (String) keyCodeToKeyName.get(keyCode);
		if (res != null) {
			return res;
		} else {
			throw new IllegalArgumentException("unknown keycode " + keyCode);
		}
	}

	public void postKeyPressed(int keyCode) {
		Display.postEvent(CanvasEvent.getInstance(this, CanvasEvent.KEY_PRESSED, convertKeyCode(keyCode)));
	}

	public void postKeyReleased(int keyCode) {
		Display.postEvent(CanvasEvent.getInstance(this, CanvasEvent.KEY_RELEASED, convertKeyCode(keyCode)));
	}

	public void postKeyRepeated(int keyCode) {
		Display.postEvent(CanvasEvent.getInstance(this, CanvasEvent.KEY_REPEATED, convertKeyCode(keyCode)));
	}

//	private class InnerView extends SurfaceView implements SurfaceHolder.Callback {
//
//		OverlayView overlayView;
//		private FrameLayout rootView;
//		private Graphics viewGraphics;
//
//		public InnerView(Context context) {
//			super(context);
//			rootView = ((Activity) context).findViewById(R.id.midletFrame);
//			overlayView = rootView.findViewById(R.id.vOverlay);
//			getHolder().addCallback(this);
//			getHolder().setFormat(PixelFormat.RGBA_8888);
//			setFocusableInTouchMode(true);
//			if (hwaOldEnabled) {
//				setWillNotDraw(false);
//				viewGraphics = new Graphics();
//				viewGraphics.setFont(new Font());
//			}
//		}
//
//		@Override
//		protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//			super.onVisibilityChanged(changedView, visibility);
//			// Fix keyboard issue on Blackberry
//			if (visibility == VISIBLE) {
//				requestFocus();
//			}
//		}
//
//		@Override
//		public boolean onKeyDown(int keyCode, KeyEvent event) {
//			keyCode = convertAndroidKeyCode(keyCode);
//			if (keyCode == Integer.MAX_VALUE) {
//				return false;
//			}
//			if (event.getRepeatCount() == 0) {
//				if (overlay == null || !overlay.keyPressed(keyCode)) {
//					postKeyPressed(keyCode);
//				}
//			} else {
//				if (overlay == null || !overlay.keyRepeated(keyCode)) {
//					postKeyRepeated(keyCode);
//				}
//			}
//			return true;
//		}
//
//		@Override
//		public boolean onKeyUp(int keyCode, KeyEvent event) {
//			keyCode = convertAndroidKeyCode(keyCode);
//			if (keyCode == Integer.MAX_VALUE) {
//				return false;
//			}
//			if (overlay == null || !overlay.keyReleased(keyCode)) {
//				postKeyReleased(keyCode);
//			}
//			return true;
//		}
//
//		public boolean onTouchEvent(MotionEvent event) {
//			switch (event.getActionMasked()) {
//				case MotionEvent.ACTION_DOWN:
//					if (overlay != null) {
//						overlay.show();
//					}
//				case MotionEvent.ACTION_POINTER_DOWN:
//					int index = event.getActionIndex();
//					int id = event.getPointerId(index);
//					if ((overlay == null || !overlay.pointerPressed(id, event.getX(index), event.getY(index)))
//							&& touchInput && id == 0) {
//						Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.POINTER_PRESSED, id,
//								convertPointerX(event.getX()), convertPointerY(event.getY())));
//					}
//					break;
//				case MotionEvent.ACTION_MOVE:
//					int pointerCount = event.getPointerCount();
//					int historySize = event.getHistorySize();
//					for (int h = 0; h < historySize; h++) {
//						for (int p = 0; p < pointerCount; p++) {
//							id = event.getPointerId(p);
//							if ((overlay == null || !overlay.pointerDragged(id, event.getHistoricalX(p, h), event.getHistoricalY(p, h)))
//									&& touchInput && id == 0) {
//								Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.POINTER_DRAGGED, id,
//										convertPointerX(event.getHistoricalX(p, h)), convertPointerY(event.getHistoricalY(p, h))));
//							}
//						}
//					}
//					for (int p = 0; p < pointerCount; p++) {
//						id = event.getPointerId(p);
//						if ((overlay == null || !overlay.pointerDragged(id, event.getX(p), event.getY(p)))
//								&& touchInput && id == 0) {
//							Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.POINTER_DRAGGED, id,
//									convertPointerX(event.getX(p)), convertPointerY(event.getY(p))));
//						}
//					}
//					break;
//				case MotionEvent.ACTION_UP:
//					if (overlay != null) {
//						overlay.hide();
//					}
//				case MotionEvent.ACTION_POINTER_UP:
//					index = event.getActionIndex();
//					id = event.getPointerId(index);
//					if ((overlay == null || !overlay.pointerReleased(id, event.getX(index), event.getY(index)))
//							&& touchInput && id == 0) {
//						Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.POINTER_RELEASED, id,
//								convertPointerX(event.getX()), convertPointerY(event.getY())));
//					}
//					break;
//				default:
//					return super.onTouchEvent(event);
//			}
//			return true;
//		}
//
//		@Override
//		public void surfaceChanged(SurfaceHolder holder, int format, int newWidth, int newHeight) {
//			Rect offsetViewBounds = new Rect(0, 0, newWidth, newHeight);
//			// calculates the relative coordinates to the parent
//			rootView.offsetDescendantRectToMyCoords(this, offsetViewBounds);
//			synchronized (paintSync) {
//				overlayView.setTargetBounds(offsetViewBounds);
//				displayWidth = newWidth;
//				displayHeight = newHeight;
//				if (checkSizeChanged() || !sizeChangedCalled) {
//					Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.SIZE_CHANGED,
//							width, height));
//					sizeChangedCalled = true;
//				}
//			}
//			Display.postEvent(paintEvent);
//		}
//
//		@Override
//		public void surfaceCreated(SurfaceHolder holder) {
//			synchronized (paintSync) {
//				surface = holder.getSurface();
//				Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.SHOW_NOTIFY));
//			}
//			if (showFps) {
//				fpsCounter = new FpsCounter(overlayView);
//				overlayView.addLayer(fpsCounter);
//			}
//			overlayView.setVisibility(true);
//		}
//
//		@Override
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			synchronized (paintSync) {
//				surface = null;
//				Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.HIDE_NOTIFY));
//				if (fpsCounter != null) {
//					fpsCounter.stop();
//					overlayView.removeLayer(fpsCounter);
//					fpsCounter = null;
//				}
//			}
//			overlayView.setVisibility(false);
//		}
//
//		@Override
//		protected void onDraw(android.graphics.Canvas canvas) {
//			if (!hwaOldEnabled) return; // Fix for Android Pie
//			Graphics g = viewGraphics;
//			g.setSurfaceCanvas(canvas);
//			g.clear(backgroundColor);
//			g.drawImage(offscreenCopy, onX, onY, onWidth, onHeight, filter, 255);
//			if (fpsCounter != null) {
//				fpsCounter.increment();
//			}
//		}
//	}

	private class PaintEvent extends Event implements EventFilter {

		private Graphics mGraphics = new Graphics();

		@Override
		public void process() {
				if (!isShown()) {
					return;
				}
				Graphics g = this.mGraphics;
				//g.setCanvas(offscreen.getCanvas(), offscreen.getBitmap());
				g.reset();
				try {
					paint(g);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				offscreen.copyPixels(offscreenCopy);
				if (!parallelRedraw) {
					repaintScreen();
				}
		}

		@Override
		public void recycle() {
		}

		private int enqueued = 0;

		@Override
		public void enterQueue() {
			enqueued++;
		}

		@Override
		public void leaveQueue() {
			enqueued--;
		}

		/**
		 * The queue should contain no more than two repaint events
		 * <p>
		 * One won't be smooth enough, and if you add more than two,
		 * then how to determine exactly how many of them need to be added?
		 */
		@Override
		public boolean placeableAfter(Event event) {
			return event != this;
		}

		@Override
		public boolean accept(Event event) {
			return event == this;
		}
	}

	private static final float FULLSCREEN_HEIGHT_RATIO = 0.85f;
	private static final String TAG = Canvas.class.getName();
	private final Object paintSync = new Object();

	private PaintEvent paintEvent = new PaintEvent();

//	private InnerView innerView;
	private Graphics graphics = new Graphics();

	private float maxHeight;

	private int displayWidth;
	private int displayHeight;
	private boolean fullscreen;
	private boolean sizeChangedCalled;

	private static boolean scaleToFit;
	private static boolean keepAspectRatio;
	private static boolean filter;
	private static boolean touchInput;
	private static boolean hwaEnabled;
	private static boolean hwaOldEnabled;
	private static boolean parallelRedraw;
	private static boolean forceFullscreen;
	private static boolean showFps;
	private static int backgroundColor;
	private static int scaleRatio;
	private static int fpsLimit;
	private static int layoutType;

	private Image offscreen;
	private Image offscreenCopy;
	private int onX, onY, onWidth, onHeight;
	private long lastFrameTime = System.currentTimeMillis();

	private Overlay overlay;
	private FpsCounter fpsCounter;

	public Canvas() {
		displayWidth = ContextHolder.getDisplayWidth();
		displayHeight = ContextHolder.getDisplayHeight();
		Gdx.app.log("Canvas", "Constructor. w=" + displayWidth + " h=" + displayHeight);
		if (forceFullscreen) {
			setFullScreenMode(true);
		} else {
			updateSize();
		}
	}

	public static void setScale(boolean scaleToFit, boolean keepAspectRatio, int scaleRatio) {
		Canvas.scaleToFit = scaleToFit;
		Canvas.keepAspectRatio = keepAspectRatio;
		Canvas.scaleRatio = scaleRatio;
	}

	public static void setBackgroundColor(int color) {
		backgroundColor = color | 0xFF000000;
	}

	public static void setFilterBitmap(boolean filter) {
		Canvas.filter = filter;
	}

	public static void setKeyMapping(int layoutType, IntIntMap intArray) {
		Canvas.layoutType = layoutType;
		Canvas.androidToMIDP = intArray;
		remapKeys();
	}

	public static void setHasTouchInput(boolean touchInput) {
		Canvas.touchInput = touchInput;
	}

	public static void setHardwareAcceleration(boolean hardwareAcceleration, boolean parallel) {
		Canvas.hwaEnabled = hardwareAcceleration;
		Canvas.hwaOldEnabled = hardwareAcceleration;
		Canvas.parallelRedraw = parallel;
	}

	public static void setForceFullscreen(boolean forceFullscreen) {
		Canvas.forceFullscreen = forceFullscreen;
	}

	public static void setShowFps(boolean showFps) {
		Canvas.showFps = showFps;
	}

	public static void setLimitFps(boolean limitFps, int fpsLimit) {
		Canvas.fpsLimit = limitFps ? fpsLimit : 0;
	}

	public void setOverlay(Overlay ov) {
		if (overlay != null) {
			overlay.setTarget(null);
		}
		if (ov != null) {
			ov.setTarget(this);
		}
		overlay = ov;
	}

	public Image getOffscreenCopy() {
		Image image = Image.createImage(onWidth, onHeight);
		Graphics g = image.getGraphics();
		g.drawImage(offscreenCopy, 0, 0, onWidth, onHeight, filter, 255);
		return image;
	}

	private boolean checkSizeChanged() {
		float tmpWidth = getWidth();
		float tmpHeight = getHeight();
		updateSize();
		return getWidth() != tmpWidth || getHeight() != tmpHeight;
	}

	/**
	 * Update the size and position of the virtual screen relative to the real one.
	 */
	private void updateSize() {
		/*
		 * We turn the sizes of the virtual screen into the sizes of the visible canvas.
		 *
		 * At the same time, we take into account that one or both virtual sizes can be less
		 * than zero, which means auto-selection of this size so that the resulting canvas
		 * has the same aspect ratio as the actual screen of the device.
		 */
		int scaledDisplayHeight;
		/*
		 * If FixedKeyboard is active, then scale down the virtual screen
		 */
		if (ContextHolder.getVk() instanceof FixedKeyboard) {
			scaledDisplayHeight = (int) (displayHeight - FixedKeyboard.KEY_ROW_COUNT * (displayWidth /
					(FixedKeyboard.KEY_WIDTH_RATIO * FixedKeyboard.KEY_HEIGHT_RATIO)) - 1);
		} else {
			scaledDisplayHeight = displayHeight;
		}
		if (virtualWidth < 0) {
			if (virtualHeight < 0) {
				/*
				 * nothing is set - screen-sized canvas
				 */
				setWidth(displayWidth);
				setHeight(scaledDisplayHeight);
			} else {
				/*
				 * only the canvas height is set
				 * width is selected by the ratio of the real screen
				 */
				setWidth(displayWidth * virtualHeight / scaledDisplayHeight);
				setHeight( virtualHeight);
			}
		} else if (virtualHeight < 0) {
			/*
			 * only the canvas width is set
			 * height is selected by the ratio of the real screen
			 */
			setWidth(virtualWidth);
			setHeight( scaledDisplayHeight * virtualWidth / displayWidth);
		} else {
			/*
			 * the width and height of the canvas are strictly set
			 */
			setWidth( virtualWidth);
			setHeight(virtualHeight);
		}

		/*
		 * calculate the maximum height
		 */
		maxHeight = getHeight();
		/*
		 * calculate the current height
		 */
		if (!fullscreen) {
			setHeight(getWidth() * FULLSCREEN_HEIGHT_RATIO);
		}

		/*
		 * We turn the size of the canvas into the size of the image
		 * that will be displayed on the screen of the device.
		 */
		if (scaleToFit) {
			if (keepAspectRatio) {
				/*
				 * try to fit in width
				 */
				onWidth = displayWidth;
				onHeight = (int) (getHeight() * displayWidth / getWidth());
				if (onHeight > scaledDisplayHeight) {
					/*
					 * if height is too big,
					 * then fit in height
					 */
					onHeight = scaledDisplayHeight;
					onWidth = (int) (getWidth() * scaledDisplayHeight / getHeight());
				}
			} else {
				/*
				 * scaling without preserving the aspect ratio:
				 * just stretch the picture to full screen
				 */
				onWidth = displayWidth;
				onHeight = scaledDisplayHeight;
			}
		} else {
			/*
			 * without scaling
			 */
			onWidth = (int) getWidth();
			onHeight = (int) getHeight();
		}

		onWidth = onWidth * scaleRatio / 100;
		onHeight = onHeight * scaleRatio / 100;

		if (displayWidth >= displayHeight) {
			/*
			 * If we hold the screen horizontally, then most likely we hold it by the left and right edges.
			 * Place the midlet screen in the center.
			 */
			onX = (displayWidth - onWidth) / 2;
			onY = (displayHeight - onHeight) / 2;
		} else {
			/*
			 * If we hold the screen vertically, then most likely we hold it by the bottom edge.
			 * Shift the midlet screen to the top.
			 */
			onX = (displayWidth - onWidth) / 2;
			onY = 0;
		}

		Rectangle screen = new Rectangle(0, 0, displayWidth, displayHeight);
		Rectangle virtualScreen = new Rectangle(onX, onY,   onWidth,  onHeight);

		if (offscreen == null || offscreen.getWidth() != getWidth() || offscreen.getHeight() != getHeight()) {
			offscreen = Image.createImage((int)getWidth(), (int)getHeight(), offscreen);
			offscreenCopy = Image.createImage((int)getWidth(), (int)getHeight(), offscreenCopy);
		}
		if (overlay != null) {
			overlay.resize(screen, virtualScreen);
		}
	}

	/**
	 * Convert the screen coordinates of the pointer into the virtual ones.
	 *
	 * @param x the pointer coordinate on the real screen
	 * @return the corresponding pointer coordinate on the virtual screen
	 */
	private float convertPointerX(float x) {
		return (x - onX) * getWidth() / onWidth;
	}

	/**
	 * Convert the screen coordinates of the pointer into the virtual ones.
	 *
	 * @param y the pointer coordinate on the real screen
	 * @return the corresponding pointer coordinate on the virtual screen
	 */
	private float convertPointerY(float y) {
		return (y - onY) * getHeight() / onHeight;
	}

	@Override
	public Stage getDisplayableView() {
		return super.getDisplayableView();
	}

	@Override
	public void clearDisplayableView() {
			super.clearDisplayableView();
//			innerView = null;
	}

	public void setFullScreenMode(boolean flag) {
			if (fullscreen != flag) {
				fullscreen = flag;
				updateSize();
				Display.postEvent(CanvasEvent.getInstance(Canvas.this, CanvasEvent.SIZE_CHANGED,
						(int)getWidth(), (int)getHeight()));
			}
	}

	public boolean hasPointerEvents() {
		return touchInput;
	}

	public boolean hasPointerMotionEvents() {
		return touchInput;
	}

	public boolean hasRepeatEvents() {
		return true;
	}

	public boolean isDoubleBuffered() {
		return true;
	}


	public abstract void paint(Graphics g);

	public final void repaint() {
		repaint(0, 0, (int)getWidth(),(int)getHeight());
	}

	public final void repaint(int x, int y, int width, int height) {
		limitFps();
		Display.postEvent(paintEvent);
	}

	// GameCanvas
	public void flushBuffer(Image image) {
		limitFps();
			image.copyPixels(offscreenCopy);
			if (!parallelRedraw) {
				repaintScreen();
			}
	}

	private void limitFps() {
		if (fpsLimit == 0) return;
		try {
			long millis = (1000 / fpsLimit) - (System.currentTimeMillis() - lastFrameTime);
			if (millis > 0) Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lastFrameTime = System.currentTimeMillis();
	}

	private boolean repaintScreen() {
		try {
			Graphics g = this.graphics;
			g.clear(backgroundColor);
			g.drawImage(offscreenCopy, onX, onY, onWidth, onHeight, filter, 255);
			if (fpsCounter != null) {
				fpsCounter.increment();
			}
		} catch (Exception e) {
			Gdx.app.log(TAG, "repaintScreen: " + e);
		}
		return true;
	}

	/**
	 * After calling this method, an immediate redraw is guaranteed to occur,
	 * and the calling thread is blocked until it is completed.
	 */
	public final void serviceRepaints() {
		EventQueue queue = Display.getEventQueue();

		/*
		 * blocking order:
		 *
		 * 1 - queue.this
		 * 2 - queue.queue
		 *
		 * accordingly, inside the EventQueue, the order must be the same,
		 * otherwise mutual blocking of two threads is possible (everything will hang)
		 */

		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized (queue) {
			/*
			 * This synchronization actually stops the events processing
			 * just before changing the value of currentEvent()
			 *
			 * Then there are only two options:
			 */

			if (queue.currentEvent() == paintEvent) {
				/*
				 * if repaint() is being processed there now,
				 * then you just need to wait for it to finish
				 */

				if (Thread.holdsLock(paintSync)) { // Avoid deadlock
					return;
				}

				try {
					queue.wait();
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			} else if (queue.removeEvents(paintEvent)) {
				/*
				 * if now something else is being processed there (not repaint),
				 * but the repaint was in the queue (and was removed from there),
				 * then it needs to be synchronously called from here
				 */

				paintEvent.run();
			}
		}
	}

	public void showNotify() {
	}

	public void hideNotify() {
	}

	public void keyPressed(int keyCode) {
	}

	public void keyRepeated(int keyCode) {
	}

	public void keyReleased(int keyCode) {
	}

	public void pointerPressed(int pointer, float x, float y) {
		if (pointer == 0) {
			pointerPressed(Math.round(x), Math.round(y));
		}
	}

	public void pointerDragged(int pointer, float x, float y) {
		if (pointer == 0) {
			pointerDragged(Math.round(x), Math.round(y));
		}
	}

	public void pointerReleased(int pointer, float x, float y) {
		if (pointer == 0) {
			pointerReleased(Math.round(x), Math.round(y));
		}
	}

	public void pointerPressed(int x, int y) {
	}

	public void pointerDragged(int x, int y) {
	}

	public void pointerReleased(int x, int y) {
	}
	public Rectangle getRectangle(){
		return new Rectangle(getRoot().getX(),getRoot().getY(),getWidth(),getHeight());
	}
}
