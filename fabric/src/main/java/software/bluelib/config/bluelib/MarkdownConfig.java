/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.config.bluelib;

import software.bluelib.config.BlueLibConfig;

public class MarkdownConfig extends BlueLibConfig {

	public boolean isMarkdownEnabled = true;

	public String boldPrefix = "**";
	public String boldSuffix = "**";
	public boolean isBoldEnabled = true;

	public String italicPrefix = "*";
	public String italicSuffix = "*";
	public boolean isItalicEnabled = true;

	public String underlinePrefix = "__";
	public String underlineSuffix = "__";
	public boolean isUnderlineEnabled = true;

	public String strikethroughPrefix = "~~";
	public String strikethroughSuffix = "~~";
	public boolean isStrikethroughEnabled = true;

	public String spoilerPrefix = "||";
	public String spoilerSuffix = "||";
	public boolean isSpoilerEnabled = true;

	public String hyperlinkPrefix = "[";
	public String hyperlinkSuffix = "]";
	public boolean isHyperlinkEnabled = true;

	public String colorPrefix = "-";
	public String colorSuffix = "-";
	public boolean isColorEnabled = true;

	public boolean isCopyToClipboardEnabled = true;
}
