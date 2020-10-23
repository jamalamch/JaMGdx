/*
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

package ru.playsoftware.j2meloader.settings;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.*;

import javax.microedition.lcdui.Canvas;
import javax.microedition.util.param.SharedPreferencesContainer;

public class KeyMapper {
	private static final String PREF_KEY = "KeyMappings";

	public static void saveArrayPref(SharedPreferencesContainer container, IntIntMap intArray) {
		Json json = new Json();
		json.writeValue(intArray,IntIntMap.class);
		container.putString(PREF_KEY, intArray.size == 0 ? null : intArray.toString());
		container.apply();
	}

	public static IntIntMap getArray(String json) {
		IntIntMap intArray = new IntIntMap();
		if (json != null) {
			JsonReader jsonArray = new JsonReader();
			JsonValue jsonValue = jsonArray.parse(json);
			for (int i = 0; i < jsonValue.size; i++) {
				JsonValue item = jsonValue.get(i);
				intArray.put(item.getInt("key"), item.getInt("value"));
			}
		} else {
			initArray(intArray);
		}
		return intArray;
	}

	public static IntIntMap getArrayPref(SharedPreferencesContainer container) {
		return getArray(container.getString(PREF_KEY, null));
	}

	public static void initArray(IntIntMap intDict) {
		intDict.put(Input.Keys.NUM_0, Canvas.KEY_NUM0);
		intDict.put(Input.Keys.NUM_1, Canvas.KEY_NUM1);
		intDict.put(Input.Keys.NUM_2, Canvas.KEY_NUM2);
		intDict.put(Input.Keys.NUM_3, Canvas.KEY_NUM3);
		intDict.put(Input.Keys.NUM_4, Canvas.KEY_NUM4);
		intDict.put(Input.Keys.NUM_5, Canvas.KEY_NUM5);
		intDict.put(Input.Keys.NUM_6, Canvas.KEY_NUM6);
		intDict.put(Input.Keys.NUM_7, Canvas.KEY_NUM7);
		intDict.put(Input.Keys.NUM_8, Canvas.KEY_NUM8);
		intDict.put(Input.Keys.NUM_9, Canvas.KEY_NUM9);
		intDict.put(Input.Keys.STAR, Canvas.KEY_STAR);
		intDict.put(Input.Keys.POUND, Canvas.KEY_POUND);
		intDict.put(Input.Keys.UP, Canvas.KEY_UP);
		intDict.put(Input.Keys.DOWN, Canvas.KEY_DOWN);
		intDict.put(Input.Keys.LEFT, Canvas.KEY_LEFT);
		intDict.put(Input.Keys.RIGHT, Canvas.KEY_RIGHT);
		intDict.put(Input.Keys.ENTER, Canvas.KEY_FIRE);
		intDict.put(Input.Keys.SOFT_LEFT, Canvas.KEY_SOFT_LEFT);
		intDict.put(Input.Keys.SOFT_RIGHT, Canvas.KEY_SOFT_RIGHT);
		intDict.put(Input.Keys.CALL, Canvas.KEY_SEND);
		intDict.put(Input.Keys.ENDCALL, Canvas.KEY_END);
	}
}
