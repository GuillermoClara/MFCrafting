package guillec.mfcrafting.commands;

import guillec.mfcrafting.MFCrafting;
import guillec.mfcrafting.objects.CraftingType;
import guillec.mfcrafting.objects.CustomItem;
import guillec.mfcrafting.utils.DataStorage;
import guillec.mfcrafting.utils.VariedTools;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edit implements CommandExecutor {
    private final MFCrafting plugin;

    public Edit(MFCrafting plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player) || sender.isOp()){

            try{
                String option = args[0];
                switch(option){
                    case "items":
                    case "item":
                        this.itemMenu(args,sender);
                        return true;
                    case "recipes":
                    case "recipe":
                        this.recipeMenu(args,sender);
                        return true;
                    case "type":
                        this.typeMenu(args,sender);
                        return true;
                    case "settings":
                        this.settingsMenu(args,sender);
                        return true;
                    default:
                        sender.sendMessage(ChatColor.GREEN+"/mfcrafting item help");
                        sender.sendMessage(ChatColor.GREEN+"/mfcrafting recipe help");
                        sender.sendMessage(ChatColor.GREEN+"/mfcrafting type help");
                        sender.sendMessage(ChatColor.GREEN+"/mfcrafting settings help");
                        return true;
                }


            } catch(IndexOutOfBoundsException e){
                sender.sendMessage(ChatColor.GREEN+"/mfcrafting item help");
                sender.sendMessage(ChatColor.GREEN+"/mfcrafting recipe help");
                sender.sendMessage(ChatColor.GREEN+"/mfcrafting type help");
                System.out.println(e.getMessage()+e.getLocalizedMessage());
                return true;
            }


        } else {
            sender.sendMessage(ChatColor.RED+"Permisos insuficientes");
            return true;
        }
    }



    public void typeMenu2(String[] args, CommandSender sender){
        String option = args[1];
        switch(option){
            case "list":
            {
                List<String> types = new ArrayList<>(DataStorage.typeMap.keySet());
                if(types.isEmpty()){
                    sender.sendMessage(ChatColor.RED+"No hay ningun tipo creado!");
                    break;
                }

                for(String type: types){
                    sender.sendMessage(ChatColor.GREEN+type);
                }
                break;
            }


            case "create":
            {
                List<String> types = new ArrayList<>(DataStorage.typeMap.keySet());
                if(types.contains(args[2])){
                    sender.sendMessage(ChatColor.RED+"Ese tipo ya existe!");
                    break;
                }

                List<CustomItem> items = new ArrayList<>();
                DataStorage.typeMap.put(args[2],items);

                sender.sendMessage(ChatColor.GREEN+"Has agregado el nuevo tipo "+ChatColor.YELLOW+args[2]);

                break;


            }
            case "delete":
            {
                List<String> types = new ArrayList<>(DataStorage.typeMap.keySet());
                if(!types.contains(args[2])){
                    sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                    break;
                }

                types.remove(args[2]);
                List<CustomItem> items = DataStorage.typeMap.get(args[2]);

                for(CustomItem item : items)
                    item.setType(this.plugin.getConfig().getString("default_type"));

                List<CustomItem> default_items = DataStorage.typeMap.get(this.plugin.getConfig().getString("default_type"));


                default_items.addAll(items);
                DataStorage.typeMap.put(this.plugin.getConfig().getString("default_type"),default_items);
                DataStorage.typeMap.remove(args[2]);

                sender.sendMessage(ChatColor.GREEN+"Has removido el tipo "+ChatColor.YELLOW+args[2]);
                sender.sendMessage(ChatColor.GRAY+"Moviendo items al tipo por defecto");
                break;
            }
            case "info":
            {

                List<CustomItem> items = DataStorage.typeMap.get(args[2]);
                if(items==null){
                    sender.sendMessage(ChatColor.RED+"Este tipo no existe!");
                    break;
                }

                sender.sendMessage(ChatColor.GREEN+"Items de tipo "+ChatColor.DARK_GREEN+args[2]);
                sender.sendMessage(ChatColor.DARK_GRAY+"================================");
                for(CustomItem item : items){
                    sender.sendMessage(ChatColor.RED+"ID: "+ChatColor.GRAY+item.getId()+"  "+ChatColor.RED+item.getName());

                }
                break;

            }

            case "icon":
            {

                List<String> types = new ArrayList<>(DataStorage.typeMap.keySet());
                if(!types.contains(args[2])){
                    sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                    break;
                }

                CustomItem item = DataStorage.itemMap.get("c"+args[3]);
                if(item == null){
                    sender.sendMessage(ChatColor.RED+"Este item no existe!");
                    break;
                }


                sender.sendMessage(ChatColor.GREEN+"Icono para tipo "+ChatColor.YELLOW+args[2]+ChatColor.GREEN+" actualizado!");
                this.plugin.getConfig().set("data."+args[2]+".icon",item.getId());
                this.plugin.saveConfig();
                break;
            }

            case "add":
            {
                try{
                    List<CustomItem> items = DataStorage.typeMap.get(args[2]);

                    if(items==null){
                        sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                        break;
                    }

                    CustomItem item = DataStorage.itemMap.get("c"+args[3]);
                    if(item == null){
                        sender.sendMessage(ChatColor.RED+"No existe ningun item con ese id");
                        break;
                    }

                    String currentType = item.getType();
                    if(currentType != null)
                        DataStorage.typeMap.get(currentType).remove(item);

                    item.setType(args[2]);





                    items.add(item);
                    DataStorage.typeMap.put(args[2],items);
                    sender.sendMessage(ChatColor.GREEN+"Has establecido el tipo "+ChatColor.YELLOW+args[2]+ChatColor.GREEN+" al item "+ChatColor.YELLOW+item.getName());
                    break;

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting type add <type> <id>");
                }
                break;
            }
            case "remove":
            {
                try{

                    List<CustomItem> items = DataStorage.typeMap.get(args[2]);
                    if(items == null){
                        sender.sendMessage(ChatColor.RED+"Este tipo no existe!");
                        break;
                    }

                    CustomItem item = DataStorage.itemMap.get("c"+args[3]);
                    if(item == null){
                        sender.sendMessage(ChatColor.RED+"El item con este id no existe!");
                        break;
                    }

                    if(!item.getType().equals(args[2])){
                        sender.sendMessage(ChatColor.RED+"El item con este id no pertenece a este tipo");
                        break;
                    }

                    DataStorage.typeMap.get(args[2]).remove(item);
                    item.setType(this.plugin.getConfig().getString("default_type"));
                    DataStorage.typeMap.get(this.plugin.getConfig().getString("default_type")).add(item);
                    sender.sendMessage(ChatColor.GREEN+"Has removido este item del tipo "+ChatColor.YELLOW+args[2]);
                    break;

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting type remove <type> <id>");
                }
                break;
            }








        }
    }

    public void typeMenu(String[] args, CommandSender sender){
        String option = args[1];
        switch(option){
            case "list":
            {
                List<String> types = new ArrayList<>(DataStorage.craftingTypeMap.keySet());
                if(types.isEmpty()){
                    sender.sendMessage(ChatColor.RED+"No hay ningun tipo creado!");
                    break;
                }

                for(String type: types){
                    sender.sendMessage(ChatColor.GREEN+type);
                }
                break;
            }


            case "create":
            {
                CraftingType type = DataStorage.craftingTypeMap.get(args[2]);
                if(type !=null){
                    sender.sendMessage(ChatColor.RED+"Ese tipo ya existe!");
                    break;
                }

                type = new CraftingType(this.plugin.typesFile,args[2],"crafting",true);
                DataStorage.craftingTypeMap.put(args[2],type);

                sender.sendMessage(ChatColor.GREEN+"Has agregado el nuevo tipo "+ChatColor.YELLOW+args[2]);

                break;


            }
            case "delete":
            {

                String name = args[2];

                CraftingType type = DataStorage.craftingTypeMap.get(name);
                if(type == null){
                    sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                    break;
                }

                DataStorage.craftingTypeMap.remove(name);


                List<CustomItem> items = type.getItems();

                String default_type = this.plugin.getConfig().getString("default_type");

                for(CustomItem item : items)
                    item.setType(default_type);

                CraftingType defaultCraftingType = DataStorage.craftingTypeMap.get(default_type);
                defaultCraftingType.getItems().addAll(items);

                sender.sendMessage(ChatColor.GREEN+"Has removido el tipo "+ChatColor.YELLOW+name);
                sender.sendMessage(ChatColor.GRAY+"Moviendo items al tipo por defecto");
                break;
            }
            case "info":
            {

                CraftingType type = DataStorage.craftingTypeMap.get(args[2]);
                if(type == null){
                    sender.sendMessage(ChatColor.RED+"Este tipo no existe!");
                    break;
                }


                sender.sendMessage(ChatColor.GREEN+"Items de tipo "+ChatColor.DARK_GREEN+args[2]);
                sender.sendMessage(ChatColor.DARK_GRAY+"================================");
                for(CustomItem item : type.getItems()){
                    sender.sendMessage(ChatColor.RED+"ID: "+ChatColor.GRAY+item.getId()+"  "+ChatColor.RED+item.getName());

                }
                break;

            }

            case "icon":
            {
                try{

                    String craftingType = args[2];
                    String itemId = "c"+args[3];

                    CraftingType type = DataStorage.craftingTypeMap.get(craftingType);
                    if(type == null){
                        sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                        break;
                    }


                    CustomItem item = DataStorage.itemMap.get(itemId);
                    if(item == null){
                        sender.sendMessage(ChatColor.RED+"Este item no existe!");
                        break;
                    }

                    type.setIcon(item);
                    sender.sendMessage(ChatColor.GREEN+"Icono para tipo "+ChatColor.YELLOW+craftingType+ChatColor.GREEN+" actualizado!");
                    break;

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting type icon <type> <item-id>");
                }
                break;

            }

            case "add":
            {
                try{

                    String craftingType = args[2];
                    String itemId = "c"+args[3];

                    CraftingType type = DataStorage.craftingTypeMap.get(craftingType);


                    if(type==null){
                        sender.sendMessage(ChatColor.RED+"Ese tipo no existe!");
                        break;
                    }

                    CustomItem item = DataStorage.itemMap.get(itemId);
                    if(item == null){
                        sender.sendMessage(ChatColor.RED+"No existe ningun item con ese id");
                        break;
                    }

                    String current = item.getType();
                    if(current != null){
                        CraftingType pastType = DataStorage.craftingTypeMap.get(current);
                        pastType.getItems().remove(item);

                    }

                    item.setType(craftingType);
                    type.getItems().add(item);

                    sender.sendMessage(ChatColor.GREEN+"Has establecido el tipo "+ChatColor.YELLOW+craftingType+ChatColor.GREEN+" al item "+ChatColor.YELLOW+item.getName());
                    break;

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting type add <type> <id>");
                }
                break;
            }
            case "remove":
            {
                try{

                    String craftingType = args[2];
                    String itemId = "c"+args[3];

                    CraftingType type = DataStorage.craftingTypeMap.get(craftingType);

                    if(type == null){
                        sender.sendMessage(ChatColor.RED+"Este tipo no existe!");
                        break;
                    }

                    CustomItem item = DataStorage.itemMap.get(itemId);
                    if(item == null){
                        sender.sendMessage(ChatColor.RED+"El item con este id no existe!");
                        break;
                    }

                    if(!item.getType().equals(craftingType)){
                        sender.sendMessage(ChatColor.RED+"El item con este id no pertenece a este tipo");
                        break;
                    }

                    type.getItems().remove(item);

                    String defaultTypeName = this.plugin.getConfig().getString("default_type");

                    item.setType(defaultTypeName);

                    CraftingType defaultType = DataStorage.craftingTypeMap.get(defaultTypeName);
                    defaultType.getItems().add(item);

                    sender.sendMessage(ChatColor.GREEN+"Has removido este item del tipo "+ChatColor.YELLOW+craftingType);
                    break;

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting type remove <type> <id>");
                }

            }






        }
    }

    public void itemMenu(String[] args, CommandSender sender){
        if( !(sender instanceof Player) )
            return;

        Player player = (Player) sender;

        String option = args[1];

        switch(option){
            case "list":

                for(Map.Entry<String, CustomItem> entry : DataStorage.itemMap.entrySet())	{

                    CustomItem item = entry.getValue();
                    if(item==null)
                        continue;

                    sender.sendMessage(ChatColor.RED+"- "+ChatColor.GRAY+item.getId()+" "+ChatColor.YELLOW+item.getName());



                }
                break;

            case "create":

                try{
                    List<String> receta = new ArrayList<>();
                    CustomItem item = new CustomItem(args[2],player.getInventory().getItemInMainHand(),receta);
                    item.setId("c"+(CustomItem.count-1));

                    CustomItem item2;
                    int count = 0;
                    do{

                        item2 = DataStorage.itemMap.get("c"+(CustomItem.count+count));
                        count++;


                    }while(item2!=null);

                    item.setId("c"+(CustomItem.count-1+count));
                    item.setType(this.plugin.getConfig().getString("default_type"));
                    DataStorage.itemMap.put("c"+(CustomItem.count-1+count),item);


                    DataStorage.craftingTypeMap.get(this.plugin.getConfig().getString("default_type")).getItems().add(item);


                    sender.sendMessage(ChatColor.GREEN+"Has agregado un nuevo item");

                } catch(IndexOutOfBoundsException e){

                    sender.sendMessage(ChatColor.RED+"Formato incorrecto");
                    sender.sendMessage(ChatColor.GRAY+"/mfcrafting items create <nombre>");
                }
                break;

            case "remove":
            {
                CustomItem item = DataStorage.itemMap.get("c"+args[2]);
                if(item == null){
                    sender.sendMessage(ChatColor.RED+"El item con esta id no existe!");
                    break;
                }

                CraftingType type = DataStorage.craftingTypeMap.get(item.getType());
                type.getItems().remove(item);


                sender.sendMessage(ChatColor.GREEN+"Has borrado el item con la id "+ChatColor.YELLOW+args[2]);
                VariedTools.removeCustomItemFromRecipe(item);
                DataStorage.itemMap.remove("c"+args[2]);
                plugin.itemsFile.set("data."+("c"+args[2]),null);
                plugin.itemsFile.save();
                break;
            }

            case "workbench":
            {
                CustomItem item = DataStorage.itemMap.get("c"+args[2]);
                if(item == null){
                    sender.sendMessage(ChatColor.RED+"El item con esta id no existe!");
                    break;
                }

                if(item.onlyWorkbench()){
                    item.setWorkbench(false);
                    sender.sendMessage(ChatColor.GRAY+"El item con la id "+ChatColor.YELLOW+item.getId()+ChatColor.GRAY+" ahora NO necesita mesa de crafteo");
                } else {
                    item.setWorkbench(true);
                    sender.sendMessage(ChatColor.GRAY+"El item con la id "+ChatColor.YELLOW+item.getId()+ChatColor.GRAY+" ahora SI necesita mesa de crafteo");
                }

                break;




            }



            case "update":
                CustomItem item = DataStorage.itemMap.get("c"+args[2]);
                if(item==null){
                    sender.sendMessage(ChatColor.RED+"El item con esta id no existe!");
                    break;
                }

                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                item.setItemStack(itemInHand);
                break;



                default:
                    String commandHeader = "/mfcrafting item";
                    sender.sendMessage(ChatColor.GREEN+commandHeader+ChatColor.GRAY+" list");
                    sender.sendMessage(ChatColor.GREEN+commandHeader+ChatColor.GRAY+" create <name>");
                    sender.sendMessage(ChatColor.GREEN+commandHeader+ChatColor.GRAY+" remove <id>");
                    sender.sendMessage(ChatColor.GREEN+commandHeader+ChatColor.GRAY+" workbench <id>");
                    sender.sendMessage(ChatColor.GREEN+commandHeader+ChatColor.GRAY+" update <id>");

        }





    }

    public void recipeMenu(String[] args, CommandSender sender){
        String option = args[1];

        CustomItem item = DataStorage.itemMap.get("c"+args[2]);

        if(item == null){
            sender.sendMessage(ChatColor.RED+"El item con esa id no existe!");
            return;
        }

        switch(option){
            case "list":
                int i = 0;
                for(HashMap<String,Integer> recipe : item.getRecipes()){
                    sender.sendMessage(ChatColor.GREEN+"Receta "+(i)+":");
                    for(String requirement : CustomItem.getRequiredItems(recipe,"7",this.plugin.langFile)){
                        sender.sendMessage(ChatColor.GRAY+requirement);
                    }
                    i++;
                }

            break;




            case "add":
            {
                String suboption = args[3];
                if(suboption.equals("custom")){

                    try{

                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);


                        String id = "c"+args[5];
                        CustomItem addItem = DataStorage.itemMap.get(id);
                        if(addItem == null){
                            sender.sendMessage(ChatColor.RED+"El item de receta que querias agregar no existe!");
                            break;
                        }

                        Integer amount = Integer.parseInt(args[6]);

                        recipe.put(id,amount);
                        sender.sendMessage(ChatColor.GREEN+"Has agregado este item a una receta de "+ChatColor.YELLOW+item.getName());

                    } catch(Exception e){
                        sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe add <id> custom <indice> <id2> <cantidad>");
                    }


                } else if(suboption.equals("hand")){

                    Player player = (Player) sender;
                    if(player == null){
                        System.out.println("Este comando solo puede ser ejecutado in-game");
                        break;
                    }


                    try{

                        String id = VariedTools.itemStackToId(player.getInventory().getItemInMainHand());

                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);


                        Integer amount = Integer.parseInt(args[5]);
                        recipe.put(id,amount);

                        player.sendMessage(ChatColor.GREEN+"Has agregado este item a la receta de "+ChatColor.YELLOW+item.getName());

                    } catch(Exception e){

                        sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe add <id> hand <index> <cantidad>");


                    }
                }
                break;
            }





            case "remove":
            {

                try {

                    String suboption = args[3];
                    if(suboption.equals("custom")){


                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);


                        String id = "c"+args[5];




                        Integer amount = recipe.get(id);
                        if(amount == 0){
                            sender.sendMessage(ChatColor.RED+"Este item no forma parte de la receta de "+ChatColor.YELLOW+item.getName());

                            break;
                        }

                        recipe.remove(id);
                        sender.sendMessage(ChatColor.GREEN+"Has eliminado este item de la receta de "+ChatColor.YELLOW+item.getName());
                        break;


                    } else if(suboption.equals("hand")){

                        Player player = (Player) sender;
                        if(player == null){
                            System.out.println("Este comando solo puede ser ejecutado in-game");
                            break;
                        }

                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);


                        String id = VariedTools.itemStackToId(player.getInventory().getItemInMainHand());
                        Integer amount = recipe.get(id);
                        if(amount == 0){
                            sender.sendMessage(ChatColor.RED+"Este item no forma parte de la receta de "+ChatColor.YELLOW+item.getName());
                            break;
                        }

                        recipe.remove(id);
                        sender.sendMessage(ChatColor.GREEN+"Has eliminado este item de la receta de "+ChatColor.YELLOW+item.getName());
                        break;

                    }
                    break;


                } catch(IllegalArgumentException | IndexOutOfBoundsException e){
                    assert sender != null;
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe remove <id> custom <recipe> <id2>");
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe remove <id> hand <recipe>");
                }

                break;
            }

            case "create":
            {
                HashMap<String,Integer> recipe = new HashMap<>();
                item.getRecipes().add(recipe);
                sender.sendMessage(ChatColor.GREEN+"Has agregado otra receta para este item!");
                break;
            }



            case "amount":
            {
                String suboption = args[3];
                if(suboption.equals("custom")){
                    try{

                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);

                        String id = "c"+args[5];
                        Integer amount = recipe.get(id);
                        if(amount == 0){
                            sender.sendMessage(ChatColor.RED+"Este item no forma parte de la receta de "+ChatColor.YELLOW+item.getName());

                            break;
                        }

                        amount = Integer.parseInt(args[6]);
                        if(amount <=0){
                            sender.sendMessage(ChatColor.RED+"El valor tiene que ser mayor a 0");
                            return;
                        }

                        recipe.put(id,amount);
                        sender.sendMessage(ChatColor.GREEN+"Has cambiado la cantidad este item de la receta de "+ChatColor.YELLOW+item.getName());

                    } catch(Exception e){
                        sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe amount <id> custom <recipe> <id2> <cantidad>");
                    }

                    break;


                } else if(suboption.equals("hand")){


                    Player player = (Player) sender;
                    if(player == null){
                        System.out.println("Este comando solo puede ser ejecutado in-game");
                        break;
                    }

                    try{

                        int index = Integer.parseInt(args[4]);
                        HashMap<String,Integer> recipe = item.getRecipes().get(index);

                        String id = VariedTools.itemStackToId(player.getInventory().getItemInMainHand());
                        Integer amount = recipe.get(id);
                        if(amount == 0){
                            sender.sendMessage(ChatColor.RED+"Este item no forma parte de la receta de "+ChatColor.YELLOW+item.getName());
                            break;
                        }

                        amount = Integer.parseInt(args[5]);

                        recipe.put(id,amount);
                        sender.sendMessage(ChatColor.GREEN+"Has eliminado este item de la receta de "+ChatColor.YELLOW+item.getName());
                        break;
                    } catch (Exception e){
                        sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe amount <id> hand <recipe> <cantidad>");

                    }


                }
                break;



            }


            case "clear":
                try{
                    int index = Integer.parseInt(args[3]);
                    HashMap<String,Integer> recipe = item.getRecipes().get(index);
                    recipe.clear();
                    sender.sendMessage(ChatColor.GREEN+"Has limpiado la receta "+index+" del item "+ChatColor.YELLOW+item.getName());

                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting recipe clear <id> <recipe>");
                 }

        }





    }

    public void settingsMenu(String[] args, CommandSender sender){
        String option = args[1];
        switch(option){

            case "slot":
            {

                try{
                    Integer slot = Integer.parseInt(args[2]);
                    this.plugin.getConfig().set("craft_item_slot",slot);
                    this.plugin.saveConfig();
                    sender.sendMessage(ChatColor.GREEN+"Slot establecido");

                }catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting settings slot <integer>");
                }

                break;

            }




            case "menu":
            {

                try{

                    String menu = args[2];
                    this.plugin.getConfig().set("craft_item_menu",menu);
                    this.plugin.saveConfig();
                    sender.sendMessage(ChatColor.GREEN+"Nuevo menu establecido!");

                } catch (Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting settings menu <menu>");
                }

                break;

            }



            case "default":


            case "workbench":
            {
                try{

                    String option2 = args[2];
                    if(option2.equals("id")){

                        Player player = (Player) sender;
                        if(player==null)
                            return;

                        ItemStack item = player.getInventory().getItemInMainHand();
                        this.plugin.getConfig().set("workbench_id",VariedTools.itemStackToId(item));
                        this.plugin.saveConfig();
                        sender.sendMessage(ChatColor.GREEN+"Nuevo bloque de mesa de crafteo agregado!");

                    } else if(option2.equals("show")){

                        if(this.plugin.getConfig().getBoolean("workbench_shows_All")){

                            this.plugin.getConfig().set("workbench_shows_All",false);
                            sender.sendMessage(ChatColor.RED+"Ahora no todos los items se mostraran en la mesa de crafteo!");


                        } else {

                            this.plugin.getConfig().set("workbench_shows_All",true);
                            sender.sendMessage(ChatColor.GREEN+"Ahora todos los items se mostraran en la mesa de crafteo!");
                            this.plugin.saveConfig();
                        }
                    }
                } catch (Exception e){

                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting settings workbench <id|show>");

                }
                break;



            }
            case "worlds":
                try{

                    List<String> blockedWorlds = this.plugin.getConfig().getStringList("blocked_worlds");

                    String option2 = args[2];

                    if(option2.equals("add")){

                        if(blockedWorlds.contains(args[3])){
                            sender.sendMessage(ChatColor.RED+"Ese mundo ya esta bloqueado!");
                            return;
                        }

                        blockedWorlds.add(args[3]);
                        this.plugin.getConfig().set("blocked_worlds",blockedWorlds);
                        this.plugin.saveConfig();
                        sender.sendMessage(ChatColor.GREEN+"Has agregado el mundo "+ChatColor.YELLOW+args[3]+ChatColor.GREEN+" a la lista negra");


                    } else if(option2.equals("remove")){
                        if(!blockedWorlds.contains(args[3])){
                            sender.sendMessage(ChatColor.RED+"Ese mundo no esta bloqueado!");
                            return;
                        }

                        blockedWorlds.remove(args[3]);
                        this.plugin.getConfig().set("blocked_worlds",blockedWorlds);
                        this.plugin.saveConfig();

                        sender.sendMessage(ChatColor.GREEN+"Este mundo ya no estara bloqueado!");




                    } else if(option2.equals("list")){

                        sender.sendMessage(ChatColor.RED+"Mundos deshabilitados:");
                        for(String world: blockedWorlds){
                            sender.sendMessage(ChatColor.GRAY+world);
                        }



                    }


                break;





                } catch(Exception e){
                    sender.sendMessage(ChatColor.GREEN+"Formato correcto: "+ChatColor.GRAY+"/mfcrafting settings world <add|remove|list>");
                }
                break;










        }
    }







}
