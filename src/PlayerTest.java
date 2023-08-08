import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testTakeDamage() {
        Player player = new Player("Alice", 100, 20, 50, false, false);
        player.takeDamage(30);

        assertEquals(70, player.getHealth());

        player.takeDamage(80);

        assertEquals(0, player.getHealth());
    }

    @Test
    public void testAddGold() {
        Player player = new Player("Bob", 120, 25, 60, false, false);
        player.addGold(30);

        assertEquals(90, player.getPlayerGold());

        player.addGold(70);

        assertEquals(160, player.getPlayerGold());
    }

    @Test
    public void testHealerCompanionHeal() {
        Player player = new Player("Eve", 80, 15, 40, false, false);
        player.healerCompanionHeal();

        assertEquals(90, player.getHealth());

        player.healerCompanionHeal();

        assertEquals(100, player.getHealth());
    }
}
