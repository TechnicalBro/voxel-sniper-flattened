package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Drain_Brush
 *
 * @author Gavjenks
 * @author psanker
 */
public class DrainBrush extends AbstractBrush {

	private double trueCircle;
	private boolean disc;

	/**
	 *
	 */
	public DrainBrush() {
		this.setName("Drain");
	}

	private void drain(SnipeData snipeData) {
		int brushSize = snipeData.getBrushSize();
		double brushSizeSquared = Math.pow(brushSize + this.trueCircle, 2);
		Undo undo = new Undo();
		if (this.disc) {
			for (int x = brushSize; x >= 0; x--) {
				double xSquared = Math.pow(x, 2);
				for (int y = brushSize; y >= 0; y--) {
					if ((xSquared + Math.pow(y, 2)) <= brushSizeSquared) {
						if (this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_STATIONARY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_LAVA.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_STATIONARY_LAVA.getId()) {
							undo.put(this.clampY(this.getTargetBlock()
								.getX() + x, this.getTargetBlock()
								.getY(), this.getTargetBlock()
								.getZ() + y));
							this.setBlockIdAt(this.getTargetBlock()
								.getZ() + y, this.getTargetBlock()
								.getX() + x, this.getTargetBlock()
								.getY(), Material.LEGACY_AIR.getId());
						}
						if (this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_STATIONARY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_LAVA.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_STATIONARY_LAVA.getId()) {
							undo.put(this.clampY(this.getTargetBlock()
								.getX() + x, this.getTargetBlock()
								.getY(), this.getTargetBlock()
								.getZ() - y));
							this.setBlockIdAt(this.getTargetBlock()
								.getZ() - y, this.getTargetBlock()
								.getX() + x, this.getTargetBlock()
								.getY(), Material.LEGACY_AIR.getId());
						}
						if (this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_STATIONARY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_LAVA.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() + y) == Material.LEGACY_STATIONARY_LAVA.getId()) {
							undo.put(this.clampY(this.getTargetBlock()
								.getX() - x, this.getTargetBlock()
								.getY(), this.getTargetBlock()
								.getZ() + y));
							this.setBlockIdAt(this.getTargetBlock()
								.getZ() + y, this.getTargetBlock()
								.getX() - x, this.getTargetBlock()
								.getY(), Material.LEGACY_AIR.getId());
						}
						if (this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_STATIONARY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_LAVA.getId() || this.getBlockIdAt(this.getTargetBlock()
							.getX() - x, this.getTargetBlock()
							.getY(), this.getTargetBlock()
							.getZ() - y) == Material.LEGACY_STATIONARY_LAVA.getId()) {
							undo.put(this.clampY(this.getTargetBlock()
								.getX() - x, this.getTargetBlock()
								.getY(), this.getTargetBlock()
								.getZ() - y));
							this.setBlockIdAt(this.getTargetBlock()
								.getZ() - y, this.getTargetBlock()
								.getX() - x, this.getTargetBlock()
								.getY(), Material.LEGACY_AIR.getId());
						}
					}
				}
			}
		} else {
			for (int y = (brushSize + 1) * 2; y >= 0; y--) {
				double ySquared = Math.pow(y - brushSize, 2);
				for (int x = (brushSize + 1) * 2; x >= 0; x--) {
					double xSquared = Math.pow(x - brushSize, 2);
					for (int z = (brushSize + 1) * 2; z >= 0; z--) {
						if ((xSquared + Math.pow(z - brushSize, 2) + ySquared) <= brushSizeSquared) {
							if (this.getBlockIdAt(this.getTargetBlock()
								.getX() + x - brushSize, this.getTargetBlock()
								.getY() + z - brushSize, this.getTargetBlock()
								.getZ() + y - brushSize) == Material.LEGACY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
								.getX() + x - brushSize, this.getTargetBlock()
								.getY() + z - brushSize, this.getTargetBlock()
								.getZ() + y - brushSize) == Material.LEGACY_STATIONARY_WATER.getId() || this.getBlockIdAt(this.getTargetBlock()
								.getX() + x - brushSize, this.getTargetBlock()
								.getY() + z - brushSize, this.getTargetBlock()
								.getZ() + y - brushSize) == Material.LEGACY_LAVA.getId() || this.getBlockIdAt(this.getTargetBlock()
								.getX() + x - brushSize, this.getTargetBlock()
								.getY() + z - brushSize, this.getTargetBlock()
								.getZ() + y - brushSize) == Material.LEGACY_STATIONARY_LAVA.getId()) {
								undo.put(this.clampY(this.getTargetBlock()
									.getX() + x, this.getTargetBlock()
									.getY() + z, this.getTargetBlock()
									.getZ() + y));
								this.setBlockIdAt(this.getTargetBlock()
									.getZ() + y - brushSize, this.getTargetBlock()
									.getX() + x - brushSize, this.getTargetBlock()
									.getY() + z - brushSize, Material.LEGACY_AIR.getId());
							}
						}
					}
				}
			}
		}
		snipeData.getOwner()
			.storeUndo(undo);
	}

	@Override
	protected final void arrow(SnipeData snipeData) {
		this.drain(snipeData);
	}

	@Override
	protected final void powder(SnipeData snipeData) {
		this.drain(snipeData);
	}

	@Override
	public final void info(Message message) {
		message.brushName(this.getName());
		message.size();
		message.custom(ChatColor.AQUA + ((this.trueCircle == 0.5) ? "True circle mode ON" : "True circle mode OFF"));
		message.custom(ChatColor.AQUA + ((this.disc) ? "Disc drain mode ON" : "Disc drain mode OFF"));
	}

	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		for (int i = 1; i < parameters.length; i++) {
			String parameter = parameters[i];
			if (parameter.equalsIgnoreCase("info")) {
				snipeData.sendMessage(ChatColor.GOLD + "Drain Brush Parameters:");
				snipeData.sendMessage(ChatColor.AQUA + "/b drain true -- will use a true sphere algorithm instead of the skinnier version with classic sniper nubs. /b drain false will switch back. (false is default)");
				snipeData.sendMessage(ChatColor.AQUA + "/b drain d -- toggles disc drain mode, as opposed to a ball drain mode");
				return;
			} else if (parameter.startsWith("true")) {
				this.trueCircle = 0.5;
				snipeData.sendMessage(ChatColor.AQUA + "True circle mode ON.");
			} else if (parameter.startsWith("false")) {
				this.trueCircle = 0;
				snipeData.sendMessage(ChatColor.AQUA + "True circle mode OFF.");
			} else if (parameter.equalsIgnoreCase("d")) {
				if (this.disc) {
					this.disc = false;
					snipeData.sendMessage(ChatColor.AQUA + "Disc drain mode OFF");
				} else {
					this.disc = true;
					snipeData.sendMessage(ChatColor.AQUA + "Disc drain mode ON");
				}
			} else {
				snipeData.sendMessage(ChatColor.RED + "Invalid brush parameters! use the info parameter to display parameter info.");
			}
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.drain";
	}
}
