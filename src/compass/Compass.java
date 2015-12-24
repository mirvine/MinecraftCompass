package compass;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.lang.Exception;
import java.lang.Error;



public class Compass extends JavaPlugin implements Listener{
  public static Logger log = Logger.getLogger("Minecraft");
  private final String QUEUE_NAME = "compass";
  private Channel channel;
  private Connection connection = null;
  private String message = "test";

  public void onEnable() {
    log.info("[Compass] Start up.");
    log.info("[Compass] trying connect0");
    ConnectionFactory factory = new ConnectionFactory();
    log.info("[Compass] trying connect1");
    factory.setHost("localhost");
    log.info("[Compass] trying connect2");
    try{
      connection = factory.newConnection();
      log.info("[Compass] trying connect2");
      channel = connection.createChannel();
      log.info("[Compass] trying connect4");
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      log.info("[Compass] trying connect4.1");
    }
    catch (Exception e){
      e.printStackTrace();
    }
    catch (Error e){
      e.printStackTrace();
    }

    getServer().getPluginManager().registerEvents(this, this);
  }
  public void onReload() {
    log.info("[Compass] Server reloaded.");
  }
  public void onDisable() {
    log.info("[Compass] Server stopping.");
  }

  public String getDirection(Player player) {
    //log.info("[Compass] Server stopping.");

    double rotation = (player.getLocation().getYaw() - 90) % 360;
    if (rotation < 0) {
        rotation += 360.0;
    }

    return String.valueOf(rotation);
  }

  @EventHandler
    public void onMove(PlayerMoveEvent event) {
      final Player player = event.getPlayer();
      try{
        //this.channel.basicPublish("", "compass", null, this.message.getBytes("UTF-8"));
        String msg = this.getDirection(player);
        this.channel.basicPublish("", "compass", null, msg.getBytes("UTF-8"));
      }catch (Exception e){
        e.printStackTrace();
      }
    }
}
