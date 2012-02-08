package to.joe.j2mc.reports;

import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.reports.command.ReportCommand;
import to.joe.j2mc.reports.command.ReportHandlingCommand;

public class J2MC_Reports extends JavaPlugin{

	public ReportsManager Manager;
	public void onEnable(){
		this.Manager = new ReportsManager(this);
		Manager.LoadDataIntially();
		
		this.getCommand("report").setExecutor(new ReportCommand(this));
		this.getCommand("r").setExecutor(new ReportHandlingCommand(this));
		
		this.getLogger().info("Reports module enabled");
	}
	
	public void onDisable(){
		this.getLogger().info("Reports module disabled");
	}
	
}
