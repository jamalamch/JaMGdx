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

import com.badlogic.gdx.utils.StringBuilder;

public class TextField extends Item {

	public static final int ANY = 0;
	public static final int CONSTRAINT_MASK = 0xFFFF;
	public static final int DECIMAL = 5;
	public static final int EMAILADDR = 1;
	public static final int INITIAL_CAPS_SENTENCE = 0x200000;
	public static final int INITIAL_CAPS_WORD = 0x100000;
	public static final int NON_PREDICTIVE = 0x80000;
	public static final int NUMERIC = 2;
	public static final int PASSWORD = 0x10000;
	public static final int PHONENUMBER = 3;
	public static final int SENSITIVE = 0x40000;
	public static final int UNEDITABLE = 0x20000;
	public static final int URL = 4;


	private String text;
	private int max;
	private int constraints;
	private String mode;

	public TextField(String label, String value, int maxSize, int Constraints)
	{
		setLabel(label);
		text = value;
		max = maxSize;
		constraints = Constraints;
	}

	void delete(int offset, int length)
	{
		text = text.substring(0, offset) + text.substring(offset+length);
	}

	public int getCaretPosition() { return 0; }

	public int getChars(char[] data)
	{
		for(int i=0; i<text.length(); i++)
		{
			data[i] = text.charAt(i);
		}
		return text.length();
	}

	public int getConstraints() { return constraints; }

	public int getMaxSize() { return max; }

	public String getString() { return text; }

	public void insert(char[] data, int offset, int length, int position)
	{
		StringBuilder out = new StringBuilder();
		out.append(text, 0, position);
		out.append(data, offset, length);
		out.append(text.substring(position));
		text = out.toString();
	}

	public void insert(String src, int position)
	{
		StringBuilder out = new StringBuilder();
		out.append(text, 0, position);
		out.append(src);
		out.append(text.substring(position));
		text = out.toString();
	}

	public void setChars(char[] data, int offset, int length)
	{
		StringBuilder out = new StringBuilder();
		out.append(data, offset, length);
		text = out.toString();
	}

	public void setConstraints(int Constraints) { constraints = Constraints; }

	public void setInitialInputMode(String characterSubset) { mode = characterSubset; }

	public int setMaxSize(int maxSize) { max = maxSize; return max; }

	public void setString(String value) { text = value; }

	public int size() { return text.length(); }

//	private TextFieldImpl textField;
//
//	public TextField(String label, String text, int maxSize, int constraints) {
//		textField = new TextFieldImpl();
//		setLabel(label);
//		setMaxSize(maxSize);
//		setConstraints(constraints);
//		setString(text);
//	}
//
//	public void setString(String text) {
//		textField.setString(text);
//		text = text;
//	}
//
//	public void insert(String src, int pos) {
//		textField.insert(src, pos);
//	}
//
//	public void insert(char[] data, int offset, int length, int position) {
//		insert(new String(data, offset, length), position);
//	}
//
//	public String getString() {
//		return textField.getString();
//	}
//
//	public int size() {
//		return getString().length();
//	}
//
//	public int setMaxSize(int maxSize) {
//		return textField.setMaxSize(maxSize);
//	}
//
//	public int getMaxSize() {
//		return textField.getMaxSize();
//	}
//
//	public void setConstraints(int constraints) {
//		textField.setConstraints(constraints);
//	}
//
//	public int getConstraints() {
//		return textField.getConstraints();
//	}
//
//	public void setInitialInputMode(String characterSubset) {
//	}
//
//	public void setChars(char[] data, int offset, int length) {
//		setString(new String(data, offset, length));
//	}
//
//	public int getChars(char[] data) {
//		return textField.getChars(data);
//	}
//
//	public int getCaretPosition() {
//		return textField.getCaretPosition();
//	}
//
//	public void delete(int offset, int length) {
//		textField.delete(offset, length);
//	}

}