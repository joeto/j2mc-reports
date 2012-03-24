package to.joe.j2mc.reports;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        this.Manager.LoadDataIntially();
        this.getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("report").setExecutor(new ReportCommand(this));
        this.getCommand("r").setExecutor(new ReportHandlingCommand(this));
        this.getCommand("rall").setExecutor(new AllReportsCommand(this));

        this.getLogger().info("Reports module enabled");
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
