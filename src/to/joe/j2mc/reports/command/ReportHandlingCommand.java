package to.joe.j2mc.reports.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Core;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.reports.J2MC_Reports;
import to.joe.j2mc.reports.Report;

public class ReportHandlingCommand extends MasterCommand<J2MC_Reports> {

    private static final List<String> POTENTIAL_ARGUMENTS = new ArrayList<String>();
    static {
        POTENTIAL_ARGUMENTS.add("close");
        POTENTIAL_ARGUMENTS.add("tp");
        POTENTIAL_ARGUMENTS.add("massclose");
    }

    public ReportHandlingCommand(J2MC_Reports Reports) {
        super(Reports);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (args.length == 0) {
            final ArrayList<Report> reps = this.plugin.Manager.getReports();
            final int size = reps.size();
            if (size == 0) {
                sender.sendMessage(ChatColor.RED + "No reports. Hurray!");
                return;
            }
            sender.sendMessage(ChatColor.DARK_PURPLE + "Found " + size + " reports:");
            for (final Report r : reps) {
                if (!r.closed()) {
                    final Location location = r.getLocation();
                    final String x = ChatColor.GOLD.toString() + location.getBlockX() + ChatColor.DARK_PURPLE + ",";
                    final String y = ChatColor.GOLD.toString() + location.getBlockY() + ChatColor.DARK_PURPLE + ",";
                    final String z = ChatColor.GOLD.toString() + location.getBlockZ() + ChatColor.DARK_PURPLE;
                    sender.sendMessage(ChatColor.DARK_PURPLE + "[" + r.getID() + "][" + x + y + z + "]<" + ChatColor.GOLD + r.getUser() + ChatColor.DARK_PURPLE + "> " + ChatColor.WHITE + r.getMessage());
                }
            }
        } else {
            final String action = args[0].toLowerCase();
            if (action.equalsIgnoreCase("close")) {
                if (args.length > 1) {
                    final int id;
                    try {
                        id = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "ID needs to be a number you dolt");
                        return;
                    }
                    String reason = "";
                    if (args.length > 2) {
                        reason = J2MC_Core.combineSplit(2, args, " ");
                    } else {
                        reason = "Closed";
                    }
                    if (id != 0 && (this.plugin.Manager.getReport(id) != null)) {
                        this.plugin.Manager.closeReport(id, sender.getName(), reason);
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Report closed");
                    } else {
                        sender.sendMessage(ChatColor.RED + "No such report");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "/r close ID [reason]");
                }
            }
            if (action.equalsIgnoreCase("tp")) {
                if (isPlayer) {
                    if (args.length > 1) {
                        final int id;
                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "ID needs to be a number you dolt");
                            return;
                        }
                        final Report report = this.plugin.Manager.getReport(id);
                        if (report != null) {
                            player.teleport(report.getLocation());
                            player.sendMessage(ChatColor.DARK_PURPLE + "Wheeeeeeeee");
                        } else {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Report not found");
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_PURPLE + "/r tp ID");
                    }
                }
            }
            if (action.equalsIgnoreCase("massclose")) {
                if (args.length > 1) {
                    if (this.isInteger(args[1])) {
                        if (args.length > 1) {
                            ArrayList<Integer> reports = new ArrayList<Integer>();
                            ArrayList<String> argsAsList = new ArrayList<String>(Arrays.asList(args));
                            argsAsList.remove(0);
                            int endReportIndex = 1;
                            for (String arg : argsAsList) {
                                if (this.isInteger(arg)) {
                                    reports.add(Integer.parseInt(arg));
                                    endReportIndex++;
                                } else {
                                    break;
                                }
                            }
                            String reason;
                            if (args.length > endReportIndex) {
                                reason = J2MC_Core.combineSplit(endReportIndex + 1, args, " ");
                            } else {
                                reason = "Closed";
                            }
                            for (Report report : this.plugin.Manager.getReports()) {
                                if (reports.contains(report.getID())) {
                                    this.plugin.Manager.closeReport(report.getID(), sender.getName(), reason);
                                }
                            }
                            StringBuilder builder = new StringBuilder();
                            builder.append(ChatColor.DARK_PURPLE + "Mass closed reports ");
                            for (int id : reports) {
                                builder.append(id + ", ");
                            }
                            builder.setLength(builder.length() - 2);
                            sender.sendMessage(builder.toString());
                            return;
                        } else {
                            sender.sendMessage(ChatColor.DARK_PURPLE + "/r massclose id1 id2 id3 [reason]");
                            sender.sendMessage(ChatColor.DARK_PURPLE + "Example usage: /r massclose 15 16 17 18 handled by dog");
                        }
                    }
                    if (args.length > 1) {
                        String reason;
                        if (args.length > 2) {
                            reason = J2MC_Core.combineSplit(2, args, " ");
                        } else {
                            reason = "Closed";
                        }
                        for (Report report : this.plugin.Manager.getReports()) {
                            if (report.getUser().equalsIgnoreCase(args[1])) {
                                this.plugin.Manager.closeReport(report.getID(), sender.getName(), reason);
                            }
                        }
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Mass closed all reports by " + args[1]);
                    } else {
                        sender.sendMessage(ChatColor.DARK_PURPLE + "/r massclose <user> [reason]");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "Usage: /r massclose <user> [reason] " + ChatColor.AQUA + " OR");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "/r massclose id1 id2 id3 [reason]");
                    sender.sendMessage(ChatColor.DARK_PURPLE + "Example usage: /r massclose 15 16 17 18 handled by dog");
                }
            }
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> matches = new ArrayList<String>();
        
        if (args.length == 1) {                        
            for (String argument : POTENTIAL_ARGUMENTS) {
                if (argument.startsWith(args[0])) {
                    matches.add(argument);
                }
            }
            
            return matches;
        }
        
        if (args.length == 2 && !(args[0].equalsIgnoreCase("massclose"))) {
            for (Report report : plugin.Manager.getReports()) {
                if (Integer.toString(report.getID()).startsWith(args[1])) {
                    matches.add(Integer.toString(report.getID()));
                }
            }
            
            Collections.sort(matches);
            return matches;
        }
        
        return matches;
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
