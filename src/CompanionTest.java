import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompanionTest {

    @Test
    public void testGetType() {
        Companion healerCompanion = new Companion("Healer");
        assertEquals("Healer", healerCompanion.getType());

        Companion warriorCompanion = new Companion("Warrior");
        assertEquals("Warrior", warriorCompanion.getType());
    }
}
