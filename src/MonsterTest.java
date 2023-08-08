import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonsterTest {

    @Test
    public void testTakeDamage() {
        Monster monster = new Monster("TestMonster", 100, 20, 50);

        // Inflict damage that is less than monster's health
        int damage1 = 30;
        monster.takeDamage(damage1);
        assertEquals(70, monster.getHealth(), "Monster's health should be reduced by damage1.");

        // Inflict damage that is equal to monster's health
        int damage2 = 70;
        monster.takeDamage(damage2);
        assertEquals(0, monster.getHealth(), "Monster's health should be reduced to 0 when damage is equal to health.");

        // Inflict damage that is greater than monster's health
        int damage3 = 50;
        monster.takeDamage(damage3);
        assertEquals(0, monster.getHealth(), "Monster's health should remain at 0 when damage is greater than health.");
    }
}
