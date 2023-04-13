package fr.lavapower.eogendlocks.listener;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.EnderChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LockListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock()) {
            Block block = event.getClickedBlock();
            if(block.getBlockData().getMaterial() != Material.ENDER_CHEST && block.getBlockData().getMaterial().toString().contains("CHEST")) {
                Chest chest = (Chest) block.getState();
                ItemStack firstItem = chest.getBlockInventory().getItem(0);
                if(!isValid(event.getItem(), firstItem) && !event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("Un verrouillage est présent.");
                }
            }
            else if(block.getBlockData().getMaterial().toString().contains("SHULKER_BOX")) {
                ShulkerBox chest = (ShulkerBox) block.getState();
                ItemStack firstItem = chest.getInventory().getItem(0);
                if(!isValid(event.getItem(), firstItem) && !event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("Un verrouillage est présent.");
                }
            }
            else if((block.getBlockData() instanceof Openable && !((Openable)block.getState().getBlockData()).isOpen())) {
                Location location = block.getLocation().clone();
                location.subtract(0, 2, 0);
                if(block.getBlockData() instanceof Door door && door.getHalf() == Bisected.Half.TOP)
                    location.subtract(0, 1, 0);
                if(location.getBlock().getBlockData().getMaterial().toString().contains("CHEST")) {
                    Chest chest = (Chest) location.getBlock().getState();
                    ItemStack firstItem = chest.getBlockInventory().getItem(0);
                    if(!isValid(event.getItem(), firstItem) && !event.getPlayer().isOp()) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("Un verrouillage est présent.");
                    }
                }
            }
        }
    }

    private boolean isValid(ItemStack playerItem, ItemStack firstItem) {
        if(playerItem != null && playerItem.hasItemMeta() && playerItem.getType() == Material.TRIPWIRE_HOOK && playerItem.getItemMeta().getDisplayName().contains("Clé") &&
                firstItem != null && firstItem.hasItemMeta() && firstItem.getType() == Material.PAPER) {

            String a = ChatColor.stripColor(NBT.get(playerItem, nbt -> nbt.getString("key")));
            String b = ChatColor.stripColor(firstItem.getItemMeta().getDisplayName());
            return a.equals(b);
        }
        return firstItem == null || !firstItem.hasItemMeta() || firstItem.getType() != Material.PAPER;
    }
}
