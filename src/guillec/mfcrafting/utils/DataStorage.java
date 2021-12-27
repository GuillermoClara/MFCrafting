package guillec.mfcrafting.utils;

import guillec.mfcrafting.MFCrafting;
import guillec.mfcrafting.files.MFile;
import guillec.mfcrafting.objects.CraftingType;
import guillec.mfcrafting.objects.CustomItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class DataStorage {
    public static HashMap<String, CustomItem> itemMap = new HashMap<>();
    public static HashMap<String, List<CustomItem>> typeMap = new HashMap<>();
    public static HashMap<String, CustomItem> itemViewingMap = new HashMap<>();
    public static HashMap<String, HashMap<String,Integer>> recipeViewingMap = new HashMap<>();
    public static HashMap<String, String> typeViewingMap = new HashMap<>();
    public static HashMap<String, Stack<Inventory>> recentInventory = new HashMap<>();
    public static HashMap<String, CraftingType> craftingTypeMap = new HashMap<>();

    public static void loadItems(MFCrafting plugin){
        MFile file = plugin.itemsFile;
        ConfigurationSection cs = file.getConfigurationSection("data");
        if(cs==null)

            return;

        for(String key : cs.getKeys(false)){
            CustomItem item = CustomItem.deserialize(cs.getConfigurationSection(key).getValues(false));
            itemMap.put(key,item);
            item.setId(key);
        }


    }

    /*
     public static void loadTypes2(MFCrafting plugin){
        ConfigurationSection cs = plugin.typesFile.getConfigurationSection("data");
        if(cs==null || cs.getKeys(false).isEmpty() ){
            String default_type = plugin.getConfig().getString("default_type");
            List<CustomItem> items = new ArrayList<>();
            for(HashMap.Entry<String,CustomItem> entry: itemMap.entrySet()){
                CustomItem customItem = entry.getValue();
                customItem.setType(default_type);
                items.add(customItem);
            }
            DataStorage.typeMap.put(default_type,items);
            return;
        }

        for(String type : cs.getKeys(false)){
            List<CustomItem> items = new ArrayList<>();
            List<String> ids = plugin.typesFile.getStringList("data."+type+".items");
            for(String id : ids){
                CustomItem item = DataStorage.itemMap.get(id);
                items.add(item);
            }
            System.out.println("Metiendo arraylist en tipo "+type);

            DataStorage.typeMap.put(type,items);
            System.out.println(items.size());
        }
    }
     */


    public static void loadTypes(MFCrafting plugin){

        ConfigurationSection cs = plugin.typesFile.getConfigurationSection("data");
        if(cs==null || cs.getKeys(false).isEmpty() ){
            String default_type = plugin.getConfig().getString("default_type");
            List<CustomItem> items = new ArrayList<>();
            CraftingType type = new CraftingType(plugin.typesFile,default_type,"crafting",true,items);
            DataStorage.craftingTypeMap.put(default_type,type);

        } else {

            for(String typeKey : cs.getKeys(false)){

                CraftingType type = new CraftingType(plugin.typesFile,typeKey);
                DataStorage.craftingTypeMap.put(typeKey,type);



            }


        }


    }


    public static void saveItems(MFCrafting plugin){
        MFile file = plugin.itemsFile;

        for(Map.Entry<String,CustomItem> entry : itemMap.entrySet()){
            CustomItem item = entry.getValue();

            try{
                file.set("data."+item.getId(),item.serialize());
            } catch(NullPointerException e){
                file.set("data."+entry.getKey(),null);
            }

        }
        file.save();
    }

    public static void saveTypes(MFCrafting plugin){

        for(Map.Entry<String,CraftingType> entry : craftingTypeMap.entrySet()){
            CraftingType type = entry.getValue();
            if(type!=null)
                type.save();




        }
        plugin.typesFile.save();



    }

    public static void saveTypes2(MFCrafting plugin){
        for(Map.Entry<String,List<CustomItem>> entry : typeMap.entrySet()){
            List<String> ids = new ArrayList<>();
            List<CustomItem> items = entry.getValue();
            for(CustomItem item : items)
                ids.add(item.getId());

            plugin.typesFile.set("data."+entry.getKey()+".items",ids);
            plugin.typesFile.save();



        }

    }




}
