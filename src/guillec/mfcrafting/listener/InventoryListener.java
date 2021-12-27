package guillec.mfcrafting.listener;

import guillec.mfcrafting.MFCrafting;
import guillec.mfcrafting.objects.CustomItem;
import guillec.mfcrafting.utils.DataStorage;
import guillec.mfcrafting.utils.GuiManager;
import guillec.mfcrafting.utils.VariedTools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryListener implements Listener {
    private final MFCrafting plugin;

    public InventoryListener(MFCrafting plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e){
        System.out.println(e.getInventory().getTitle());
        System.out.println(e.getSlot());

        if(e.getWhoClicked().getGameMode()== GameMode.CREATIVE)
            return;

        String title = e.getInventory().getTitle();
        int slot = e.getRawSlot();
        Player player = (Player)e.getWhoClicked();

        switch(title){
            case "container.crafting":

                if(this.plugin.getConfig().getStringList("blocked_worlds").contains(player.getWorld().getName()))
                    return;


                if(slot == this.plugin.getConfig().getInt("craft_item_slot")){

                    ItemStack selectedItemStack = e.getInventory().getItem(slot);
                    if(selectedItemStack!=null){
                        System.out.println("Nulo");
                        return;
                    } else {
                        System.out.println("Not nulo");
                    }

                    GuiManager.generateMainMenu(player,0,this.plugin);
                    e.setCancelled(true);
                }
                break;


            case "Tipo de crafteo":
            {
                if(e.getAction()==InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction()==InventoryAction.HOTBAR_SWAP) {
                    e.setCancelled(true);
                    return;
                }




                System.out.println("Slot seleccionado: "+slot);
                ItemStack itemStack;
                try{
                    itemStack = e.getInventory().getItem(slot);
                } catch(Exception ex){
                    e.setCancelled(true);
                    return;
                }



                if(itemStack!=null && itemStack.getType()!= Material.STAINED_GLASS){
                    try{
                        String displayName = itemStack.getItemMeta().getDisplayName();
                        String type = displayName.substring(displayName.lastIndexOf("§")+2);
                        GuiManager.generateTypeMenu(player,type,0, plugin.langFile);
                        e.setCancelled(true);
                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                        e.setCancelled(true);
                    }


                } else if(itemStack !=null){

                    try{
                        List<String> lore = itemStack.getItemMeta().getLore();

                        if (lore.size() > 1) {

                            String leftClick = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            String rightClick = lore.get(1).substring(lore.get(1).lastIndexOf(" ")+1);
                            if(e.getClick()==ClickType.LEFT){
                                int page = Integer.parseInt(leftClick);
                                GuiManager.generateMainMenu(player,page-1,this.plugin);
                            }
                            else if(e.getClick() == ClickType.RIGHT){
                                int page = Integer.parseInt(rightClick);
                                GuiManager.generateMainMenu(player,page-1,this.plugin);

                            }
                            e.setCancelled(true);
                        } else {
                            String click = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            int page = Integer.parseInt(click);
                            GuiManager.generateMainMenu(player,page-1,this.plugin);

                        }




                        e.setCancelled(true);


                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }


                }
                e.setCancelled(true);
                break;
            }
            case "Mesa de crafteo":
            {

                if(e.getAction()==InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction()==InventoryAction.HOTBAR_SWAP) {
                    e.setCancelled(true);
                    return;
                }
                
                ItemStack itemStack;
                try{
                    itemStack = e.getInventory().getItem(slot);
                } catch(Exception ex){
                    e.setCancelled(true);
                    return;
                }



                if(itemStack!=null && itemStack.getType()!= Material.STAINED_GLASS){
                    try{
                        String displayName = itemStack.getItemMeta().getDisplayName();
                        String type = displayName.substring(displayName.lastIndexOf("§")+2);
                        GuiManager.generateWorkbenchTypeMenu(player,type,0, plugin.langFile);
                        e.setCancelled(true);
                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                        e.setCancelled(true);
                    }


                } else if(itemStack !=null){

                    try{
                        List<String> lore = itemStack.getItemMeta().getLore();

                        if (lore.size() > 1) {

                            String leftClick = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            String rightClick = lore.get(1).substring(lore.get(1).lastIndexOf(" ")+1);
                            if(e.getClick()==ClickType.LEFT){
                                int page = Integer.parseInt(leftClick);
                                GuiManager.generateWorkbenchMainMenu(player,page-1,this.plugin);
                            }
                            else if(e.getClick() == ClickType.RIGHT){
                                int page = Integer.parseInt(rightClick);
                                GuiManager.generateWorkbenchMainMenu(player,page-1,this.plugin);

                            }
                            e.setCancelled(true);
                        } else {
                            String click = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            int page = Integer.parseInt(click);
                            GuiManager.generateWorkbenchMainMenu(player,page-1,this.plugin);

                        }




                        e.setCancelled(true);


                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }


                }
                e.setCancelled(true);
                break;
                
                
                
                
                
            }
            
            
            case "Craftear":
            {
                if(e.getAction()==InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction()==InventoryAction.HOTBAR_SWAP) {
                    e.setCancelled(true);
                    return;
                }

                ItemStack itemStack = e.getInventory().getItem(slot);
                if(itemStack!=null && itemStack.getType()!=Material.AIR && itemStack.getType()!=Material.STAINED_GLASS){

                    String loreId = itemStack.getItemMeta().getLore().get(0);
                    String id = loreId.substring(loreId.lastIndexOf("§")+2);

                    CustomItem item = DataStorage.itemMap.get(id);

                    if(e.getClick()==ClickType.RIGHT){



                        e.setCancelled(true);
                        DataStorage.recentInventory.get(player.getName()).push(e.getInventory());
                        GuiManager.openRecipesList(player,item,0, plugin.langFile);
                        break;

                    } else if(e.getClick()==ClickType.LEFT){

                        player.closeInventory();
                        HashMap<String,Integer> recipe = VariedTools.getFulfilledRecipe(player,item);
                        if(recipe == null){
                            player.sendMessage(ChatColor.RED+"No tienes los suficientes items requeridos para craftear este item!");
                            player.openInventory(e.getInventory());
                            return;
                        }



                        craft(player,item,recipe);
                        player.openInventory(e.getInventory());
                        e.setCancelled(true);



                    }
                    e.setCancelled(true);



                } else if(itemStack!=null && itemStack.getType()==Material.STAINED_GLASS){


                    try{
                        List<String> lore = itemStack.getItemMeta().getLore();

                        String type = DataStorage.typeViewingMap.get(player.getName());

                        if (lore.size() > 1) {

                            String leftClick = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            String rightClick = lore.get(1).substring(lore.get(1).lastIndexOf(" ")+1);
                            if(e.getClick()==ClickType.LEFT){
                                int page = Integer.parseInt(leftClick);
                                GuiManager.generateTypeMenu(player,type,page-1, plugin.langFile);
                            }
                            else if(e.getClick() == ClickType.RIGHT){
                                int page = Integer.parseInt(rightClick);
                                GuiManager.generateTypeMenu(player,type,page-1, plugin.langFile);

                            }
                            e.setCancelled(true);
                        } else {
                            String click = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            int page = Integer.parseInt(click);
                            GuiManager.generateTypeMenu(player,type,page-1, plugin.langFile);

                        }




                        e.setCancelled(true);


                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }



                }


            }
            case "Recetas":
            {

                if(e.getAction()==InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction()==InventoryAction.HOTBAR_SWAP) {
                    e.setCancelled(true);
                    return;
                }

                if(slot>53){
                    e.setCancelled(true);
                    return;
                }




                try{

                    CustomItem item = DataStorage.itemViewingMap.get(player.getName());
                    if(item == null){
                        e.setCancelled(true);
                        return;
                    }

                    ItemStack selectedItem = e.getInventory().getItem(slot);
                    if(selectedItem != null && selectedItem.getType()!=Material.AIR && selectedItem.getType()!= Material.STAINED_GLASS){


                        if(e.getClick()==ClickType.LEFT){

                            String displayName = selectedItem.getItemMeta().getDisplayName();
                            System.out.println(displayName);
                            int index = Integer.parseInt(displayName.substring(displayName.lastIndexOf(" ")+1));
                            HashMap<String,Integer> recipeViewing = item.getRecipes().get(index-1);

                            DataStorage.recentInventory.get(player.getName()).push(e.getInventory());
                            GuiManager.openExpandedRecipe(player,recipeViewing,0, plugin.langFile);

                        } else if(e.getClick() == ClickType.RIGHT){
                           player.sendMessage(ChatColor.GRAY+"Regresando a items crafteables tipo "+ChatColor.GREEN+item.getType());
                           player.closeInventory();
                           player.openInventory(DataStorage.recentInventory.get(player.getName()).pop());
                        }

                    }





                } catch(IndexOutOfBoundsException ignore){

                }



                e.setCancelled(true);
                break;


            }



            case "Receta":
                if(e.getAction()==InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction()==InventoryAction.HOTBAR_SWAP) {
                    e.setCancelled(true);
                    return;
                }

                ItemStack item = e.getInventory().getItem(e.getSlot());
                if(item == null)
                    return;

                if(item.getType()==Material.STAINED_GLASS){

                    try{
                        List<String> lore = item.getItemMeta().getLore();

                        HashMap<String,Integer> recipeViewed = DataStorage.recipeViewingMap.get(e.getWhoClicked().getName());

                        if (lore.size() > 1) {

                            String leftClick = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            String rightClick = lore.get(1).substring(lore.get(1).lastIndexOf(" ")+1);
                            if(e.getClick()==ClickType.LEFT){
                                int page = Integer.parseInt(leftClick);
                                GuiManager.openExpandedRecipe(player,recipeViewed,page-1, plugin.langFile);

                            }
                            else if(e.getClick() == ClickType.RIGHT){
                                int page = Integer.parseInt(rightClick);
                                GuiManager.openExpandedRecipe(player,recipeViewed,page-1, plugin.langFile);

                            }
                            e.setCancelled(true);
                        } else {
                            String click = lore.get(0).substring(lore.get(0).lastIndexOf(" ")+1);
                            int page = Integer.parseInt(click);
                            GuiManager.openExpandedRecipe(player,recipeViewed,page-1, plugin.langFile);

                        }




                        e.setCancelled(true);


                    } catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }

                } else if(e.getClick()==ClickType.RIGHT){
                    player.openInventory(DataStorage.recentInventory.get(player.getName()).pop());
                }
                e.setCancelled(true);
        }




    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent e){

        System.out.println(e.getInventorySlots().toString());

        if(e.getInventorySlots().contains(this.plugin.getConfig().getInt("craft_item_slot"))){
            System.out.println("Cancelado");
            e.setCancelled(true);
        }

    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        String inventoryName = e.getPlayer().getInventory().getName();

        switch(inventoryName){
            case "Receta":
            case "Tipo de crafteo":
            case "Craftear":
            case "Mesa de crafteo":
                e.setCancelled(true);
        }

    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent e){
        String source = e.getSource().getName();
        switch(source){
            case "Receta":
            case "Tipo de crafteo":
            case "Craftear":
                e.setCancelled(true);
        }

        String destination = e.getDestination().getName();
        switch(destination){
            case "Receta":
            case "Tipo de crafteo":
            case "Craftear":
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e){
        if(isWorkbench(e.getClickedBlock())){
            if(this.plugin.getConfig().getStringList("blocked_worlds").contains(e.getPlayer().getWorld().getName()))
                return;
            GuiManager.generateWorkbenchMainMenu(e.getPlayer(),0,this.plugin);

        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if(e.getInventory().getName().equals("container.crafting")){
            if(this.plugin.getConfig().getString("workbench_id").equals("WORKBENCH:0"))
                e.setCancelled(true);
        }

    }

    public boolean isWorkbench(Block block){

        if(block == null)
            return false;

        String identifier = block.getType().toString()+":"+block.getData();
        System.out.println(identifier);
        return identifier.equals(this.plugin.getConfig().getString("workbench_id"));
    }





    public void craft(Player player, CustomItem item, HashMap<String,Integer> recipe){
        Inventory inventory = player.getInventory();
        int i = 0;
        for(Map.Entry<String,Integer> items : recipe.entrySet()){

            CustomItem requiredItem = DataStorage.itemMap.get(items.getKey());
            ItemStack itemStack;
            if(requiredItem!=null){

                itemStack = requiredItem.getItemStack();

            } else {

                itemStack = VariedTools.idToItemStack(items.getKey());

            }

            int amount = items.getValue();
            takeItem(player,itemStack,amount);
            player.updateInventory();
            i++;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,10,1f);

        if(inventory.firstEmpty() != -1)
            inventory.addItem(item.getItemStack());
        else
            player.getWorld().dropItemNaturally(player.getLocation(),item.getItemStack());



    }

    public void takeItem(Player player, ItemStack item, int amountRequired){
        ItemStack[] contents = player.getInventory().getContents();

        int itemsToTake = amountRequired;
        for(int i=0;i<contents.length;i++){
            ItemStack current = contents[i];

            if(current!=null && VariedTools.isSameItemStack(item,current)){

                if(current.getAmount()>itemsToTake){
                    current.setAmount(current.getAmount() - itemsToTake);
                    return;
                }


                itemsToTake-=current.getAmount();
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }


            if (itemsToTake <= 0)
                return;
        }




    }




}
