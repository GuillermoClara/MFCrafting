package guillec.mfcrafting.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MFile extends YamlConfiguration {
    private JavaPlugin plugin;
    private final String fileName;

    public  MFile(JavaPlugin plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName;
        createFile();
    }

    public void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()){
                if (plugin.getResource(fileName) != null){
                    plugin.saveResource(fileName, false);
                }else{
                    save(file);
                }
            }else{
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save(){
        try {
            save(new File(plugin.getDataFolder(), fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
