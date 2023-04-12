package fr.lavapower.eogendlocks.command;

import de.tr7zw.changeme.nbtapi.NBT;
import fr.lavapower.eogendlocks.EogendLocks;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LockCommand implements CommandExecutor, TabCompleter {
    private final EogendLocks plugin;

    public LockCommand(EogendLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player))
        {
            sender.sendMessage("Cette commande n'est utilisable que par un joueur !");
            return true;
        }

        if(args.length >= 1) {
            ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("Clé");
            item.setItemMeta(meta);
            NBT.modify(item, nbt -> {
                nbt.setString("key", String.join(" ", args));
            });
            player.getInventory().addItem(item);
            sender.sendMessage("Clé donnée !");
            plugin.getLockLogger().info(player.getDisplayName() + " - GIVE KEY : " + String.join(" ", args));
        }
        else
            sender.sendMessage("Usage : /lock <text>");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
