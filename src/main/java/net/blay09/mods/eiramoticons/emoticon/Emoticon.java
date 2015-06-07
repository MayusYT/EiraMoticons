// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.emoticon;

import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.blay09.mods.eiramoticons.render.EmoticonRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;

import java.awt.image.BufferedImage;

public class Emoticon implements IEmoticon {

	public final IEmoticonLoader loader;
	public final int id;
	public final String name;

	private Object identifier;
	private String[] tooltipLines;
	private boolean manualOnly;

	private boolean loadRequested;
	private int textureId = -1;
	private int width;
	private int height;
	private float scaleX;
	private float scaleY;
	private BufferedImage loadBuffer;

	public Emoticon(int id, String name, IEmoticonLoader loader) {
		this.id = id;
		this.name = name;
		this.loader = loader;

		tooltipLines = new String[] {I18n.format("eiramoticons:tooltip.name", name)};
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getChatString() {
		return "\u00a7z" + id + "    ";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getLoadData() {
		return identifier;
	}

	@Override
	public void setLoadData(Object loadData) {
		this.identifier = loadData;
	}

	@Override
	public IEmoticonLoader getLoader() {
		return loader;
	}

	@Override
	public boolean isManualOnly() {
		return manualOnly;
	}

	@Override
	public void setManualOnly(boolean manualOnly) {
		this.manualOnly = manualOnly;
	}

	@Override
	public void setTooltip(String emoticonGroup) {
		tooltipLines = new String[] {I18n.format("eiramoticons:tooltip.name", name), I18n.format("eiramoticons:tooltip.group", emoticonGroup)};
	}

	@Override
	public void setCustomTooltip(String[] tooltipLines) {
		this.tooltipLines = tooltipLines;
	}

	@Override
	public void setImage(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		float renderWidth = width;
		float renderHeight = height;
		if(renderWidth > EmoticonRenderer.EMOTICON_WIDTH) {
			float factor = EmoticonRenderer.EMOTICON_WIDTH / renderWidth;
			renderWidth *= factor;
			renderHeight *= factor;
		}
		if(renderHeight > EmoticonRenderer.EMOTICON_HEIGHT) {
			float factor = EmoticonRenderer.EMOTICON_HEIGHT / renderHeight;
			renderWidth *= factor;
			renderHeight *= factor;
		}
		scaleX = renderWidth / width;
		scaleY = renderHeight / height;
		loadBuffer = image;
	}

	public int getTextureId() {
		if(loadBuffer != null) {
			textureId = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), loadBuffer);
			loadBuffer = null;
		}
		return textureId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void requestTexture() {
		if(!loadRequested) {
			loadRequested = true;
			AsyncEmoticonLoader.instance.loadAsync(this);
		}
	}

	public void disposeTexture() {
		if(textureId != -1) {
			TextureUtil.deleteTexture(textureId);
		}
	}

	public String[] getTooltip() {
		return tooltipLines;
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}
}
