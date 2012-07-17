package to.joe.j2mc.reports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.event.MessageEvent;
import to.joe.j2mc.reports.command.AllReportsCommand;
import to.joe.j2mc.reports.command.ReportCommand;
import to.joe.j2mc.reports.command.ReportHandlingCommand;

public class J2MC_Reports extends JavaPlugin implements Listener {

    public ReportsManager Manager;

    @Override
    public void onDisable() {
        this.getLogger().info("Reports module disabled");
    }

    @Override
    public void onEnable() {
        this.Manager = new ReportsManager(this);
        this.Manager.LoadDataInitially();
        this.getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("report").setExecutor(new ReportCommand(this));
        this.getCommand("r").setExecutor(new ReportHandlingCommand(this));
        this.getCommand("rall").setExecutor(new AllReportsCommand(this));

        // Read reports from sql table every 10 seconds for bob
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Manager.LoadDataInitially();
            }
        }, 200, 200);

        this.getLogger().info("Reports module enabled");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("j2mc.reports.admin")) {
        	int open = 0;
        	try {
                final PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT count(*) FROM `reports` WHERE closed=0");
                final ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    open = rs.getInt(1);
                }
            } catch (final SQLException e) {
                event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Couldn't fetch all reports!");
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
            	event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Couldn't fetch all reports!");
                e.printStackTrace();
            }
        	
            event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Currently " + Manager.getReports().size() + " open reports on current server.");
            event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + String.valueOf(open-Manager.getReports().size()) + " open reports on other servers.");
        }
    }

    @EventHandler
    public void onIRCMessageEvent(MessageEvent event) {
        if (event.targetting("ReportCall")) {
            final int size = this.Manager.getReports().size();
            String response = "There are currently " + size + " reports open. ";
            switch (size) {
                case 0:
                    response += "\\o/";
                    break;
                case 1:
                    response += ":|";
                    break;
                case 2:
                    response += ":(";
                    break;
                case 3:
                    response += ":'(";
                    break;
                case 4:
                    response += "D:";
                    break;
                default:
                    response += "Seriously guys? Start cleaning up.";
                    break;
            }
            this.getServer().getPluginManager().callEvent(new MessageEvent(MessageEvent.compile("ADMININFO"), response));
        }
    }
}
