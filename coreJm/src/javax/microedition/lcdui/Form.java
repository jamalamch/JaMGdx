/*
 * Copyright 2012 Kulikov Dmitriy
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

package javax.microedition.lcdui;

import java.util.ArrayList;

public class Form extends Screen {
	private static final float BORDER_PADDING = 7;

//	private ScrollView scrollview;
//	private LinearLayout layout;

	private ArrayList<Item> items = new ArrayList<>();
	private ItemStateListener listener;

	public Form(String title) {
		setTitle(title);
	}

	public Form(String title, Item[] elements) {
		setTitle(title);
		for (Item item : elements) {
			append(item);
		}
	}

	public Item get(int index) {
		return items.get(index);
	}

	public int size() {
		return items.size();
	}

	public int append(String text) {
		return append(new StringItem(null, text));
	}

	public int append(Image img) {
		return append(new ImageItem(null, img, ImageItem.LAYOUT_DEFAULT, null));
	}

	public int append(final Item item) {
		if (item.hasOwnerForm()) {
			throw new IllegalStateException();
		}

		items.add(item);
		item.setOwnerForm(this);
		addActor(item);
//		if (layout != null) {
//			AppCompatActivity a = getParentActivity();
//			if (a != null) {
//				View v = item.getItemView();
//				a.runOnUiThread(() -> layout.addView(v));
//			}
//		}
		return items.size() - 1;
	}

	public void insert(final int index, final Item item) {
		if (item.hasOwnerForm()) {
			throw new IllegalStateException();
		}

		items.add(index, item);
		item.setOwnerForm(this);
		getRoot().addActorAt(index,item);
//		if (layout != null) {
//			AppCompatActivity a = getParentActivity();
//			if (a != null) {
//				View v = item.getItemView();
//				a.runOnUiThread(() -> layout.addView(v, index));
//			}
//		}
	}

	public void set(final int index, final Item item) {
		if (item.hasOwnerForm()) {
			throw new IllegalStateException();
		}

		items.set(index, item).setOwnerForm(null);
		item.setOwnerForm(this);
		getRoot().getChildren().set(index,item);
//		if (layout != null) {
//			AppCompatActivity a = getParentActivity();
//			if (a != null) {
//				a.runOnUiThread(() -> {
//					View v = item.getItemView();
//					layout.removeViewAt(index);
//					layout.addView(v, index);
//				});
//			}
//		}
	}

	public void delete(final int index) {
		items.remove(index).setOwnerForm(null);
		getRoot().getChildren().removeIndex(index);
//		if (layout != null) {
//			AppCompatActivity a = getParentActivity();
//			if (a != null) {
//				a.runOnUiThread(() -> layout.removeViewAt(index));
//			}
//		}
	}

	public void deleteAll() {
		for (Item item : items) {
			item.setOwnerForm(null);
		}

		items.clear();
		clear();
//		if (layout != null) {
//			AppCompatActivity a = getParentActivity();
//			if (a != null) {
//				a.runOnUiThread(() -> layout.removeAllViews());
//			}
//		}
	}

	public void setItemStateListener(ItemStateListener listener) {
		this.listener = listener;
	}

	public void notifyItemStateChanged(Item item) {
		if (listener != null) {
			listener.itemStateChanged(item);
		}
	}

//	@Override
//	public View getScreenView() {
//		if (scrollview == null) {
//			Context context = getParentActivity();
//
//			layout = new LinearLayout(context);
//			layout.setOrientation(LinearLayout.VERTICAL);
//
//			int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BORDER_PADDING, context.getResources().getDisplayMetrics()));
//			layout.setPadding(padding, padding, padding, padding);
//
//			scrollview = new ScrollView(context);
//			scrollview.addView(layout);
//
//			for (Item item : items) {
//				layout.addView(item.getItemView());
//			}
//		}
//
//		return scrollview;
//	}
//
//	@Override
//	public void clearScreenView() {
//		scrollview = null;
//		layout = null;
//
//		Item[] array = items.toArray(new Item[0]);
//		for (Item item : array) {
//			item.clearItemView();
//		}
//	}
//
//	public boolean contextMenuItemSelected(MenuItem menuitem) {
//		for (Item item : items) {
//			if (menuitem.getGroupId() == item.hashCode() && item.contextMenuItemSelected(menuitem)) {
//				return true;
//			}
//		}
//
//		return false;
//	}
}
