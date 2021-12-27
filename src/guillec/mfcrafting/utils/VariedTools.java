package guillec.mfcrafting.utils;

import guillec.mfcrafting.objects.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariedTools {

    public static ItemStack idToItemStack(String id){

        String[] splitData = id.split(":");

        Material material = null;
        int durability = 0;

        if(splitData.length == 2 ){
            try{
                material = Material.matchMaterial(splitData[0]);
            } catch(Exception e){
                System.out.println("Material unknown");
            }

            try{
                durability = Integer.parseInt(splitData[1]);
            } catch(Exception e){
                System.out.println("Incorrect format for durability");
            }



        } else {

            try{
                material = Material.matchMaterial(splitData[0]);
            } catch(Exception e){
                System.out.println("Material unknown");
            }

        }

        assert material != null;
        return new ItemStack(material,1, (short) durability);

    }





    public static String itemStackToId(ItemStack itemStack){
        return itemStack.getType()+":"+itemStack.getDurability();
    }

    public static int getRequiredRows(int size){

        int rows = 0;
        if(size<=9)
            rows = 9;
        else if(size<=18)
            rows=18;
        else if(size<=27)
            rows = 27;
        else if(size<=36)
            rows = 36;
        else if(size<=45)
            rows = 45;
        else
            rows = 54;

        return rows;


    }

    public static boolean isSameItemStack(ItemStack item1, ItemStack item2){
        return ((item1.getType()==(item2.getType()) && (item1.getDurability()==item2.getDurability()) && item1.getData().equals(item2.getData()) && item1.getItemMeta().equals(item2.getItemMeta())));
    }

    public static int getAmountofItemStack(ItemStack reference, Player player){
        int count = 0;
        for(ItemStack item : player.getInventory().getContents()){

            if(item!=null)
            if(isSameItemStack(reference,item))
                count+=item.getAmount();

        }
        return count;
    }

    public static boolean fulfillsRequirements(Player player,HashMap<String,Integer> recipe){

        for(Map.Entry<String,Integer> items : recipe.entrySet()){

            CustomItem requiredItem = DataStorage.itemMap.get(items.getKey());
            ItemStack itemStack;
            if(requiredItem!=null){

                itemStack = requiredItem.getItemStack();

            } else {

                itemStack = VariedTools.idToItemStack(items.getKey());
                System.out.println(itemStack);
            }

            int amount = items.getValue();
            int currentAmount = VariedTools.getAmountofItemStack(itemStack,player);
            if(currentAmount<amount)
                return false;

        }

        return true;





    }

    public static HashMap<String,Integer> getFulfilledRecipe(Player player, CustomItem item){
        for(HashMap<String,Integer> recipe : item.getRecipes()){
            if(fulfillsRequirements(player,recipe))
                return recipe;
        }

        return null;

    }

    public static List<CustomItem> getItemsWithoutWorkbench(List<CustomItem> items){
        List<CustomItem> result = new ArrayList<>();

        for(CustomItem item: items){
            if(!item.onlyWorkbench())
                result.add(item);
        }

        return result;

    }

    public static void removeCustomItemFromRecipe(CustomItem item){


        for(Map.Entry<String,CustomItem> customItemEntry : DataStorage.itemMap.entrySet()){

            CustomItem customItem = customItemEntry.getValue();
            List<HashMap<String,Integer>> recipes = customItem.getRecipes();
            if(recipes.isEmpty())
                continue;



            for(HashMap<String,Integer> recipe : recipes){
                if( recipe.get(item.getId())!=null && recipe.get(item.getId())!=0 )
                    recipe.remove(item.getId());

            }






        }
    }




}
