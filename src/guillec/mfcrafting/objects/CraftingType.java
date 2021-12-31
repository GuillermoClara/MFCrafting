package guillec.mfcrafting.objects;

import guillec.mfcrafting.MFCrafting;
import guillec.mfcrafting.files.MFile;
import guillec.mfcrafting.utils.DataStorage;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingType {
    private String type;
    private boolean visible;
    private List<CustomItem> items = new ArrayList<>();
    private CustomItem icon;
    private String name;
    private final MFile file;

    public CraftingType(MFile file, String name){

        this.file = file;
        this.name = name;

        String path = "data."+name;
        this.type = file.getString(path+".type");
        this.visible = file.getBoolean(path+".visible");

        CustomItem iconItem = DataStorage.itemMap.get(file.getString(path+".icon"));
        if(iconItem != null)
            this.icon = iconItem;



        for(String itemId : file.getStringList(path+".items")){

            CustomItem customItem = DataStorage.itemMap.get(itemId);
            if(customItem!=null)
                this.items.add(customItem);
        }

    }

    public CraftingType(MFile file, String name,String type, boolean visible, List<CustomItem> items){
        this.file = file;
        this.name = name;
        this.type = type;
        this.visible = visible;
        this.items = items;
    }

    public CraftingType(MFile file, String name,String type, boolean visible){
        this.file = file;
        this.name = name;
        this.type = type;
        this.visible = visible;
    }



    public void save(){

        String path = "data."+this.name;
        file.set(path+".visible",this.visible);
        file.set(path+".type",this.type);
        List<String> ids = new ArrayList<>();
        for(CustomItem item: items){
            if(item!=null){
                ids.add(item.getId());
            }
        }
        file.set(path+".items",ids);
        if(this.icon!=null)
            file.set(path+".icon",this.icon.getId());
    }








    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public List<CustomItem> getItems(){
        return this.items;
    }

    public void setItems(List<CustomItem> items){
        this.items = items;
    }

    public boolean isVisible(){
        return this.visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public CustomItem getIcon(){
        return this.icon;
    }

    public void setIcon(CustomItem item){
        this.icon = item;
    }




}
