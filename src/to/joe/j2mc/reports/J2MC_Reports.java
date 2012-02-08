package to.joe.j2mc.reports;

import org.bukkit.plugin.java.JavaPlugin;

public class J2MC_Reports extends JavaPlugin{

	public ReportsManager Manager;
	public void onEnable(){
		this.Manager = new ReportsManager();
		this.getLogger().info("Reports module enabled");
	}
	
	public void onDisable(){
		this.getLogger().info("Reports module disabled");
	}
	
}
