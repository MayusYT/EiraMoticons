// Copyright (c) 2015, Christopher "blay09" Baker
// Some rights reserved.

package net.blay09.mods.eiramoticons.addon;

import com.google.common.io.Files;
import net.blay09.mods.eiramoticons.api.EiraMoticonsAPI;
import net.blay09.mods.eiramoticons.api.IEmoticon;
import net.blay09.mods.eiramoticons.api.IEmoticonLoader;
import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileAddon implements IEmoticonLoader {

	public FileAddon() {
		File emoticonDir = new File(Minecraft.getMinecraft().mcDataDir, "emoticons");
		File[] emoticons = emoticonDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".bmp");
			}
		});
		if(emoticons != null) {
			for(File emoticonFile : emoticons) {
				String nameWithoutExt = Files.getNameWithoutExtension(emoticonFile.getName());
				IEmoticon emoticon = EiraMoticonsAPI.registerEmoticon(nameWithoutExt, this);
				emoticon.setIdentifier(emoticonFile);
				emoticon.setTooltip(new String[] {"\u00a7eEmote: \u00a7r" + nameWithoutExt});
			}
		}
	}

	@Override
	public void loadEmoticonImage(IEmoticon emoticon) {
		try {
			BufferedImage image = ImageIO.read((File) emoticon.getIdentifier());
			if(image != null) {
				emoticon.setImage(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}