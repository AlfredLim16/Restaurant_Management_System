package storage;

import model.MenuItem;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public interface IMenuItem {

    void create(MenuItem item);
    MenuItem get(int itemId);
    ArrayList<MenuItem> getAll();
    void update(MenuItem item);
    void delete(int itemId);

    ArrayList<MenuItem> findByCategory(String category);
    ArrayList<MenuItem> findAvailable();
}
