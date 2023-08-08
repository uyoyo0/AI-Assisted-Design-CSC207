import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopItemTest {

    @Test
    public void testGetName() {
        ShopItem item = new ShopItem("TestItem", 50, 20, 30);
        assertEquals("TestItem", item.getName(), "Item name should match.");
    }

    @Test
    public void testGetPrice() {
        ShopItem item = new ShopItem("TestItem", 50, 20, 30);
        assertEquals(50, item.getPrice(), "Item price should match.");
    }

    @Test
    public void testGetDamage() {
        ShopItem weapon = new ShopItem("TestWeapon", 100, 40, 0);
        ShopItem armor = new ShopItem("TestArmor", 80, 0, 20);

        assertEquals(40, weapon.getDamage(), "Weapon damage should match.");
        assertEquals(0, armor.getDamage(), "Armor damage should match.");
    }

    @Test
    public void testGetDefense() {
        ShopItem weapon = new ShopItem("TestWeapon", 100, 40, 0);
        ShopItem armor = new ShopItem("TestArmor", 80, 0, 20);

        assertEquals(0, weapon.getDefense(), "Weapon defense should match.");
        assertEquals(20, armor.getDefense(), "Armor defense should match.");
    }
}
