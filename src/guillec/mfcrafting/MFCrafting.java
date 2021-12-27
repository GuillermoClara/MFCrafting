package guillec.mfcrafting;

import guillec.mfcrafting.commands.Edit;
import guillec.mfcrafting.files.MFile;
import guillec.mfcrafting.listener.InventoryListener;
import guillec.mfcrafting.utils.DataStorage;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MFCrafting extends JavaPlugin {
    public String configPath;
    public MFile itemsFile = new MFile(this,"items.yml");
    public MFile typesFile = new MFile(this, "types.yml");
    public MFile langFile = new MFile(this,"lang.yml");

    public void onEnable(){
            getLogger().info("Iniciando...");
            configRegister();
            DataStorage.loadItems(this);
            DataStorage.loadTypes(this);
            PluginManager pm= getServer().getPluginManager();
            pm.registerEvents(new InventoryListener(this),this);
            this.getCommand("mfcrafting").setExecutor(new Edit(this));

    }

    public void onDisable(){
        DataStorage.saveItems(this);
        DataStorage.saveTypes(this);
        getLogger().info("Desactivando...");
    }

    public void configRegister() {
        File config = new File(getDataFolder(), "config.yml");
        this.configPath = config.getPath();
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }


}
