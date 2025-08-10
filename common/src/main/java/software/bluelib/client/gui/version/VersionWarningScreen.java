/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.client.gui.version;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@SuppressWarnings({ "unused" })
public class VersionWarningScreen extends Screen {

	public interface Consumer {

		void accept(Acknowledgement pAck, boolean pDontShowAgain);
	}

	private final String modId;
	private final String version;
	private final GridLayout layout = new GridLayout().columnSpacing(10).rowSpacing(20);
	private final OptionInstance<Boolean> dontShowAgain = OptionInstance.createBoolean("bluelib$snapshots$dont_show_again", false);
	private final Consumer consumer;

	public VersionWarningScreen(String pModId, String pVersion, Consumer pConsumer) {
		super(getTranslatableOrFallback(pModId, "version.warning.title", pModId));
		this.modId = pModId;
		this.version = pVersion;
		this.consumer = pConsumer;
	}

	@Override
	protected void init() {
		super.init();

		GridLayout.RowHelper helper = this.layout.createRowHelper(2);
		LayoutSettings settings = helper.newCellSettings().alignHorizontallyCenter();

		Component title = getTranslatableOrFallback(modId, "version.warning.title", modId);
		Component description = getTranslatableOrFallback(modId, "version.warning.description", version, modId);
		Component dontShowAgainTip = getTranslatableOrFallback(modId, "version.warning.dont_show_again_tip", modId);

		helper.addChild(new StringWidget(title, this.font), 2, settings);
		helper.addChild(new MultiLineTextWidget(description, this.font).setCentered(true).setMaxWidth(310), 2, settings);

		Checkbox dontShowAgainCheckbox = Checkbox.builder(
				getTranslatableOrFallback(modId, "version.warning.dont_show_again", modId),
				Minecraft.getInstance().font)
				.selected(dontShowAgain.get())
				.tooltip(Tooltip.create(dontShowAgainTip))
				.build();

		helper.addChild(dontShowAgainCheckbox, 2, settings);

		helper.addChild(Button.builder(CommonComponents.GUI_YES, btn -> this.consumer.accept(Acknowledgement.YES, dontShowAgainCheckbox.selected())).build());

		helper.addChild(Button.builder(CommonComponents.GUI_NO, btn -> this.consumer.accept(Acknowledgement.NO, false)).build());

		this.layout.visitWidgets(this::addRenderableWidget);
		this.layout.arrangeElements();
		this.repositionElements();
	}

	@Override
	protected void repositionElements() {
		FrameLayout.alignInRectangle(this.layout, 0, 0, this.width, this.height, 0.5F, 0.5F);
	}

	@Override
	public void onClose() {
		this.consumer.accept(Acknowledgement.NO, false);
	}

	public enum Acknowledgement {
		YES,
		NO
	}

	private static Component getTranslatableOrFallback(String pModId, String pKey, Object... pArgs) {
		String fullKey = pModId + "." + pKey;
		Component comp = Component.translatable(fullKey, pArgs);
		if (comp.getString().equals(fullKey)) {
			String fallbackKey = "bluelib." + pKey;
			return Component.translatable(fallbackKey, pArgs);
		}
		return comp;
	}
}
