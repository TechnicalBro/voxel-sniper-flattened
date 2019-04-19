package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Snow_cone_brush
 *
 * @author Voxel
 */
public class SnowConeBrush extends AbstractBrush {

	private void addSnow(SnipeData v, Block targetBlock) {
		int brushSize;
		int blockPositionX = targetBlock.getX();
		int blockPositionY = targetBlock.getY();
		int blockPositionZ = targetBlock.getZ();
		if (this.getBlockIdAt(blockPositionX, blockPositionY, blockPositionZ) == Material.LEGACY_AIR.getId()) {
			brushSize = 0;
		} else {
			brushSize = this.clampY(blockPositionX, blockPositionY, blockPositionZ)
				.getData() + 1;
		}
		int brushSizeDoubled = 2 * brushSize;
		int[][] snowcone = new int[brushSizeDoubled + 1][brushSizeDoubled + 1]; // Will hold block IDs
		int[][] snowconeData = new int[brushSizeDoubled + 1][brushSizeDoubled + 1]; // Will hold data values for snowcone
		int[][] yOffset = new int[brushSizeDoubled + 1][brushSizeDoubled + 1];
		// prime the arrays
		for (int x = 0; x <= brushSizeDoubled; x++) {
			for (int z = 0; z <= brushSizeDoubled; z++) {
				boolean flag = true;
				for (int i = 0; i < 10; i++) { // overlay
					if (flag) {
						if ((this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i, blockPositionZ - brushSize + z) == 0 || this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i, blockPositionZ - brushSize + z) == Material.LEGACY_SNOW.getId()) && this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i - 1, blockPositionZ - brushSize + z) != Material.LEGACY_AIR.getId() && this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i - 1, blockPositionZ - brushSize + z) != Material.LEGACY_SNOW.getId()) {
							flag = false;
							yOffset[x][z] = i;
						}
					}
				}
				snowcone[x][z] = this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z);
				snowconeData[x][z] = this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z)
					.getData();
			}
		}
		// figure out new snowheights
		for (int x = 0; x <= brushSizeDoubled; x++) {
			double xSquared = Math.pow(x - brushSize, 2);
			for (int z = 0; z <= 2 * brushSize; z++) {
				double zSquared = Math.pow(z - brushSize, 2);
				double dist = Math.pow(xSquared + zSquared, 0.5); // distance from center of array
				int snowData = brushSize - (int) Math.ceil(dist);
				if (snowData >= 0) { // no funny business
					switch (snowData) {
						case 0:
							if (snowcone[x][z] == Material.LEGACY_AIR.getId()) {
								snowcone[x][z] = Material.LEGACY_SNOW.getId();
								snowconeData[x][z] = 0;
							}
							break;
						case 7: // Turn largest snowtile into snowblock
							if (snowcone[x][z] == Material.LEGACY_SNOW.getId()) {
								snowcone[x][z] = Material.LEGACY_SNOW_BLOCK.getId();
								snowconeData[x][z] = 0;
							}
							break;
						default: // Increase snowtile size, if smaller than target
							if (snowData > snowconeData[x][z]) {
								switch (snowcone[x][z]) {
									case 0:
										snowconeData[x][z] = snowData;
										snowcone[x][z] = Material.LEGACY_SNOW.getId();
										break;
									case 78:
										snowconeData[x][z] = snowData;
										break;
									default:
										break;
								}
							} else if (yOffset[x][z] > 0 && snowcone[x][z] == Material.LEGACY_SNOW.getId()) {
								snowconeData[x][z]++;
								if (snowconeData[x][z] == 7) {
									snowconeData[x][z] = 0;
									snowcone[x][z] = Material.LEGACY_SNOW_BLOCK.getId();
								}
							}
							break;
					}
				}
			}
		}
		Undo undo = new Undo();
		for (int x = 0; x <= brushSizeDoubled; x++) {
			for (int z = 0; z <= brushSizeDoubled; z++) {
				if (this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z) != snowcone[x][z] || this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z)
					.getData() != snowconeData[x][z]) {
					undo.put(this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z));
				}
				this.setBlockIdAt(blockPositionZ - brushSize + z, blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], snowcone[x][z]);
				this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z)
					.setData((byte) snowconeData[x][z]);
			}
		}
		v.getOwner()
			.storeUndo(undo);
	}

	@Override
	protected final void arrow(SnipeData snipeData) {
	}

	@Override
	protected final void powder(SnipeData snipeData) {
		switch (getTargetBlock().getType()) {
			case SNOW:
				this.addSnow(snipeData, this.getTargetBlock());
				break;
			default:
				Block blockAbove = getTargetBlock().getRelative(BlockFace.UP);
				if (blockAbove != null && blockAbove.getType() == Material.LEGACY_AIR) {
					addSnow(snipeData, blockAbove);
				} else {
					snipeData.getOwner()
						.getPlayer()
						.sendMessage(ChatColor.RED + "Error: Center block neither snow nor air.");
				}
				break;
		}
	}

	@Override
	public final void info(Message message) {
		message.brushName("Snow Cone");
	}

	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		if (parameters[1].equalsIgnoreCase("info")) {
			snipeData.sendMessage(ChatColor.GOLD + "Snow Cone Parameters:");
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.snowcone";
	}
}
