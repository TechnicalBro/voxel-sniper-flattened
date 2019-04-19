package com.thevoxelbox.voxelsniper.brush;

import java.util.Random;
import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.brush.perform.PerformBrush;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Splatter_Brushes
 *
 * @author Voxel
 */
public class SplatterBallBrush extends PerformBrush {

	private static final int GROW_PERCENT_MIN = 1;
	private static final int GROW_PERCENT_DEFAULT = 1000;
	private static final int GROW_PERCENT_MAX = 9999;
	private static final int SEED_PERCENT_MIN = 1;
	private static final int SEED_PERCENT_DEFAULT = 1000;
	private static final int SEED_PERCENT_MAX = 9999;
	private static final int SPLATREC_PERCENT_MIN = 1;
	private static final int SPLATREC_PERCENT_DEFAULT = 3;
	private static final int SPLATREC_PERCENT_MAX = 10;
	private int seedPercent; // Chance block on first pass is made active
	private int growPercent; // chance block on recursion pass is made active
	private int splatterRecursions; // How many times you grow the seeds
	private Random generator = new Random();

	/**
	 *
	 */
	public SplatterBallBrush() {
		this.setName("Splatter Ball");
	}

	private void splatterBall(SnipeData v, Block targetBlock) {
		if (this.seedPercent < SEED_PERCENT_MIN || this.seedPercent > SEED_PERCENT_MAX) {
			v.getOwner()
				.getPlayer()
				.sendMessage(ChatColor.BLUE + "Seed percent set to: 10%");
			this.seedPercent = SEED_PERCENT_DEFAULT;
		}
		if (this.growPercent < GROW_PERCENT_MIN || this.growPercent > GROW_PERCENT_MAX) {
			v.getOwner()
				.getPlayer()
				.sendMessage(ChatColor.BLUE + "Growth percent set to: 10%");
			this.growPercent = GROW_PERCENT_DEFAULT;
		}
		if (this.splatterRecursions < SPLATREC_PERCENT_MIN || this.splatterRecursions > SPLATREC_PERCENT_MAX) {
			v.getOwner()
				.getPlayer()
				.sendMessage(ChatColor.BLUE + "Recursions set to: 3");
			this.splatterRecursions = SPLATREC_PERCENT_DEFAULT;
		}
		int[][][] splat = new int[2 * v.getBrushSize() + 1][2 * v.getBrushSize() + 1][2 * v.getBrushSize() + 1];
		// Seed the array
		for (int x = 2 * v.getBrushSize(); x >= 0; x--) {
			for (int y = 2 * v.getBrushSize(); y >= 0; y--) {
				for (int z = 2 * v.getBrushSize(); z >= 0; z--) {
					if (this.generator.nextInt(SEED_PERCENT_MAX + 1) <= this.seedPercent) {
						splat[x][y][z] = 1;
					}
				}
			}
		}
		// Grow the seeds
		int gref = this.growPercent;
		int[][][] tempSplat = new int[2 * v.getBrushSize() + 1][2 * v.getBrushSize() + 1][2 * v.getBrushSize() + 1];
		for (int r = 0; r < this.splatterRecursions; r++) {
			this.growPercent = gref - ((gref / this.splatterRecursions) * (r));
			for (int x = 2 * v.getBrushSize(); x >= 0; x--) {
				for (int y = 2 * v.getBrushSize(); y >= 0; y--) {
					for (int z = 2 * v.getBrushSize(); z >= 0; z--) {
						tempSplat[x][y][z] = splat[x][y][z]; // prime tempsplat
						int growcheck = 0;
						if (splat[x][y][z] == 0) {
							if (x != 0 && splat[x - 1][y][z] == 1) {
								growcheck++;
							}
							if (y != 0 && splat[x][y - 1][z] == 1) {
								growcheck++;
							}
							if (z != 0 && splat[x][y][z - 1] == 1) {
								growcheck++;
							}
							if (x != 2 * v.getBrushSize() && splat[x + 1][y][z] == 1) {
								growcheck++;
							}
							if (y != 2 * v.getBrushSize() && splat[x][y + 1][z] == 1) {
								growcheck++;
							}
							if (z != 2 * v.getBrushSize() && splat[x][y][z + 1] == 1) {
								growcheck++;
							}
						}
						if (growcheck >= GROW_PERCENT_MIN && this.generator.nextInt(GROW_PERCENT_MAX + 1) <= this.growPercent) {
							tempSplat[x][y][z] = 1; // prevent bleed into splat
						}
					}
				}
			}
			// integrate tempsplat back into splat at end of iteration
			for (int x = 2 * v.getBrushSize(); x >= 0; x--) {
				for (int y = 2 * v.getBrushSize(); y >= 0; y--) {
					if (2 * v.getBrushSize() + 1 >= 0)
						System.arraycopy(tempSplat[x][y], 0, splat[x][y], 0, 2 * v.getBrushSize() + 1);
				}
			}
		}
		this.growPercent = gref;
		// Fill 1x1x1 holes
		for (int x = 2 * v.getBrushSize(); x >= 0; x--) {
			for (int y = 2 * v.getBrushSize(); y >= 0; y--) {
				for (int z = 2 * v.getBrushSize(); z >= 0; z--) {
					if (splat[Math.max(x - 1, 0)][y][z] == 1 && splat[Math.min(x + 1, 2 * v.getBrushSize())][y][z] == 1 && splat[x][Math.max(0, y - 1)][z] == 1 && splat[x][Math.min(2 * v.getBrushSize(), y + 1)][z] == 1) {
						splat[x][y][z] = 1;
					}
				}
			}
		}
		// Make the changes
		double rSquared = Math.pow(v.getBrushSize() + 1, 2);
		for (int x = 2 * v.getBrushSize(); x >= 0; x--) {
			double xSquared = Math.pow(x - v.getBrushSize() - 1, 2);
			for (int y = 2 * v.getBrushSize(); y >= 0; y--) {
				double ySquared = Math.pow(y - v.getBrushSize() - 1, 2);
				for (int z = 2 * v.getBrushSize(); z >= 0; z--) {
					if (splat[x][y][z] == 1 && xSquared + ySquared + Math.pow(z - v.getBrushSize() - 1, 2) <= rSquared) {
						this.current.perform(targetBlock.getRelative(-v.getBrushSize() + x, -v.getBrushSize() + y, -v.getBrushSize() + z));
					}
				}
			}
		}
		v.getOwner()
			.storeUndo(this.current.getUndo());
	}

	@Override
	protected final void arrow(SnipeData snipeData) {
		this.splatterBall(snipeData, this.getTargetBlock());
	}

	@Override
	protected final void powder(SnipeData snipeData) {
		this.splatterBall(snipeData, this.getLastBlock());
	}

	@Override
	public final void info(Message message) {
		if (this.seedPercent < SEED_PERCENT_MIN || this.seedPercent > SEED_PERCENT_MAX) {
			this.seedPercent = SEED_PERCENT_DEFAULT;
		}
		if (this.growPercent < GROW_PERCENT_MIN || this.growPercent > GROW_PERCENT_MAX) {
			this.growPercent = GROW_PERCENT_DEFAULT;
		}
		if (this.splatterRecursions < SPLATREC_PERCENT_MIN || this.splatterRecursions > SPLATREC_PERCENT_MAX) {
			this.splatterRecursions = SPLATREC_PERCENT_DEFAULT;
		}
		message.brushName("Splatter Ball");
		message.size();
		message.custom(ChatColor.BLUE + "Seed percent set to: " + this.seedPercent / 100 + "%");
		message.custom(ChatColor.BLUE + "Growth percent set to: " + this.growPercent / 100 + "%");
		message.custom(ChatColor.BLUE + "Recursions set to: " + this.splatterRecursions);
	}

	@Override
	public final void parameters(String[] parameters, SnipeData snipeData) {
		for (int i = 1; i < parameters.length; i++) {
			String parameter = parameters[i];
			if (parameter.equalsIgnoreCase("info")) {
				snipeData.sendMessage(ChatColor.GOLD + "Splatter Ball brush Parameters:");
				snipeData.sendMessage(ChatColor.AQUA + "/b sb s[int] -- set a seed percentage (1-9999). 100 = 1% Default is 1000");
				snipeData.sendMessage(ChatColor.AQUA + "/b sb g[int] -- set a growth percentage (1-9999).  Default is 1000");
				snipeData.sendMessage(ChatColor.AQUA + "/b sb r[int] -- set a recursion (1-10).  Default is 3");
				return;
			} else if (parameter.startsWith("s")) {
				double temp = Integer.parseInt(parameter.replace("s", ""));
				if (temp >= SEED_PERCENT_MIN && temp <= SEED_PERCENT_MAX) {
					snipeData.sendMessage(ChatColor.AQUA + "Seed percent set to: " + temp / 100 + "%");
					this.seedPercent = (int) temp;
				} else {
					snipeData.sendMessage(ChatColor.RED + "Seed percent must be an integer 1-9999!");
				}
			} else if (parameter.startsWith("g")) {
				double temp = Integer.parseInt(parameter.replace("g", ""));
				if (temp >= GROW_PERCENT_MIN && temp <= GROW_PERCENT_MAX) {
					snipeData.sendMessage(ChatColor.AQUA + "Growth percent set to: " + temp / 100 + "%");
					this.growPercent = (int) temp;
				} else {
					snipeData.sendMessage(ChatColor.RED + "Growth percent must be an integer 1-9999!");
				}
			} else if (parameter.startsWith("r")) {
				int temp = Integer.parseInt(parameter.replace("r", ""));
				if (temp >= SPLATREC_PERCENT_MIN && temp <= SPLATREC_PERCENT_MAX) {
					snipeData.sendMessage(ChatColor.AQUA + "Recursions set to: " + temp);
					this.splatterRecursions = temp;
				} else {
					snipeData.sendMessage(ChatColor.RED + "Recursions must be an integer 1-10!");
				}
			} else {
				snipeData.sendMessage(ChatColor.RED + "Invalid brush parameters! use the info parameter to display parameter info.");
			}
		}
	}

	@Override
	public String getPermissionNode() {
		return "voxelsniper.brush.splatterball";
	}
}
