package storage;

import model.MenuItem;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class InMemoryMenuItem extends AbstractInMemory<MenuItem> implements IMenuItem {

    private final ArrayList<MenuItem> matchingMenuItems = new ArrayList<>();
    private final ArrayList<MenuItem> availableMenuItems = new ArrayList<>();

    public InMemoryMenuItem(){
        sampleMenuData();
    }

    private void sampleMenuData(){
        create(new MenuItem(0, "Fried Chicken", 70.00, "Main", true));
        create(new MenuItem(0, "Nuggets", 70.00, "Main", true));
        create(new MenuItem(0, "Fish Fillet", 50.00, "Side", true));
        create(new MenuItem(0, "McCafe", 20.00, "Drink", true));
        create(new MenuItem(0, "CokeFloat", 20.00, "Drink", true));
        create(new MenuItem(0, "Sundae", 20.00, "Drink", true));
        create(new MenuItem(0, "McFlurry", 20.00, "Drink", true));
    }

    @Override
    protected int getEntityId(MenuItem menuItem){
        return menuItem.getItemId();
    }

    @Override
    protected void setEntityId(MenuItem menuItem, int itemId){
        menuItem.setItemId(itemId);
    }

    @Override
    public ArrayList<MenuItem> findByCategory(String targetCategory){
        matchingMenuItems.clear();
        for(MenuItem currentItem : storage.values()){
            if(currentItem.getCategory().equals(targetCategory)){
                matchingMenuItems.add(currentItem);
            }
        }
        return matchingMenuItems;
    }

    @Override
    public ArrayList<MenuItem> findAvailable(){
        availableMenuItems.clear();
        for(MenuItem currentItem : storage.values()){
            if(currentItem.isAvailable()){
                availableMenuItems.add(currentItem);
            }
        }
        return availableMenuItems;
    }
}
