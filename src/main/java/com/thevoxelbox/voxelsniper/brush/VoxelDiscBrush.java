package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.brush.perform.PerformBrush;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Voxel_Disc_Brush
 *
 * @author Voxel
 */
public class VoxelDiscBrush extends PerformBrush {

	public VoxelDiscBrush() {
		this.setName("Voxel Disc");
	}

	private void disc(SnipeData snipeData, Block targetBlock) {
		for (int x = snipeData.getBrushSize(); x >= -snipeData.getBrushSize(); x--) {
			for (int z = snipeData.getBrushSize(); z >= -snipeData.getBrushSize(); z--) {
				this.current.perform(targetBlock.getRelative(x, 0, z));
			}
		}
		snipeData.getOwner()
			.storeUndo(this.current.getUndo());
	}

	@Override
	protected final void arrow(SnipeData snipeData) {
		this.disc(snipeData, this.getTargetBlock());
	}

	@Override
	protected final void powder(SnipeData snipeData) {
		this.disc(snipeData, this.getLastBlock());
	}

	@Override
	public final void info(Message message) {
		message.brushName(this.getName());
		message.size();
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.voxeldisc";
	}
}
