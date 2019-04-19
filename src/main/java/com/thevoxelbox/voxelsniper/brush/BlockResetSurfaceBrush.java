package com.thevoxelbox.voxelsniper.brush;

import java.util.ArrayList;
import java.util.List;
import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * This brush only looks for solid blocks, and then changes those plus any air blocks touching them. If it works, this brush should be faster than the original
 * blockPositionY an amount proportional to the volume of a snipe selection area / the number of blocks touching air in the selection. This is because every solid block
 * surrounded blockPositionY others should take equally long to check and not change as it would take MC to change them and then check and find no lighting to update. For
 * air blocks surrounded blockPositionY other air blocks, this brush saves about 80-100 checks blockPositionY not updating them or their lighting. And for air blocks touching solids,
 * this brush is slower, because it replaces the air once per solid block it is touching. I assume on average this is about 2 blocks. So every air block
 * touching a solid negates one air block floating in air. Thus, for selections that have more air blocks surrounded blockPositionY air than air blocks touching solids,
 * this brush will be faster, which is almost always the case, especially for undeveloped terrain and for larger brush sizes (unlike the original brush, this
 * should only slow down blockPositionY the square of the brush size, not the cube of the brush size). For typical terrain, blockPositionY my calculations, overall speed increase is
 * about a factor of 5-6 for a size 20 brush. For a complicated city or ship, etc., this may be only a factor of about 2. In a hypothetical worst case scenario
 * of a 3d checkerboard of stone and air every other block, this brush should only be about 1.5x slower than the original brush. Savings increase for larger
 * brushes.
 *
 * @author GavJenks
 */
public class BlockResetSurfaceBrush extends AbstractBrush {

	private static final List<Material> DENIED_UPDATES = new ArrayList<>();

	static {
		DENIED_UPDATES.add(Material.LEGACY_SIGN);
		DENIED_UPDATES.add(Material.LEGACY_SIGN_POST);
		DENIED_UPDATES.add(Material.LEGACY_WALL_SIGN);
		DENIED_UPDATES.add(Material.LEGACY_CHEST);
		DENIED_UPDATES.add(Material.LEGACY_FURNACE);
		DENIED_UPDATES.add(Material.LEGACY_BURNING_FURNACE);
		DENIED_UPDATES.add(Material.LEGACY_REDSTONE_TORCH_OFF);
		DENIED_UPDATES.add(Material.LEGACY_REDSTONE_TORCH_ON);
		DENIED_UPDATES.add(Material.LEGACY_REDSTONE_WIRE);
		DENIED_UPDATES.add(Material.LEGACY_DIODE_BLOCK_OFF);
		DENIED_UPDATES.add(Material.LEGACY_DIODE_BLOCK_ON);
		DENIED_UPDATES.add(Material.LEGACY_WOODEN_DOOR);
		DENIED_UPDATES.add(Material.LEGACY_WOOD_DOOR);
		DENIED_UPDATES.add(Material.LEGACY_IRON_DOOR);
		DENIED_UPDATES.add(Material.LEGACY_IRON_DOOR_BLOCK);
		DENIED_UPDATES.add(Material.LEGACY_FENCE_GATE);
		DENIED_UPDATES.add(Material.LEGACY_AIR);
	}

	/**
	 *
	 */
	public BlockResetSurfaceBrush() {
		this.setName("Block Reset Brush Surface Only");
	}

	@SuppressWarnings("deprecation")
	private void applyBrush(SnipeData v) {
		World world = this.getWorld();
		for (int z = -v.getBrushSize(); z <= v.getBrushSize(); z++) {
			for (int x = -v.getBrushSize(); x <= v.getBrushSize(); x++) {
				for (int y = -v.getBrushSize(); y <= v.getBrushSize(); y++) {
					Block block = world.getBlockAt(this.getTargetBlock()
						.getX() + x, this.getTargetBlock()
						.getY() + y, this.getTargetBlock()
						.getZ() + z);
					if (DENIED_UPDATES.contains(block.getType())) {
						continue;
					}
					boolean airFound = false;
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x + 1, this.getTargetBlock()
						.getY() + y, this.getTargetBlock()
						.getZ() + z)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x + 1, this.getTargetBlock()
							.getY() + y, this.getTargetBlock()
							.getZ() + z);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x - 1, this.getTargetBlock()
						.getY() + y, this.getTargetBlock()
						.getZ() + z)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x - 1, this.getTargetBlock()
							.getY() + y, this.getTargetBlock()
							.getZ() + z);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x, this.getTargetBlock()
						.getY() + y + 1, this.getTargetBlock()
						.getZ() + z)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY() + y + 1, this.getTargetBlock()
							.getZ() + z);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x, this.getTargetBlock()
						.getY() + y - 1, this.getTargetBlock()
						.getZ() + z)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY() + y - 1, this.getTargetBlock()
							.getZ() + z);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x, this.getTargetBlock()
						.getY() + y, this.getTargetBlock()
						.getZ() + z + 1)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY() + y, this.getTargetBlock()
							.getZ() + z + 1);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (world.getBlockAt(this.getTargetBlock()
						.getX() + x, this.getTargetBlock()
						.getY() + y, this.getTargetBlock()
						.getZ() + z - 1)
						.getTypeId() == 0) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY() + y, this.getTargetBlock()
							.getZ() + z - 1);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
						airFound = true;
					}
					if (airFound) {
						Block blockAt = world.getBlockAt(this.getTargetBlock()
							.getX() + x, this.getTargetBlock()
							.getY() + y, this.getTargetBlock()
							.getZ() + z);
						byte oldData = blockAt.getData();
						resetBlock(blockAt, oldData);
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void resetBlock(Block block, byte oldData) {
		block.setTypeIdAndData(block.getTypeId(), (byte) ((block.getData() + 1) & 0xf), true);
		block.setTypeIdAndData(block.getTypeId(), oldData, true);
	}

	@Override
	protected final void arrow(SnipeData snipeData) {
		applyBrush(snipeData);
	}

	@Override
	protected final void powder(SnipeData snipeData) {
		applyBrush(snipeData);
	}

	@Override
	public final void info(Message message) {
		message.brushName(this.getName());
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.blockresetsurface";
	}
}
