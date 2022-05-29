package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  @Subcommand("withdraw")
  public void onWithdraw(CommandSender sender, double hearts) {
    Player player = (Player) sender;
    if (hearts >= LifeStealPlugin.getLifeSteal().getUtils().getPlayerBaseHealth(player)) {
      LifeStealPlugin.getLifeSteal().getMessenger().sendMessage(player, "Chutiye, aukat hai tera itna?");
    } else {
      LifeStealPlugin.getLifeSteal().getUtils().setPlayerBaseHealth(player, LifeStealPlugin.getLifeSteal().getUtils().getPlayerBaseHealth(player) - hearts);
      //      TODO - Drop heart at @player 's position
    }
  }
}
