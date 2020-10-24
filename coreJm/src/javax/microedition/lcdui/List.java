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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Collections;

import javax.microedition.lcdui.list.CompoundListAdapter;

public class List extends Screen implements Choice{
	public static final Command SELECT_COMMAND = new Command("", Command.SCREEN, 0);

	private Array<Boolean> selected;
	private CompoundListAdapter adapter;

	private int listType;
	private int selectedIndex = -1;
	private int fitPolicy;

	private Command selectCommand = SELECT_COMMAND;

//	private SimpleEvent msgSetSelection = new SimpleEvent() {
//		@Override
//		public void process() {
//			list.setSelection(selectedIndex);
//		}
//	};
//
//	private SimpleEvent msgSetContextMenuListener = new SimpleEvent() {
//		@Override
//		public void process() {
//			if (listener != null) {
//				list.setOnCreateContextMenuListener(List.this);
//			} else {
//				list.setLongClickable(false);
//			}
//		}
//	};

//	private class ClickListener {
//		@Override
//		public void onItemClick(int position, long id) {
//			selectedIndex = position;
//			switch (listType) {
//				case IMPLICIT:
//					fireCommandAction(selectCommand, List.this);
//					break;
//				case EXCLUSIVE:
//					if (position >= 0 && position < selected.size()) {
//						Collections.fill(selected, Boolean.FALSE);
//						selected.set(position, Boolean.TRUE);
//					}
//					break;
//				case MULTIPLE:
//					if (position >= 0 && position < selected.size()) {
//						selected.set(position, !selected.get(position));
//					}
//					break;
//			}
//			adapter.notifyDataSetChanged();
//		}
//	}
//
//	private ClickListener clicklistener = new ClickListener();

	public List(String title, int listType) {
		switch (listType) {
			case IMPLICIT:
			case EXCLUSIVE:
			case MULTIPLE:
				this.listType = listType;
				break;

			default:
				throw new IllegalArgumentException("list type " + listType + " is not supported");
		}

		setTitle(title);
	}

	public List(String title, int listType, String[] stringElements, Image[] imageElements) {
		this(title, listType);
		selected = new Array<Boolean>(stringElements.length);
		if (stringElements != null && imageElements != null && imageElements.length != stringElements.length) {
			throw new IllegalArgumentException("string and image arrays have different length");
		}

//		for(int i=0; i<stringElements.length; i++)
//		{
//			if(imageElements!=null)
//			{
//				items.add(new ImageItem(stringElements[i], imageElements[i], 0, stringElements[i]));
//			}
//			else
//			{
//				items.add(new StringItem(stringElements[i], stringElements[i]));
//			}
//		}
//
//		if (stringElements != null) {
//			strings.addAll(Arrays.asList(stringElements));
//		}
//
//		if (imageElements != null) {
//			images.addAll(Arrays.asList(imageElements));
//		}
//
//		int size = Math.max(strings.size(), images.size());
//
//		if (size > 0) {
//			selected.addAll(Collections.nCopies(size, Boolean.FALSE));
//
//			if (strings.size() == 0) {
//				strings.addAll(Collections.nCopies(size, null));
//			}
//
//			if (images.size() == 0) {
//				images.addAll(Collections.nCopies(size, null));
//			}
//		}

		setTitle(title);
		for(int i=0; i<stringElements.length; i++)
		{
			if(imageElements!=null)
			{
				addActor(new ImageItem(stringElements[i], imageElements[i], 0, stringElements[i]));
			}
			else
			{
				addActor(new StringItem(stringElements[i], stringElements[i]));
			}
		}
	}

	public void setSelectCommand(Command cmd) {
		if (selectCommand != SELECT_COMMAND) {
			removeCommand(selectCommand);
		}

		if (cmd != null) {
			addCommand(selectCommand = cmd);
		} else {
			selectCommand = SELECT_COMMAND;
		}
	}

	@Override
	public int append(String stringPart, Image imagePart) {
//		synchronized (selected) {
//			int index = selected.size();
//			boolean select = index == 0 && listType != MULTIPLE;
//
//			strings.add(stringPart);
//			images.add(imagePart);
//
//			if (select) {
//				selectedIndex = index;
//			}
//
//			if (list != null) {
//				adapter.append(stringPart, imagePart);
//			}
//
//			return index;
//		}
		int index = selected.size;
		boolean select = index == 0 && listType != MULTIPLE;
		selected.add(select);
		if(imagePart!=null)
		{
			addActor(new ImageItem(stringPart, imagePart, 0, stringPart));
		}
		else
		{
			addActor(new StringItem(stringPart, stringPart));
		}
		return getRoot().getChildren().size-1;
	}

	@Override
	public void delete(int elementNum) {
//		synchronized (selected) {
//			strings.remove(elementNum);
//			images.remove(elementNum);
//
//
//			if (selected.size() == 0) {
//				selectedIndex = -1;
//			}
//
//			if (list != null) {
//				adapter.delete(elementNum);
//			}
//		}
		selected.removeIndex(elementNum);
		getRoot().getChildren().removeIndex(elementNum);
	}

	@Override
	public void deleteAll() {
//		synchronized (selected) {
//			strings.clear();
//			images.clear();
//			selected.clear();
//
//			selectedIndex = -1;
//
//			if (list != null) {
//				adapter.deleteAll();
//			}
//		}
		selected.clear();
		clear();
	}

	@Override
	public Image getImage(int elementNum) {
		ImageItem img = (ImageItem)getRoot().getChild(elementNum);
		return img.getImage();
	}

	@Override
	public int getSelectedFlags(boolean[] selectedArray) {
		synchronized (selected) {
			if (selectedArray.length < selected.size) {
				throw new IllegalArgumentException("return array is too short");
			}

			int index = 0;
			int selectedCount = 0;

			for (Boolean flag : selected) {
				if (flag) {
					selectedCount++;
				}

				selectedArray[index++] = flag;
			}

			while (index < selectedArray.length) {
				selectedArray[index++] = false;
			}

			return selectedCount;
		}
	}

	@Override
	public int getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public String getString(int elementNum) {
		StringItem str = (StringItem)getRoot().getChild(elementNum);
		return str.getText();
	}

	@Override
	public void insert(int elementNum, String stringPart, Image imagePart) {
//		synchronized (selected) {
//			boolean select = selected.size() == 0 && listType != MULTIPLE;
//
//			strings.add(elementNum, stringPart);
//			images.add(elementNum, imagePart);
//			selected.add(elementNum, select);
//
//			if (select) {
//				selectedIndex = elementNum;
//			}
//
//			if (list != null) {
//				adapter.insert(elementNum, stringPart, imagePart);
//			}
//		}
//		if(elementNum<items.size() && elementNum>0)
//		{
//			try
//			{
//				if(imagePart!=null)
//				{
//					items.add(elementNum, new ImageItem(stringPart, imagePart, 0, stringPart));
//				}
//				else
//				{
//					items.add(elementNum, new StringItem(stringPart, stringPart));
//				}
//			}
//			catch(Exception e)
//			{
//				append(stringPart, imagePart);
//			}
//		}
		boolean select = selected.size == 0 && listType != MULTIPLE;
		selected.insert(elementNum, select);
		Item item;
		if(imagePart!=null)
		{
			item =  new ImageItem(stringPart, imagePart, 0, stringPart);
		}
		else
		{
			item = new StringItem(stringPart, stringPart);
		}
		getRoot().addActorAt(elementNum, item);
	}

	@Override
	public boolean isSelected(int elementNum) {
		return selected.get(elementNum);
	}

	@Override
	public void set(int elementNum, String stringPart, Image imagePart) {
//		synchronized (selected) {
//			strings.set(elementNum, stringPart);
//			images.set(elementNum, imagePart);
//
//			if (list != null) {
//				adapter.set(elementNum, stringPart, imagePart);
//			}
//		}
		Item item;
		if(imagePart!=null)
		{
			item =  new ImageItem(stringPart, imagePart, 0, stringPart);
		}
		else
		{
			item = new StringItem(stringPart, stringPart);
		}
		getRoot().getChildren().set(elementNum,item);
	}

	@Override
	public void setSelectedFlags(boolean[] selectedArray) {
		if (listType == EXCLUSIVE || listType == IMPLICIT) {
			for (int i = 0; i < selectedArray.length; i++) {
				if (selectedArray[i]) {
					setSelectedIndex(i, true);
					return;
				}
			}
		}

		synchronized (selected) {
			if (selectedArray.length < selected.size) {
				throw new IllegalArgumentException("array is too short");
			}

			int size = selected.size;

			for (int i = 0; i < size; i++) {
				selected.set(i,selectedArray[i]);
			}
		}
	}

	@Override
	public void setSelectedIndex(int elementNum, boolean flag) {
		synchronized (selected) {
			selected.set(elementNum,flag);

			if (flag) {
				selectedIndex = elementNum;
			}
		}
	}

	@Override
	public void setFont(int elementNum, Font font) {
	}

	@Override
	public Font getFont(int elementNum) {
		return Font.getDefaultFont();
	}

	@Override
	public void setFitPolicy(int fitPolicy) {
		this.fitPolicy = fitPolicy;
	}

	@Override
	public int getFitPolicy() {
		return fitPolicy;
	}

	@Override
	public int size() {
		synchronized (selected) {
			return selected.size;
		}
	}

//	@Override
//	public View getScreenView() {
//		Context context = getParentActivity();
//
//		adapter = new CompoundListAdapter(context, this, listType);
//
//		list = new ListView(context);
//		list.setAdapter(adapter);
//
//		int size = selected.size();
//
//		for (int i = 0; i < size; i++) {
//			adapter.append(strings.get(i), images.get(i));
//		}
//
//		if (listType == IMPLICIT && selectedIndex >= 0 && selectedIndex < selected.size()) {
//			list.setSelection(selectedIndex);
//		}
//
//		list.setOnItemClickListener(clicklistener);
//		ViewHandler.postEvent(msgSetContextMenuListener);
//
//		return list;
//	}
//
//	@Override
//	public void clearScreenView() {
//		list = null;
//		adapter = null;
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//		menu.clear();
//
//		for (Command cmd : getCommands()) {
//			menu.add(hashCode(), cmd.hashCode(), cmd.getPriority(), cmd.getAndroidLabel());
//		}
//	}
//
//	public boolean contextMenuItemSelected(MenuItem item, int selectedIndex) {
//		if (listener == null) {
//			return false;
//		}
//		this.selectedIndex = selectedIndex;
//
//		int id = item.getItemId();
//
//		for (Command cmd : getCommands()) {
//			if (cmd.hashCode() == id) {
//				fireCommandAction(cmd, this);
//				return true;
//			}
//		}
//		return false;
//	}
}