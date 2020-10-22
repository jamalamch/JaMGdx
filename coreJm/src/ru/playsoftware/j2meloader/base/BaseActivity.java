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

package ru.playsoftware.j2meloader.base;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class BaseActivity extends ApplicationAdapter {
	public String Acivityname = "test";
	public void create() {
		Preferences preferences = Gdx.app.getPreferences(Acivityname);
		String theme = preferences.getString("pref_theme", "light");
		if (theme.equals("dark")) {
			//setTheme(R.style.AppTheme);
		} else {
			//setTheme(R.style.AppTheme_Light);
		}
//		if (getSupportActionBar() != null) {
//			getSupportActionBar().setElevation(getResources().getDisplayMetrics().density * 2);
//		}
	}
}
