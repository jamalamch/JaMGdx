/*
 * Copyright 2015-2016 Nickolay Savchenko
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

package javax.microedition.shell;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.ViewHandler;
import javax.microedition.lcdui.event.SimpleEvent;
import javax.microedition.lcdui.overlay.OverlayView;
import javax.microedition.lcdui.pointer.FixedKeyboard;
import javax.microedition.lcdui.pointer.VirtualKeyboard;
import javax.microedition.util.ContextHolder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Align;
import ja.mgdx.Assets;
import ja.mgdx.JaMGDx;
import ru.playsoftware.j2meloader.config.ConfigActivity;
import ru.playsoftware.j2meloader.util.LogUtils;

public class MicroActivity extends ApplicationAdapter {
	private static final int ORIENTATION_DEFAULT = 0;
	private static final int ORIENTATION_AUTO = 1;
	private static final int ORIENTATION_PORTRAIT = 2;
	private static final int ORIENTATION_LANDSCAPE = 3;

	private Displayable current;
	private boolean visible;
	private boolean actionBarEnabled;
	private boolean statusBarEnabled;
	private boolean keyLongPressed;
	private FrameLayout layout;
	private Toolbar toolbar;
	private MicroLoader microLoader;

	@Override
	public void create() {
		Preferences sp = Gdx.app.getPreferences(getApplicationContext());
		//SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setTheme(sp.getString("pref_theme", "light"));
		ContextHolder.setCurrentActivity(this);
		setContentView(R.layout.activity_micro);
		OverlayView overlayView = findViewById(R.id.vOverlay);
		layout = findViewById(R.id.displayable_container);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBarEnabled = sp.getBoolean("pref_actionbar_switch", false);
		statusBarEnabled = sp.getBoolean("pref_statusbar_switch", false);
		boolean wakelockEnabled = sp.getBoolean("pref_wakelock_switch", false);
		if (wakelockEnabled) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		String appName = JaMGDx.getStringExtra(ConfigActivity.MIDLET_NAME_KEY);
		microLoader = new MicroLoader(this, appName);
		microLoader.init();
		microLoader.applyConfiguration();
		VirtualKeyboard vk = ContextHolder.getVk();
		if (vk != null) {
			vk.setView(overlayView);
			overlayView.addLayer(vk);
		}
		if (vk instanceof FixedKeyboard) {
			setOrientation(ORIENTATION_PORTRAIT);
		} else {
			int orientation = microLoader.getOrientation();
			setOrientation(orientation);
		}
		try {
			loadMIDlet();
		} catch (Exception e) {
			e.printStackTrace();
			showErrorDialog(e.toString());
		}
	}

	@Override
	public void resume() {
		super.resume();
		visible = true;
		MidletThread.resumeApp();
	}

	@Override
	public void pause() {
		visible = false;
		MidletThread.pauseApp();
		super.pause();
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus  && current instanceof Canvas) {
			hideSystemUI();
		}
	}

	private String getApplicationContext(){
		return "appname";
	}

	private void setOrientation(int orientation) {
		switch (orientation) {
			case ORIENTATION_AUTO:
				setRequestedOrientation(JaMGDx.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				break;
			case ORIENTATION_PORTRAIT:
				setRequestedOrientation(JaMGDx.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				break;
			case ORIENTATION_LANDSCAPE:
				setRequestedOrientation(JaMGDx.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				break;
			case ORIENTATION_DEFAULT:
			default:
				break;
		}
	}

	private void setRequestedOrientation(int id){

	}

	private void setTheme(String theme) {

	}

	private void loadMIDlet() throws Exception {
		LinkedHashMap<String, String> midlets = microLoader.loadMIDletList();
		int size = midlets.size();
		String[] midletsNameArray = midlets.values().toArray(new String[0]);
		String[] midletsClassArray = midlets.keySet().toArray(new String[0]);
		if (size == 0) {
			throw new Exception("No MIDlets found");
		} else if (size == 1) {
			MidletThread.create(microLoader, midletsClassArray[0]);
		} else {
			showMidletDialog(midletsNameArray, midletsClassArray);
		}
	}

	private void showMidletDialog(String[] midletsNameArray, final String[] midletsClassArray) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this)
//				.setTitle(R.string.select_dialog_title)
//				.setItems(midletsNameArray, (d, n) -> MidletThread.create(microLoader, midletsClassArray[n]))
//				.setOnCancelListener(dialogInterface -> finish());
//		builder.show();
		Dialog builderf = new Dialog(Assets.select_dialog_title, JaMGDx.skin){
			@Override
			protected void result(Object object) {
				 MidletThread.create(microLoader, midletsClassArray[(int)object]);
			}
		};
		for (int i = 0;i<midletsNameArray.length;i++)
			builderf.button(midletsNameArray[i],midletsClassArray[i]);
		builderf.pack();
		builderf.setOrigin(Align.center);
	}

	void showErrorDialog(String message) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this)
//				.setIcon(android.R.drawable.ic_dialog_alert)
//				.setTitle(R.string.error)
//				.setMessage(message)
//				.setPositiveButton(android.R.string.ok, (d, w) -> ContextHolder.notifyDestroyed());
//		builder.setOnCancelListener(dialogInterface -> ContextHolder.notifyDestroyed());
//		builder.show();
		Dialog builderf = new Dialog(Assets.string_error, JaMGDx.skin){
			@Override
			protected void result(Object object) {
				if((Boolean)object)
					ContextHolder.notifyDestroyed();
			}
		};
		builderf.button("YES",true);
		builderf.button("NO",false);
		builderf.pack();
		builderf.setOrigin(Align.center);
	}

	private SimpleEvent msgSetCurrent = new SimpleEvent() {
		@Override
		public void process() {
			current.clearDisplayableView();
			layout.removeAllViews();
			layout.addView(current.getDisplayableView());
			invalidateOptionsMenu();
			ActionBar actionBar = getSupportActionBar();
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
			if (current instanceof Canvas) {
				hideSystemUI();
				if (!actionBarEnabled) {
					actionBar.hide();
				} else {
					final String title = current.getTitle();
					actionBar.setTitle(title == null ? AppClassLoader.getName() : title);
					layoutParams.height = (int) (getToolBarHeight() / 1.5);
				}
			} else {
				showSystemUI();
				actionBar.show();
				final String title = current.getTitle();
				actionBar.setTitle(title == null ? AppClassLoader.getName() : title);
				layoutParams.height = getToolBarHeight();
			}
			toolbar.setLayoutParams(layoutParams);
		}
	};

	private int getToolBarHeight() {
		int[] attrs = new int[]{R.attr.actionBarSize};
		TypedArray ta = obtainStyledAttributes(attrs);
		int toolBarHeight = ta.getDimensionPixelSize(0, -1);
		ta.recycle();
		return toolBarHeight;
	}

	private void hideSystemUI() {
		else if (!statusBarEnabled) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	private void showSystemUI() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	public void setCurrent(Displayable displayable) {
		current = displayable;
		ViewHandler.postEvent(msgSetCurrent);
	}

	public Displayable getCurrent() {
		return current;
	}

	public boolean isVisible() {
		return visible;
	}

	private void showExitConfirmation() {
//		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//		alertBuilder.setTitle(R.string.CONFIRMATION_REQUIRED)
//				.setMessage(R.string.FORCE_CLOSE_CONFIRMATION)
//				.setPositiveButton(android.R.string.yes, (d, w) -> MidletThread.destroyApp())
//				.setNegativeButton(android.R.string.no, null);
//		alertBuilder.create().show();
	}

	@Override
	public boolean dispatchKeyEvent(InputEvent event) {
		if (event.getKeyCode() == Input.Keys.MENU && event.type == InputEvent.Type.keyUp) {
			onKeyUp(event.getKeyCode(), event);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public void openOptionsMenu() {
		if (!actionBarEnabled  && current instanceof Canvas) {
			showSystemUI();
		}
	}

	@Override
	public boolean onKeyLongPress(int keyCode, InputEvent event) {
		if (keyCode ==  Input.Keys.BACK) {
			showExitConfirmation();
			keyLongPressed = true;
			return true;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, InputEvent event) {
		if ((keyCode == Input.Keys.BACK || keyCode == Input.Keys.MENU) && !keyLongPressed) {
			openOptionsMenu();
			return true;
		}
		keyLongPressed = false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (current != null) {
			menu.clear();
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.midlet_displayable, menu);
			if (current instanceof Canvas) {
				SubMenu group = menu.getItem(0).getSubMenu();
				if (actionBarEnabled) {
					inflater.inflate(R.menu.midlet_canvas_no_keys2, menu);
				} else {
					inflater.inflate(R.menu.midlet_canvas_no_keys, group);
				}
				VirtualKeyboard vk = ContextHolder.getVk();
				if (vk instanceof FixedKeyboard) {
					inflater.inflate(R.menu.midlet_canvas_fixed, group);
				} else if (vk != null) {
					inflater.inflate(R.menu.midlet_canvas, group);
				}
			}
			for (Command cmd : current.getCommands()) {
				menu.add(Menu.NONE, cmd.hashCode(), Menu.NONE, cmd.getAndroidLabel());
			}
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (current != null) {
			int id = item.getItemId();
			if (item.getGroupId() == R.id.action_group_common_settings) {
				if (id == R.id.action_exit_midlet) {
					showExitConfirmation();
				} else if (id == R.id.action_take_screenshot) {
					takeScreenshot();
				} else if (id == R.id.action_save_log) {
					saveLog();
				} else if (ContextHolder.getVk() != null) {
					// Handled only when virtual keyboard is enabled
					handleVkOptions(id);
				}
				return true;
			}
			return current.menuItemSelected(id);
		}

		return super.onOptionsItemSelected(item);
	}

	private void handleVkOptions(int id) {
		VirtualKeyboard vk = ContextHolder.getVk();
		switch (id) {
			case JaMGDx.Id.action_layout_edit_mode:
				vk.setLayoutEditMode(VirtualKeyboard.LAYOUT_KEYS);
				Toast.makeText(this, R.string.layout_edit_mode,
						Toast.LENGTH_SHORT).show();
				break;
			case JaMGDx.Id.action_layout_scale_mode:
				vk.setLayoutEditMode(VirtualKeyboard.LAYOUT_SCALES);
				Toast.makeText(this, R.string.layout_scale_mode,
						Toast.LENGTH_SHORT).show();
				break;
			case JaMGDx.Id.action_layout_edit_finish:
				vk.setLayoutEditMode(VirtualKeyboard.LAYOUT_EOF);
				Toast.makeText(this, R.string.layout_edit_finished,
						Toast.LENGTH_SHORT).show();
				break;
			case JaMGDx.Id.action_layout_switch:
				showSetLayoutDialog();
				break;
			case JaMGDx.Id.action_hide_buttons:
				showHideButtonDialog();
				break;
		}
	}
	private void takeScreenshot() {
		microLoader.takeScreenshot((Canvas) current);
//				.subscribeOn(Schedulers.computation())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new SingleObserver<String>() {
//					@Override
//					public void onSubscribe(Disposable d) {
//					}
//
//					@Override
//					public void onSuccess(String s) {
//						Toast.makeText(MicroActivity.this, getString(R.string.screenshot_saved)
//								+ " " + s, Toast.LENGTH_LONG).show();
//					}
//
//					@Override
//					public void onError(Throwable e) {
//						e.printStackTrace();
//						Toast.makeText(MicroActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
//					}
//				});
	}

	private void saveLog() {
		try {
			LogUtils.writeLog();
			Toast.makeText(this, R.string.log_saved, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
		}
	}

	private void showHideButtonDialog() {
		final VirtualKeyboard vk = ContextHolder.getVk();
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.hide_buttons)
				.setMultiChoiceItems(vk.getKeyNames(), vk.getKeyVisibility(),
						(dialogInterface, i, b) -> vk.setKeyVisibility(i, b))
				.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}

	private void showSetLayoutDialog() {
		final VirtualKeyboard vk = ContextHolder.getVk();
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.layout_switch)
				.setSingleChoiceItems(vk.getLayoutNames(), -1,
						(dialogInterface, i) -> vk.setLayout(i))
				.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}

	@Override
	public boolean onContextItemSelected(@NonNull MenuItem item) {
		if (current instanceof Form) {
			((Form) current).contextMenuItemSelected(item);
		} else if (current instanceof List) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			((List) current).contextMenuItemSelected(item, info.position);
		}

		return super.onContextItemSelected(item);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		ContextHolder.notifyOnActivityResult(requestCode, resultCode, data);
	}
}
