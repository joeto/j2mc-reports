package to.joe.j2mc.reports.command;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Core;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.reports.J2MC_Reports;
import to.joe.j2mc.reports.Report;

public class ReportHandlingCommand extends MasterCommand{

	J2MC_Reports plugin;
	public ReportHandlingCommand(J2MC_Reports Reports){
		super(Reports);
		this.plugin = Reports;
	}
	
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
    	if(isPlayer && player.hasPermission("j2mc.admin")){
            if (args.length == 0) {
                final ArrayList<Report> reps = plugin.Manager.getReports();
                final int size = reps.size();
                if (size == 0) {
                    player.sendMessage(ChatColor.RED + "No reports. Hurray!");
                    return;
                }
                player.sendMessage(ChatColor.DARK_PURPLE + "Found " + size + " reports:");
                for (final Report r : reps) {
                    if (!r.closed()) {
                        final Location location = r.getLocation();
                        final String x = ChatColor.GOLD.toString() + location.getBlockX() + ChatColor.DARK_PURPLE + ",";
                        final String y = ChatColor.GOLD.toString() + location.getBlockY() + ChatColor.DARK_PURPLE + ",";
                        final String z = ChatColor.GOLD.toString() + location.getBlockZ() + ChatColor.DARK_PURPLE;
                        player.sendMessage(ChatColor.DARK_PURPLE + "[" + r.getID() + "][" + x + y + z + "]<" + ChatColor.GOLD + r.getUser() + ChatColor.DARK_PURPLE + "> " + ChatColor.WHITE + r.getMessage());
                    }
                }
            }else {
                final String action = args[0].toLowerCase();
                if (action.equals("close")) {
                	if (args.length > 2) {
                        final int id = Integer.parseInt(args[1]);
                        if (id != 0) {
                        	plugin.Manager.closeReport(id, player.getName(), J2MC_Core.combineSplit(2, args, " "));
                        	player.sendMessage(ChatColor.DARK_PURPLE + "Report closed");
                        }
                    }else {
                        player.sendMessage(ChatColor.DARK_PURPLE + "/r close ID reason");
                    }
                }
            }
    	}
    }
}
