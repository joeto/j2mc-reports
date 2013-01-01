package to.joe.j2mc.reports.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.reports.J2MC_Reports;

public class AllReportsCommand extends MasterCommand<J2MC_Reports> {

    public AllReportsCommand(J2MC_Reports Reports) {
        super(Reports);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        try {
            final PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM `reports` WHERE closed=0");
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final int server = rs.getInt("server");
                final int id = rs.getInt("id");
                final int x = (int) Math.round(rs.getDouble("x"));
                final int y = (int) Math.round(rs.getDouble("y"));
                final int z = (int) Math.round(rs.getDouble("z"));
                final String user = rs.getString("user");
                final String message = rs.getString("message");
                sender.sendMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.WHITE + "mc" + server + ChatColor.DARK_PURPLE + "][" + ChatColor.WHITE + id + ChatColor.DARK_PURPLE + "][" + ChatColor.GOLD + x + "," + y + "," + z + ChatColor.DARK_PURPLE + "]" + ChatColor.LIGHT_PURPLE + "<" + ChatColor.GOLD + user + ChatColor.LIGHT_PURPLE + "> " + ChatColor.WHITE + message);
            }
        } catch (final SQLException e) {
            sender.sendMessage("Error fetching reports");
            e.printStackTrace();
        }

    }
}
