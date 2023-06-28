package org.sweetrazory.waystonesplus.eventhandlers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.sweetrazory.waystonesplus.Main;
import org.sweetrazory.waystonesplus.items.WaystoneSummonItem;
import org.sweetrazory.waystonesplus.memoryhandlers.WaystoneMemory;

import java.util.List;

public class OnWaystoneBreak implements Listener {
    public OnWaystoneBreak(WaystoneMemory waystoneMemory, BlockBreakEvent event) {
        try {
            Block block = event.getBlock();
            List<MetadataValue> blockMeta = block.getMetadata("waystoneId");
            List<MetadataValue> blockWaystoneTypeList = block.getMetadata("waystoneType");
            if (!blockWaystoneTypeList.isEmpty() && !blockMeta.isEmpty()) {
                String blockWaystoneType = blockWaystoneTypeList.get(0).asString();
                if (!blockMeta.isEmpty() && WaystoneMemory.getWaystoneTypes().containsKey(blockWaystoneType)) {
                    if (event.getPlayer().hasPermission("waystonesplus.breakwaystone") || event.getPlayer().isOp()) {
                        Location dropLocation = event.getPlayer().getTargetBlock(null, 5).getLocation().add(0, 1, 0);
                        World world = event.getPlayer().getWorld();
                        String waystoneId = blockMeta.get(0).asString();
                        String waystoneName = WaystoneMemory.getWaystoneDataMemory().get(waystoneId).getName();
                        ItemStack skullItem = new WaystoneSummonItem().getLodestoneHead(waystoneName, blockWaystoneType, null, null);
                        world.dropItemNaturally(dropLocation, skullItem);
                        waystoneMemory.removeWaystone(waystoneId);
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        } catch (IndexOutOfBoundsException error) {
            Main.getInstance().getLogger().warning("A non-fatal error occurred.");
        }
    }
}
