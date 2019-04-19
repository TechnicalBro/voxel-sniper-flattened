/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import org.bukkit.block.Block;

/**
 * @author Voxel
 */
public class ComboNoUndoPerformer extends AbstractPerformer {

	private int i;
	private byte d;

	public ComboNoUndoPerformer() {
		this.setName("Combo, No-Undo"); // made name more descriptive - Giltwist
	}

	@Override
	public void init(com.thevoxelbox.voxelsniper.SnipeData snipeData) {
		this.world = snipeData.getWorld();
		this.i = snipeData.getVoxelId();
		this.d = snipeData.getData();
	}

	@Override
	public void info(Message message) {
		message.performerName(this.getName());
		message.voxel();
		message.data();
	}

	@Override
	public void perform(Block block) {
		if (block.getTypeId() != this.i || block.getData() != this.d) {
			block.setTypeIdAndData(this.i, this.d, true);
		}
	}
}
