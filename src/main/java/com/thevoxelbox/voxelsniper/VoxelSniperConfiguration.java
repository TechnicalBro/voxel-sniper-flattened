package com.thevoxelbox.voxelsniper;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

/**
 * Configuration storage defining global configurations for VoxelSniper.
 */
public class VoxelSniperConfiguration
{
    public static final String CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE = "litesniper-max-brush-size";
    public static final String CONFIG_IDENTIFIER_UNDO_CACHE_SIZE = "undo-cache-size";
    public static final String CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS = "litesniper-restricted-items";
    public static final int DEFAULT_LITESNIPER_MAX_BRUSH_SIZE = 5;
    public static final int DEFAULT_UNDO_CACHE_SIZE = 20;
    private FileConfiguration configuration;

    /**
     * @param configuration Configuration that is going to be used.
     */
    public VoxelSniperConfiguration(FileConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Returns the maximum amount of snipes stored in the undo cache of snipers.
     *
     * @return the maximum amount of snipes stored in the undo cache of snipers
     */
    public int getUndoCacheSize()
    {
        return configuration.getInt(CONFIG_IDENTIFIER_UNDO_CACHE_SIZE, DEFAULT_UNDO_CACHE_SIZE);
    }

    /**
     * Set the maximum amount of snipes stored in the undo cache of snipers.
     *
     * @param size size of undo cache
     */
    public void setUndoCacheSize(int size)
    {
        configuration.set(CONFIG_IDENTIFIER_UNDO_CACHE_SIZE, size);
    }

    /**
     * Returns maximum size of brushes that LiteSnipers can use.
     *
     * @return maximum size
     */
    public int getLiteSniperMaxBrushSize()
    {
        return configuration.getInt(CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE, DEFAULT_LITESNIPER_MAX_BRUSH_SIZE);
    }

    /**
     * Set maximum size of brushes that LiteSnipers can use.
     *
     * @param size maximum size
     */
    public void setLiteSniperMaxBrushSize(int size)
    {
        configuration.set(CONFIG_IDENTIFIER_LITESNIPER_MAX_BRUSH_SIZE, size);
    }

    /**
     * Returns List of restricted Litesniper Items.
     *
     * @return List of restricted Litesniper Items
     */
    public List<Integer> getLiteSniperRestrictedItems()
    {
        return configuration.getIntegerList(CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS);
    }

    /**
     * Set new list of restricted Litesniper Items.
     *
     * @param restrictedItems List of restricted Litesniper Items
     */
    public void setLitesniperRestrictedItems(List<Integer> restrictedItems)
    {
        Preconditions.checkNotNull(restrictedItems, "Restricted items must be a list.");
        configuration.set(CONFIG_IDENTIFIER_LITESNIPER_RESTRICTED_ITEMS, restrictedItems);
    }
}
