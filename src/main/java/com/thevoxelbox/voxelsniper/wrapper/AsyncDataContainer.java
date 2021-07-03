package com.thevoxelbox.voxelsniper.wrapper;

import com.fastasyncworldedit.core.FaweCache;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.Tag;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @deprecated In the future Fawe will need to handle that internally properly,
 *     deprecated for removal without replacement
 */
@Deprecated
public final class AsyncDataContainer implements PersistentDataContainer {

	private final Supplier<CompoundTag> supplier;
	private final Consumer<CompoundTag> consumer;

	public AsyncDataContainer(
		final @NotNull Supplier<CompoundTag> supplier,
		final @NotNull Consumer<CompoundTag> consumer
	) {
		this.supplier = supplier;
		this.consumer = consumer;
	}

	private CompoundTag root() {
		return (CompoundTag) supplier.get().getValue().get("PublicBukkitValues");
	}

	private Map<String, Tag> get() {
		return get(true);
	}

	private Map<String, Tag> get(boolean create) {
		CompoundTag tag = root();
		Map<String, Tag> raw;
		if (tag == null) {
			if (!create) {
				return Collections.emptyMap();
			}
			final Map<String, Tag> map = new HashMap<>(root().getValue());
			map.put("PublicBukkitValues", new CompoundTag(raw = new HashMap<>()));
			this.consumer.accept(new CompoundTag(map));
		} else {
			raw = tag.getValue();
		}
		return raw;
	}

	public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
		Validate.notNull(key, "The provided key for the custom value was null");
		Validate.notNull(type, "The provided type for the custom value was null");
		Validate.notNull(value, "The provided value for the custom value was null");
		// Modify public values
		final Map<String, Tag> publicValues = new HashMap<>(this.get());
		publicValues.put(key.toString(), FaweCache.IMP.asTag(type.toPrimitive(value, null)));
		// Modify the root tag
		final Map<String, Tag> map = new HashMap<>(root().getValue());
		map.put("PublicBukkitValues", new CompoundTag(publicValues));
		// Update the owning object
		this.consumer.accept(new CompoundTag(map));
	}

	public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type) {
		Validate.notNull(key, "The provided key for the custom value was null");
		Validate.notNull(type, "The provided type for the custom value was null");
		Tag value = get(false).get(key.toString());
		if (value == null) {
			return false;
		}
		return type.getPrimitiveType() == value.getValue().getClass();
	}

	public <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type) {
		Validate.notNull(key, "The provided key for the custom value was null");
		Validate.notNull(type, "The provided type for the custom value was null");
		Tag value = get(false).get(key.toString());
		return (Z) value.toRaw();
	}

	public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue) {
		Z z = this.get(key, type);
		return z != null ? z : defaultValue;
	}

	@NotNull
	@Override
	public Set<NamespacedKey> getKeys() {
		Set<NamespacedKey> keys = new HashSet<>();
		this.get(false).keySet().forEach(key -> {
			String[] keyData = key.split(":", 2);
			if (keyData.length == 2) {
				keys.add(new NamespacedKey(keyData[0], keyData[1]));
			}

		});
		return keys;
	}

	public void remove(NamespacedKey key) {
		Validate.notNull(key, "The provided key for the custom value was null");
		// Modify public values
		final Map<String, Tag> publicValues = new HashMap<>(this.get(false));
		publicValues.remove(key.toString());
		// Modify the root tag
		final Map<String, Tag> map = new HashMap<>(root().getValue());
		map.put("PublicBukkitValues", new CompoundTag(publicValues));
		// Update the owning object
		this.consumer.accept(new CompoundTag(map));
	}

	public boolean isEmpty() {
		return get(false).isEmpty();
	}

	public PersistentDataAdapterContext getAdapterContext() {
		return null;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AsyncDataContainer)) {
			return false;
		} else {
			Map<String, Tag> myRawMap = this.getRaw();
			Map<String, Tag> theirRawMap = ((AsyncDataContainer) obj).getRaw();
			return Objects.equals(myRawMap, theirRawMap);
		}
	}

	public Map<String, Tag> getRaw() {
		return get(false);
	}

	public int hashCode() {
		return get(false).hashCode();
	}

	public Map<String, Object> serialize() {
		return new CompoundTag(get(false)).toRaw();
	}
}