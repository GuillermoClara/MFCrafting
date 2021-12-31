package guillec.mfcrafting.objects;

import guillec.mfcrafting.files.MFile;
import guillec.mfcrafting.utils.DataStorage;
import guillec.mfcrafting.utils.VariedTools;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomItem implements ConfigurationSerializable {
    private String id;
    private String name;
    private String type;
    private ItemStack itemStack;
    private boolean needsWorkbench;
    public static int count=0;
    private HashMap<String,Integer> recipe = new HashMap<>();
    private List<HashMap<String,Integer>> recipes = new ArrayList<>();

    public CustomItem(String name, ItemStack itemStack, List<String> recipe, String type, boolean workbench){
        this.name = name;
        this.itemStack = itemStack;
        this.recipes = this.arrayToRecipes(recipe);
        System.out.println("Tamano de recetas: "+recipes.size());
        this.type = type;
        this.needsWorkbench = workbench;
        count++;
    }

    public CustomItem(String name, ItemStack itemStack, List<String> recipe){
        this.name = name;
        this.itemStack = itemStack;
        this.recipes = this.arrayToRecipes(recipe);
        this.needsWorkbench = false;
        count++;
    }


    public static CustomItem deserialize(Map<String,Object> args){
        String name = (String)args.get("name");
        ItemStack itemStack = (ItemStack) args.get("itemStack");
        String type = (String)args.get("type");
        List<String> items = (List<String>) args.get("recipe");
        boolean workbench = (Boolean)args.get("workbench");

        return new CustomItem(name,itemStack,items,type,workbench);
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",this.name);
        result.put("itemStack", this.itemStack);
        result.put("recipe", this.recipesToArray());
        result.put("type", this.type);
        result.put("workbench", this.needsWorkbench);


        return result;
    }


    public List<String> recipeToArray(){

        List<String> result = new ArrayList<>();
        for(Map.Entry<String,Integer> entries : this.recipe.entrySet()){

            String builder = entries.getKey()+","+entries.getValue();
            result.add(builder);
            System.out.println(builder);
        }

        return result;


    }

    public List<String> recipeToArray(HashMap<String,Integer> map){

        List<String> result = new ArrayList<>();
        for(Map.Entry<String,Integer> entries : map.entrySet()){

            String builder = entries.getKey()+","+entries.getValue();
            result.add(builder);
            System.out.println(builder);
        }

        return result;


    }


    public List<HashMap<String,Integer>> arrayToRecipes(List<String> items){
        List<HashMap<String,Integer>> result = new ArrayList<>();

        for(String recipe: items){

            List<String> recipeList = new ArrayList<>(Arrays.asList(recipe.split(";")));
            HashMap<String,Integer> recipeMap = new HashMap<>();
            for(String item: recipeList){
                String identifier = item.substring(0,item.indexOf(","));
                int amount = Integer.parseInt(item.substring(item.indexOf(",")+1));
                recipeMap.put(identifier,amount);


            }
            result.add(recipeMap);


        }
        return result;

    }

    public String recipeToString(HashMap<String,Integer> recipe){
        List<String> recipeAsArray = recipeToArray(recipe);
        int size = recipeAsArray.size();
        String result = "";
        for(int i = 0;i<size;i++){

            if(i<(size-1)){

                result=result+recipeAsArray.get(i)+";";

            } else {
                result=result+recipeAsArray.get(i);
            }

        }

        return result;

    }

    public List<String> recipesToArray(){
        List<String> result = new ArrayList<>();

        for(HashMap<String,Integer> recipeMap : this.recipes){
            result.add(this.recipeToString(recipeMap));
        }

        return result;

    }

    public static List<ItemStack> recipeToItemStacks(HashMap<String,Integer> recipe, MFile lang){
            List<ItemStack> result = new ArrayList<>();

            for(Map.Entry<String,Integer> entry : recipe.entrySet()){

                CustomItem requirement = DataStorage.itemMap.get(entry.getKey());
                if(requirement == null){
                    ItemStack itemStack = VariedTools.idToItemStack(entry.getKey());
                    itemStack.setAmount(entry.getValue());
                    ItemMeta meta = itemStack.getItemMeta();
                    String name = lang.getString("blocks."+entry.getKey());
                    if(name == null){
                        name = itemStack.getType().toString();
                    }


                    meta.setDisplayName("§2"+name);
                    result.add(itemStack);


                } else {

                    ItemStack itemStack = new ItemStack(requirement.getItemStack());
                    itemStack.setAmount(entry.getValue());
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName("§2"+requirement.getName());
                    itemStack.setItemMeta(meta);
                    result.add(itemStack);
                }



            }
            return result;

    }


    public String getId(){
        return this.id;
    }

    public void setId(String identifier){
        this.id = identifier;
    }


    public String getName(){
        return this.name;
    }

    public static List<String> getRequiredItems(HashMap<String,Integer> recipe,String color, MFile lang){
        List<String> result = new ArrayList<>();
        for(Map.Entry<String,Integer> entries : recipe.entrySet()){
            CustomItem item = DataStorage.itemMap.get(entries.getKey());

            String requirement;
            if(item != null){
                requirement = "§"+color + entries.getValue() + "x " + item.getName();
            } else {
                String name = lang.getString("blocks."+entries.getKey());
                if(name == null)
                    requirement = "§"+color + entries.getValue() + "x " + entries.getKey();
                else
                    requirement = "§"+color + entries.getValue() + "x " + name;
            }

            result.add(requirement);

        }
        return result;

    }

    public HashMap<String,Integer> getRecipe(){
        return this.recipe;
    }

    public void setItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack(){
        return this.itemStack;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public List<HashMap<String,Integer>> getRecipes(){
        return this.recipes;
    }

    public boolean onlyWorkbench(){
        return this.needsWorkbench;
    }

    public void setWorkbench(boolean workbench){
        this.needsWorkbench = workbench;
    }


}
