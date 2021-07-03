package com.thevoxelbox.voxelsniper.wrapper;

import com.sk89q.jnbt.CompoundTag;
import com.thevoxelbox.voxelsniper.wrapper.AsyncBlock;
import com.thevoxelbox.voxelsniper.wrapper.AsyncBlockState;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.serializer.gson.GsonComponentSerializer;
import com.sk89q.worldedit.util.formatting.text.serializer.legacy.LegacyComponentSerializer;
import com.sk89q.worldedit.world.block.BaseBlock;
import org.bukkit.DyeColor;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @deprecated In the future Fawe will need to handle that internally properly,
 *     deprecated for removal without replacement
 */
@Deprecated
public class AsyncSign extends AsyncBlockState implements Sign {

	public AsyncSign(AsyncBlock block, BaseBlock state) {
		super(block, state);
	}

	private boolean isEditable = false;

	@Override
	public @NotNull List<net.kyori.adventure.text.Component> lines() {
		throw new UnsupportedOperationException();
	}

	@Override
	public net.kyori.adventure.text.@NotNull Component line(int index) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void line(int index, net.kyori.adventure.text.@NotNull Component line) throws IndexOutOfBoundsException {

	}

	@Override
	public String[] getLines() {
		CompoundTag nbt = getNbtData();
		String[] data = new String[4];
		if (nbt != null) {
			for (int i = 1; i <= 4; i++) {
				data[i - 1] = fromJson(nbt.getString("Text" + i));
			}
		}
		return data;
	}

	private String fromJson(String jsonInput) {
		if (jsonInput == null || jsonInput.isEmpty()) {
			return "";
		}
		return GsonComponentSerializer.INSTANCE.deserialize(jsonInput).toString();
	}

	private String toJson(String oldInput) {
		if (oldInput == null || oldInput.isEmpty()) {
			return "";
		}
		return LegacyComponentSerializer.INSTANCE.serialize(TextComponent.of(oldInput));
	}

	@Override
	public String getLine(int index) throws IndexOutOfBoundsException {
		CompoundTag nbt = getNbtData();
		return nbt == null ? "" : fromJson(nbt.getString("Text" + (index + 1)));
	}

	@Override
	public void setLine(int index, String line) throws IndexOutOfBoundsException {
		final Map<String, Tag> map = this.cloneNbtMap();
		if (map.isEmpty()) {
			return;
		}
		map.put("Text" + (index + 1), new StringTag(toJson(line)));
		this.setNbtData(map);
	}

	@Override
	public boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public void setEditable(boolean arg0) {
		this.isEditable = arg0;
	}

	@Override
	public boolean isGlowingText() {
		return false;
	}

	@Override
	public void setGlowingText(boolean bl) {

	}

	@Override
	@NotNull
	public PersistentDataContainer getPersistentDataContainer() {
		return new AsyncDataContainer(this::getNbtData, this::setNbtData);
	}

	@Override
	@Nullable
	public DyeColor getColor() {
		CompoundTag nbt = this.getNbtData();
		if (nbt != null) {
			String color = nbt.getString("Color").toUpperCase(Locale.ROOT);
			if (!color.isEmpty()) {
				return DyeColor.valueOf(color);
			}
		}
		return DyeColor.BLACK;
	}

	@Override
	public void setColor(DyeColor color) {
		final Map<String, Tag> map = this.cloneNbtMap();
		if (map.isEmpty()) {
			return;
		}
		map.put("Color", new StringTag(color.name().toLowerCase(Locale.ROOT)));
		this.setNbtData(map);
	}
}
