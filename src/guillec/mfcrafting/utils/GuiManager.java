package guillec.mfcrafting.utils;

import guillec.mfcrafting.MFCrafting;
import guillec.mfcrafting.files.MFile;
import guillec.mfcrafting.objects.CraftingType;
import guillec.mfcrafting.objects.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuiManager {




    public static void generateMainMenu(Player player, int page, MFCrafting plugin){

       // List<String> types = new ArrayList<String>(DataStorage.typeMap.keySet());
        List<String> types = new ArrayList<>(DataStorage.craftingTypeMap.keySet());
        if(types.isEmpty()){
            player.sendMessage(ChatColor.RED+"No hay categorias de crafteos establecidas");
            return;
        }

        if(!plugin.getConfig().getBoolean("show_default"))
            types.remove(plugin.getConfig().getString("default_type"));


        if(types.size()>=54)
        types = types.subList((page*53),types.size());




        int rows = VariedTools.getRequiredRows(types.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }


        Inventory inventory =  Bukkit.createInventory(player,rows, "Tipo de crafteo");
        System.out.println("Array size: "+types.size());

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;

        System.out.println("Rows: "+rows/9);
        List<String> lore = new ArrayList<>();
        while(i<(rows/9)-reducer){

                int endbound = types.size();
                if(endbound>9 && row_line>=(rows/9)-reducer-1)
                    endbound = types.size()-9*toSubtract;
                if(endbound>=9)
                    endbound = 9;

                System.out.println("Endbound: "+endbound);
                System.out.println("Row line: "+row_line);

                List<String> sublist= types.subList(row_line*9,(row_line*9)+endbound);
                System.out.println("Sublist size: "+sublist.size());
                List<Integer> slots = inventorySlots((sublist.size()));
                int k = 0;

                for(String type : sublist){

                    CraftingType craftingType = DataStorage.craftingTypeMap.get(type);


                    ItemStack itemStack;
                    if(craftingType.getIcon() == null){
                        itemStack = new ItemStack(Material.RAILS);
                        System.out.println("Icon is null");
                    }

                    else {
                        itemStack = new ItemStack(craftingType.getIcon().getItemStack());

                    }


                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName("§2§l"+type);
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    System.out.println(k);
                    inventory.setItem(slots.get(k)+(row_line*9),itemStack);
                    System.out.println(meta.getDisplayName()+" generado correctamente");
                    k++;
                }

                if(types.size()>=54){


                    ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                    itemStack.setDurability((short)13);
                    ItemMeta meta = itemStack.getItemMeta();

                    lore.add("§aClick izquierdo: §7pag "+(page+2));
                    if(page > 0)
                        lore.add("§cClick derecho: §7pag "+(page));
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    inventory.setItem(53,itemStack);
                } else if(page>0){
                    ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                    itemStack.setDurability((short)13);
                    ItemMeta meta = itemStack.getItemMeta();
                    lore.add("§cClick derecho: §7pag "+(page));
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    inventory.setItem(rows-1,itemStack);
                }

                toSubtract++;
                row_line++;
            i++;
        }

        Stack<Inventory> inventoryStack = DataStorage.recentInventory.get(player.getName());
        if(inventoryStack == null){
            inventoryStack = new Stack<>();
            DataStorage.recentInventory.put(player.getName(),inventoryStack);
        }

        else
            DataStorage.recentInventory.get(player.getName()).clear();

        player.openInventory(inventory);





    }

    public static void generateWorkbenchMainMenu(Player player, int page, MFCrafting plugin){

        List<String> types = new ArrayList<String>(DataStorage.craftingTypeMap.keySet());
        if(types.isEmpty()){
            player.sendMessage(ChatColor.RED+"No hay categorias de crafteos establecidas");
            return;
        }

        if(!plugin.getConfig().getBoolean("show_default"))
            types.remove(plugin.getConfig().getString("default_type"));

        if(types.size()>=54)
            types = types.subList((page*53),types.size());

        int rows = VariedTools.getRequiredRows(types.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }


        Inventory inventory =  Bukkit.createInventory(player,rows, "Mesa de crafteo");
        System.out.println("Array size: "+types.size());

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;

        System.out.println("Rows: "+rows/9);
        List<String> lore = new ArrayList<>();

        while(i<(rows/9)-reducer){

            int endbound = types.size();
            if(endbound>9 && row_line>=(rows/9)-reducer-1)
                endbound = types.size()-9*toSubtract;
            if(endbound>=9)
                endbound = 9;

            System.out.println("Endbound: "+endbound);
            System.out.println("Row line: "+row_line);

            List<String> sublist= types.subList(row_line*9,(row_line*9)+endbound);
            System.out.println("Sublist size: "+sublist.size());
            List<Integer> slots = inventorySlots((sublist.size()));
            int k = 0;
            for(String type : sublist){

                CraftingType craftingType = DataStorage.craftingTypeMap.get(type);


                ItemStack itemStack;
                if(craftingType.getIcon() == null)
                    itemStack = new ItemStack(Material.RAILS);
                else {
                    itemStack = new ItemStack(craftingType.getIcon().getItemStack());

                }


                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§2§l"+type);
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                System.out.println(k);
                inventory.setItem(slots.get(k)+(row_line*9),itemStack);
                System.out.println(meta.getDisplayName()+" generado correctamente");
                k++;
            }

            if(types.size()>=54){


                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.clear();
                lore.add("§aClick izquierdo: §7pag "+(page+2));
                if(page > 0)
                    lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(53,itemStack);
            } else if(page>0){
                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                lore.clear();
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(rows-1,itemStack);
            }

            toSubtract++;
            row_line++;
            i++;
        }

        Stack<Inventory> inventoryStack = DataStorage.recentInventory.get(player.getName());
        if(inventoryStack == null){
            inventoryStack = new Stack<>();
            DataStorage.recentInventory.put(player.getName(),inventoryStack);
        }

        else
            DataStorage.recentInventory.get(player.getName()).clear();

        DataStorage.recentInventory.get(player.getName()).push(inventory);


        player.openInventory(inventory);





    }


    public static void generateTypeMenu(Player player, String type, int page, MFile lang){

        DataStorage.typeViewingMap.put(player.getName(),type);
        CraftingType craftingType = DataStorage.craftingTypeMap.get(type);


        List<CustomItem> items = new ArrayList<>(VariedTools.getItemsWithoutWorkbench(craftingType.getItems()));
        if(items.isEmpty()){
            System.out.println("item vacio");
            return;
        }

        for(CustomItem item : craftingType.getItems())
            if(item.getRecipes().isEmpty())
                items.remove(item);


        if(items.size()>=54)
            items = items.subList((page*53),items.size());
        int rows = VariedTools.getRequiredRows(items.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }

        Inventory inventory =  Bukkit.createInventory(player,rows, "Craftear");

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;

        List<String> lore = new ArrayList<>();
        while(i<(rows/9)-reducer){

            int endbound = items.size();
            if(endbound>9 && row_line>=(rows/9)-reducer-1)
                endbound = items.size()-9*toSubtract;
            if(endbound>=9)
                endbound = 9;

            List<CustomItem> sublist= items.subList(row_line*9,(row_line*9)+endbound);
            System.out.println("Sublist size: "+sublist.size());
            List<Integer> slots = inventorySlots((sublist.size()));
            int k = 0;
            for(CustomItem item : sublist){
                ItemStack itemStack = new ItemStack(item.getItemStack());

                ItemMeta meta = itemStack.getItemMeta();
                lore = generateItemLore(item,player,lang);
                lore.add(0,"§0"+item.getId());
                meta.setLore(lore);
                lore.clear();
                itemStack.setItemMeta(meta);
                inventory.setItem(slots.get(k)+(row_line*9),itemStack);
                k++;
            }

            if(items.size()>=54){


                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§aClick izquierdo: §7pag "+(page+2));
                if(page > 0)
                    lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(53,itemStack);
            } else if(page>0){
                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(rows-1,itemStack);
            }


            toSubtract++;
            row_line++;
            i++;
        }

        player.openInventory(inventory);

    }

    public static void generateWorkbenchTypeMenu(Player player, String type, int page, MFile lang){

        DataStorage.typeViewingMap.put(player.getName(),type);


        CraftingType craftingType = DataStorage.craftingTypeMap.get(type);
        if(!craftingType.getType().equals("crafting"))
            return;


        List<CustomItem> items = new ArrayList<>(craftingType.getItems());

        //List<CustomItem> items = new ArrayList<>(DataStorage.typeMap.get(type));


        if(items.isEmpty()){
            System.out.println("item vacio");
            return;
        }

        for(CustomItem item : craftingType.getItems())
            if(item.getRecipes().isEmpty())
                items.remove(item);


        if(items.size()>=54)
            items = items.subList((page*53),items.size());
        int rows = VariedTools.getRequiredRows(items.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }

        Inventory inventory =  Bukkit.createInventory(player,rows, "Craftear");

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;

        List<String> lore = new ArrayList<>();
        while(i<(rows/9)-reducer){

            int endbound = items.size();
            if(endbound>9 && row_line>=(rows/9)-reducer-1)
                endbound = items.size()-9*toSubtract;
            if(endbound>=9)
                endbound = 9;

            List<CustomItem> sublist= items.subList(row_line*9,(row_line*9)+endbound);
            System.out.println("Sublist size: "+sublist.size());
            List<Integer> slots = inventorySlots((sublist.size()));
            int k = 0;
            for(CustomItem item : sublist){
                ItemStack itemStack = new ItemStack(item.getItemStack());

                ItemMeta meta = itemStack.getItemMeta();
                lore = generateItemLore(item,player,lang);
                lore.add(0,"§0"+item.getId());
                meta.setLore(lore);
                lore.clear();
                itemStack.setItemMeta(meta);
                inventory.setItem(slots.get(k)+(row_line*9),itemStack);
                k++;
            }

            if(items.size()>=54){


                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§aClick izquierdo: §7pag "+(page+2));
                if(page > 0)
                    lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(53,itemStack);
            } else if(page>0){
                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(rows-1,itemStack);
            }


            toSubtract++;
            row_line++;
            i++;
        }

        player.openInventory(inventory);

    }



    public static void openRecipesList(Player player, CustomItem item, int page, MFile lang){

        DataStorage.itemViewingMap.put(player.getName(),item);

        List<HashMap<String,Integer>> recipes = item.getRecipes();
        if(recipes.isEmpty()){
            System.out.println("item vacio");
            return;
        }

        if(recipes.size()>=54)
            recipes = recipes.subList((page*53),recipes.size());
        int rows = VariedTools.getRequiredRows(recipes.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }

        Inventory inventory =  Bukkit.createInventory(player,rows, "Recetas");

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;

        List<String> lore = new ArrayList<>();
        while(i<(rows/9)-reducer){

            int endbound = recipes.size();
            if(endbound>9 && row_line>=(rows/9)-reducer-1)
                endbound = recipes.size()-9*toSubtract;
            if(endbound>=9)
                endbound = 9;

            List<HashMap<String,Integer>> sublist= recipes.subList(row_line*9,(row_line*9)+endbound);
            System.out.println("Sublist size: "+sublist.size());
            List<Integer> slots = inventorySlots((sublist.size()));
            int k = 0;
            for(HashMap<String,Integer> recipe : sublist){
                ItemStack itemStack = new ItemStack(item.getItemStack());

                ItemMeta meta = itemStack.getItemMeta();
                lore = CustomItem.getRequiredItems(recipe,"7",lang);
                lore.add("");
                lore.add("§2Receta detallada §fσ");
                lore.add("§2Volver al item §f÷");
                meta.setLore(lore);
                lore.clear();
                meta.setDisplayName("§bReceta "+(1+k+(row_line*9)+page*53));
                itemStack.setItemMeta(meta);
                inventory.setItem(slots.get(k)+(row_line*9),itemStack);
                k++;
            }

            if(recipes.size()>=54){


                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§aClick izquierdo: §7pag "+(page+2));
                if(page > 0)
                    lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(53,itemStack);
            } else if(page>0){
                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(rows-1,itemStack);
            }

            toSubtract++;
            row_line++;
            i++;

        }
        player.openInventory(inventory);

    }

    public static void openExpandedRecipe(Player player, HashMap<String,Integer> recipe, int page, MFile lang){

        DataStorage.recipeViewingMap.put(player.getName(),recipe);

        List<ItemStack> items = CustomItem.recipeToItemStacks(recipe,lang);
        if(items.isEmpty()){
            System.out.println("item vacio");
            return;
        }

        if(items.size()>=54)
            items = items.subList((page*53),items.size());
        int rows = VariedTools.getRequiredRows(items.size());

        int reducer = 0;
        if(page > 0 && rows<54){
            rows+=9;
            reducer= 1;
        }

        Inventory inventory =  Bukkit.createInventory(player,rows, "Receta");

        int i = 0;
        int row_line = 0;
        int toSubtract = 0;
        while(i<(rows/9)-reducer){

            int endbound = items.size();
            if(endbound>9 && row_line>=(rows/9)-reducer-1)
                endbound = items.size()-9*toSubtract;
            if(endbound>=9)
                endbound = 9;

            List<ItemStack> sublist= items.subList(row_line*9,(row_line*9)+endbound);
            List<Integer> slots = inventorySlots((sublist.size()));
            int k = 0;
            List<String> lore = new ArrayList<>();
            for(ItemStack item : sublist){
                lore.add("");
                lore.add("§7Click derecho para volver a lista de recetas");
                if(item.getItemMeta().getLore()!=null)
                    item.getItemMeta().getLore().addAll(lore);
                else
                    item.getItemMeta().setLore(lore);
                lore.clear();
                inventory.setItem(slots.get(k)+(row_line*9),item);
                k++;
            }




            if(items.size()>=54){


                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§aClick izquierdo: §7pag "+(page+2));
                if(page > 0)
                    lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(53,itemStack);
            } else if(page>0){
                ItemStack itemStack = new ItemStack((Material.STAINED_GLASS));
                itemStack.setDurability((short)13);
                ItemMeta meta = itemStack.getItemMeta();
                lore.add("§cClick derecho: §7pag "+(page));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inventory.setItem(rows-1,itemStack);
            }

            toSubtract++;
            row_line++;
            i++;
        }
        player.openInventory(inventory);


    }


    public static List<Integer> inventorySlots(int n){
        System.out.println("Slot input: "+n);
        List<Integer> slots = new ArrayList<>();
        String csv = "";
        switch(n){

            case 1:
                csv = "4";
                break;
            case 2:
                csv = "1,7";
                break;
            case 3:
                csv = "1,4,7";
                break;
            case 4:
                csv = "0,2,6,8";
                break;
            case 5:
                csv = "0,2,4,6,8";
                break;
            case 6:
                csv = "1,2,3,5,6,7";
                break;
            case 7:
                csv = "1,2,3,4,5,6,7";
                break;
            case 8:
                csv = "0,1,2,3,5,6,7,8";
                break;
            case 9:
                csv = "0,1,2,3,4,5,6,7,8";
        }

        String[] values = csv.split(",");
        System.out.println(csv);

        for(String value: values){
            Integer number = Integer.parseInt(value);
            slots.add(number);
        }

        return slots;
    }

    public static List<String> generateItemLore(CustomItem item, Player player, MFile lang){
        List<String> lore = new ArrayList<>();
        lore.add("§0"+item.getId());
        int i = 0;
        for(HashMap<String,Integer> recipe : item.getRecipes()){

            if(i<3){
                if(VariedTools.fulfillsRequirements(player,recipe)){
                    lore.add("§aReceta n."+(i+1));
                    lore.addAll(CustomItem.getRequiredItems(recipe,"a",lang));
                } else {
                    lore.add("§cReceta n."+(i+1));
                    lore.addAll(CustomItem.getRequiredItems(recipe,"c",lang));
                }
            } else {
                lore.add("§4"+(recipe.size()-i)+" §erecetas mas...");

            }


            lore.add("");
            i++;

        }
        lore.add("");
        lore.add("§2Craftear §fσ");
        lore.add("§2Ver Recetas §f÷");

        return lore;

    }



}
