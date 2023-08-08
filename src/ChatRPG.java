import java.util.Random;
import java.util.Scanner;
/**
 * Represents a game entity with basic attributes such as name, health, damage, and gold reward.
 */
interface GameEntity {
    String getName();
    int getHealth();
    int getDamage();
    int getGoldReward();
    void takeDamage(int damage);
}

/**
 * A class that processes commands given by the player in the game.
 */
class CommandProcessor {
    private static final int WARRIOR_EXTRA_DAMAGE_MIN = 5;
    private static final int WARRIOR_EXTRA_DAMAGE_MAX = 15;
    private static final Random random = new Random();

    private final Player player;
    private final Monster[] monsters;
    private Companion[] companions;
    private final Shop shop;
    private final Room room;

    private final Scanner scanner;

    /**
     * Constructs a CommandProcessor object with the provided game entities and initializes the scanner.
     *
     * @param player The player in the game.
     * @param monsters The array of monsters in the game.
     * @param companions The array of companions available in the game.
     * @param shop The shop where the player can buy weapons and armor.
     * @param room The room where the player can explore and encounter monsters.
     */
    public CommandProcessor(Player player, Monster[] monsters, Companion[] companions, Shop shop, Room room) {
        this.player = player;
        this.monsters = monsters;
        this.companions = companions;
        this.shop = shop;
        this.room = room;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Processes the command based on the provided choice.
     *
     * @param choice The player's choice as an integer.
     */
    public void processCommand(int choice) {
        switch (choice) {
            case 1:
                explore();
                break;
            case 2:
                player.displayStats();
                break;
            case 3:
                recruitCompanion();
                break;
            case 4:
                visitShop();
                break;
            case 5:
                System.out.println("Thanks for playing! Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    /**
     * Allows the player to explore the current room, encountering monsters or attempting to run away.
     */
    private void explore() {
        int roomIndex = random.nextInt(room.getRooms().length);
        System.out.println("\nYou are in a " + room.getRooms()[roomIndex] + ". What would you like to do?");
        System.out.println("1. Attack monsters");
        System.out.println("2. Run away");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                exploreRoom();
                break;
            case 2:
                System.out.println("You try to run away!");
                if (random.nextDouble() < 0.5) {
                    System.out.println("You successfully escape!");
                } else {
                    System.out.println("The monsters block your escape!");
                    int monsterIndex = random.nextInt(monsters.length);
                    Monster monster = monsters[monsterIndex];
                    int monsterAttack = random.nextInt(monster.getDamage());
                    player.takeDamage(monsterAttack);
                    System.out.println("The " + monster.getName() + " attacks you for " + monsterAttack + " damage.");
                }
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    /**
     * Allows the player to explore the current room and encounter a random monster.
     */
    private void exploreRoom() {
        int monsterIndex = random.nextInt(monsters.length);
        Monster monster = monsters[monsterIndex];

        System.out.println("\nYou encounter a " + monster.getName() + "!");

        while (monster.getHealth() > 0 && player.getHealth() > 0) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Attack");
            System.out.println("2. Run");

            int choice = scanner.nextInt();
            int monsterAttack;
            switch (choice) {
                case 1:
                    int playerAttack = random.nextInt(player.getDamage());
                    monsterAttack = random.nextInt(monster.getDamage());

                    System.out.println("You attack the " + monster.getName() + " for " + playerAttack + " damage.");
                    monster.takeDamage(playerAttack);

                    System.out.println("The " + monster.getName() + " attacks you for " + monsterAttack + " damage.");
                    player.takeDamage(monsterAttack);

                    if (player.isHasHealerCompanion()) {
                        System.out.println("Your Healer companion restores your health by 10.");
                        player.healerCompanionHeal();
                    }

                    if (player.isHasWarriorCompanion()) {
                        int warriorAttack = random.nextInt(WARRIOR_EXTRA_DAMAGE_MAX - WARRIOR_EXTRA_DAMAGE_MIN + 1) + WARRIOR_EXTRA_DAMAGE_MIN;
                        System.out.println("Your Warrior companion attacks the " + monster.getName() + " for " + warriorAttack + " damage.");
                        monster.takeDamage(warriorAttack);
                    }
                    break;
                case 2:
                    System.out.println("You try to run away!");
                    if (random.nextDouble() < 0.5) {
                        System.out.println("You successfully escape!");
                        return;
                    } else {
                        System.out.println("The " + monster.getName() + " blocks your escape!");
                        monsterAttack = random.nextInt(monster.getDamage());
                        player.takeDamage(monsterAttack);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        if (player.getHealth() > 0) {
            System.out.println("\nCongratulations! You defeated the " + monster.getName() + " and gained " + monster.getGoldReward() + " gold.");
            player.addGold(monster.getGoldReward());
        } else {
            System.out.println("\nYou were defeated by the " + monster.getName() + ". Game Over!");
            System.exit(0);
        }
    }

    /**
     * Allows the player to recruit a companion to join their journey.
     */
    protected void recruitCompanion() {
        System.out.println("\nYou found someone willing to join your journey!");

        if (player.hasMaxCompanions()) {
            System.out.println("You already have two companions. You cannot recruit more.");
            return;
        }

        System.out.println("Choose a companion to recruit:");
        System.out.println("1. Healer (Restores health)");
        System.out.println("2. Warrior (Deals extra damage)");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                if (!player.isHasHealerCompanion()) {
                    player.setHasHealerCompanion(true);
                    System.out.println("You recruited a Healer companion!");
                } else {
                    System.out.println("You already have a Healer companion.");
                }
                break;
            case 2:
                if (!player.isHasWarriorCompanion()) {
                    player.setHasWarriorCompanion(true);
                    System.out.println("You recruited a Warrior companion!");
                } else {
                    System.out.println("You already have a Warrior companion.");
                }
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    /**
     * Allows the player to visit the shop and buy weapons or armor.
     */
    private void visitShop() {
        System.out.println("\nWelcome to the Shop! What would you like to buy?");
        System.out.println("1. Weapons");
        System.out.println("2. Armor");
        System.out.println("3. Exit shop");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                shop.buyWeapon(player);
                break;
            case 2:
                shop.buyArmor(player);
                break;
            case 3:
                System.out.println("Thanks for visiting the Shop!");
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
}

/**
 * RPGGame class represents the main class for running the RPG Game.
 */
public class ChatRPG {
    private static final int INITIAL_PLAYER_HEALTH = 100;
    private static final int INITIAL_PLAYER_DAMAGE = 20;
    private static final int INITIAL_PLAYER_GOLD = 0;

    /**
     * Main method to start the RPG Game.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the RPG Game!\n Enter your name: ");
        String playerName = scanner.nextLine();

        Player player = new Player(playerName, INITIAL_PLAYER_HEALTH, INITIAL_PLAYER_DAMAGE,
                INITIAL_PLAYER_GOLD, false, false);

        while (player.getHealth() > 0) {
            Monster[] monsters = createMonsters();
            Companion[] companions = createCompanions();
            Shop shop = new Shop();
            Room room = new Room();

            System.out.println("Hello, " + player.getName() + "! Your journey begins now.");
            CommandProcessor commandProcessor = new CommandProcessor(player, monsters, companions, shop, room);

            while (player.getHealth() > 0) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Explore");
                System.out.println("2. Check stats");
                System.out.println("3. Recruit a companion");
                System.out.println("4. Visit the shop");
                System.out.println("5. Quit game");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                commandProcessor.processCommand(choice);
            }

            System.out.println("Game Over! Your journey has come to an end.");
        }
    }

    /**
     * Creates an array of Monsters with predefined attributes.
     * @return An array of Monster objects.
     */
    private static Monster[] createMonsters() {
        String[] monsters = { "Goblin", "Orc", "Dragon" };
        int[] monsterHealth = { 30, 50, 100 };
        int[] monsterDamage = { 5, 10, 20 };
        int[] monsterGold = { 10, 20, 50 };

        Monster[] monsterArray = new Monster[monsters.length];
        for (int i = 0; i < monsters.length; i++) {
            monsterArray[i] = new Monster(monsters[i], monsterHealth[i], monsterDamage[i], monsterGold[i]);
        }
        return monsterArray;
    }

    /**
     * Creates an array of Companions with predefined types.
     * @return An array of Companion objects.
     */
    private static Companion[] createCompanions() {
        Companion[] companions = new Companion[2];
        companions[0] = new Companion("Healer");
        companions[1] = new Companion("Warrior");
        return companions;
    }
}

/**
 * Represents a Monster in the game.
 */
class Monster implements GameEntity {
    private final String name;
    private int health;
    private final int damage;
    private final int goldReward;

    /**
     * Creates a new Monster with the specified attributes.
     *
     * @param name       The name of the monster.
     * @param health     The health points of the monster.
     * @param damage     The damage points the monster can inflict.
     * @param goldReward The amount of gold rewarded when the monster is defeated.
     */
    public Monster(String name, int health, int damage, int goldReward) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.goldReward = goldReward;
    }

    /**
     * Get the name of the monster.
     *
     * @return The name of the monster.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the health points of the monster.
     *
     * @return The health points of the monster.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the damage points the monster can inflict.
     *
     * @return The damage points of the monster.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get the amount of gold rewarded when the monster is defeated.
     *
     * @return The amount of gold reward.
     */
    public int getGoldReward() {
        return goldReward;
    }

    /**
     * Inflicts damage to the monster, reducing its health points.
     * If the health points drop below 0, the health is set to 0.
     *
     * @param damage The amount of damage to be inflicted.
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
}

/**
 * Represents a Player in the game.
 */
class Player implements GameEntity {
    private final String playerName;
    private int playerHealth;
    private int playerDamage;
    private int playerGold;
    private boolean hasHealerCompanion;
    private boolean hasWarriorCompanion;

    /**
     * Creates a new Player with the specified attributes.
     *
     * @param playerName         The name of the player.
     * @param playerHealth       The health points of the player.
     * @param playerDamage       The damage points the player can inflict.
     * @param playerGold         The amount of gold the player possesses.
     * @param hasHealerCompanion Indicates if the player has a healer companion.
     * @param hasWarriorCompanion Indicates if the player has a warrior companion.
     */
    public Player(String playerName, int playerHealth, int playerDamage, int playerGold,
                  boolean hasHealerCompanion, boolean hasWarriorCompanion) {
        this.playerName = playerName;
        this.playerHealth = playerHealth;
        this.playerDamage = playerDamage;
        this.playerGold = playerGold;
        this.hasHealerCompanion = hasHealerCompanion;
        this.hasWarriorCompanion = hasWarriorCompanion;
    }

    /**
     * Get the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return playerName;
    }

    /**
     * Get the health points of the player.
     *
     * @return The health points of the player.
     */
    public int getHealth() {
        return playerHealth;
    }

    /**
     * Inflicts damage to the player, reducing their health points.
     * If the health points drop below 0, the health is set to 0.
     *
     * @param damage The amount of damage to be inflicted.
     */
    public void takeDamage(int damage) {
        playerHealth -= damage;
        if (playerHealth < 0) {
            playerHealth = 0;
        }
    }

    /**
     * Get the damage points the player can inflict.
     *
     * @return The damage points of the player.
     */
    public int getDamage() {
        return playerDamage;
    }

    /**
     * Get the amount of gold the player possesses.
     *
     * @return The amount of gold possessed by the player.
     */
    @Override
    public int getGoldReward() {
        return 0;
    }

    /**
     * Adds a specified amount of gold to the player's total gold.
     *
     * @param amount The amount of gold to be added.
     */
    public void addGold(int amount) {
        playerGold += amount;
    }

    /**
     * Get the amount of gold possessed by the player.
     *
     * @return The amount of gold possessed by the player.
     */
    public int getPlayerGold() {
        return playerGold;
    }

    /**
     * Check if the player has a healer companion.
     *
     * @return True if the player has a healer companion; otherwise, false.
     */
    public boolean isHasHealerCompanion() {
        return hasHealerCompanion;
    }

    /**
     * Set whether the player has a healer companion.
     *
     * @param hasHealerCompanion True to indicate the player has a healer companion; otherwise, false.
     */
    public void setHasHealerCompanion(boolean hasHealerCompanion) {
        this.hasHealerCompanion = hasHealerCompanion;
    }

    /**
     * Check if the player has a warrior companion.
     *
     * @return True if the player has a warrior companion; otherwise, false.
     */
    public boolean isHasWarriorCompanion() {
        return hasWarriorCompanion;
    }

    /**
     * Set whether the player has a warrior companion.
     *
     * @param hasWarriorCompanion True to indicate the player has a warrior companion; otherwise, false.
     */
    public void setHasWarriorCompanion(boolean hasWarriorCompanion) {
        this.hasWarriorCompanion = hasWarriorCompanion;
    }

    /**
     * Check if the player has both a healer and a warrior companion.
     *
     * @return True if the player has both companions; otherwise, false.
     */
    public boolean hasMaxCompanions() {
        return hasHealerCompanion && hasWarriorCompanion;
    }

    /**
     * Heals the player by increasing their health points.
     */
    public void healerCompanionHeal() {
        playerHealth += 10;
    }

    /**
     * Displays the statistics of the player, including health, damage, gold, and companions.
     */
    public void displayStats() {
        System.out.println("\n----- " + playerName + "'s Stats -----");
        System.out.println("Health: " + playerHealth);
        System.out.println("Damage: " + playerDamage);
        System.out.println("Gold: " + playerGold);
        System.out.println("Companions: ");
        if (hasHealerCompanion) {
            System.out.println("- Healer");
        }
        if (hasWarriorCompanion) {
            System.out.println("- Warrior");
        }
        System.out.println("-------------------------");
    }

    /**
     * Set the amount of gold possessed by the player.
     *
     * @param gold The amount of gold to be set.
     */
    public void setPlayerGold(int gold) {
        this.playerGold = gold;
    }

    /**
     * Set the damage points of the player's weapon.
     *
     * @param weaponDamage The damage points of the weapon.
     */
    public void setPlayerDamage(int weaponDamage) {
        this.playerDamage = weaponDamage;
    }

    /**
     * Set the health points of the player.
     *
     * @param health The health points to be set.
     */
    public void setPlayerHealth(int health) {
        this.playerHealth = health;
    }
}

/**
 * Represents a companion that can be owned by the player.
 */
class Companion {
    private final String type;

    /**
     * Creates a new Companion with the specified type.
     *
     * @param type The type of companion.
     */
    public Companion(String type) {
        this.type = type;
    }

    /**
     * Get the type of the companion.
     *
     * @return The type of the companion.
     */
    public String getType() {
        return type;
    }
}

/**
 * Represents an item available in the shop.
 */
class ShopItem {
    private final String name;
    private final int price;
    private final int damage;
    private final int defense;

    /**
     * Constructs a new ShopItem object with the specified properties.
     *
     * @param name    The name of the shop item.
     * @param price   The price of the shop item in gold coins.
     * @param damage  The damage value of the weapon (for weapons) or 0 (for armor).
     * @param defense The defense value of the armor (for armor) or 0 (for weapons).
     */
    public ShopItem(String name, int price, int damage, int defense) {
        this.name = name;
        this.price = price;
        this.damage = damage;
        this.defense = defense;
    }

    /**
     * Returns the name of the shop item.
     *
     * @return The name of the shop item.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the shop item.
     *
     * @return The price of the shop item in gold coins.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the damage value of the weapon (for weapons) or 0 (for armor).
     *
     * @return The damage value of the weapon or 0 for armor.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the defense value of the armor (for armor) or 0 (for weapons).
     *
     * @return The defense value of the armor or 0 for weapons.
     */
    public int getDefense() {
        return defense;
    }
}

/**
 * Represents a shop where players can buy weapons and armors.
 */
class Shop {
    private ShopItem[] weapons;
    private ShopItem[] armors;

    /**
     * Constructs a new Shop object and initializes the available shop items.
     */
    public Shop() {
        initializeShopItems();
    }

    /**
     * Initializes the weapons and armors available in the shop with default values.
     */
    private void initializeShopItems() {
        weapons = new ShopItem[]{
                new ShopItem("Weapon 1", 30, 30, 0),
                new ShopItem("Weapon 2", 50, 40, 0),
                new ShopItem("Weapon 3", 100, 50, 0)
        };

        armors = new ShopItem[]{
                new ShopItem("Armor 1", 20, 0, 20),
                new ShopItem("Armor 2", 40, 0, 30),
                new ShopItem("Armor 3", 80, 0, 40)
        };
    }

    /**
     * Allows the player to buy a weapon from the shop.
     *
     * @param player The player object making the purchase.
     */
    public void buyWeapon(Player player) {
        System.out.println("\nAvailable Weapons:");
        for (int i = 0; i < weapons.length; i++) {
            ShopItem weapon = weapons[i];
            System.out.println((i + 1) + ". " + weapon.getName() + " (Damage: " + weapon.getDamage() + ") - " + weapon.getPrice() + " gold");
        }
        System.out.println((weapons.length + 1) + ". Cancel");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice >= 1 && choice <= weapons.length) {
            int weaponIndex = choice - 1;
            ShopItem weapon = weapons[weaponIndex];
            int weaponPrice = weapon.getPrice();
            int weaponDamage = weapon.getDamage();

            if (player.getPlayerGold() >= weaponPrice) {
                player.setPlayerGold(player.getPlayerGold() - weaponPrice);
                player.setPlayerDamage(weaponDamage);
                System.out.println("You bought the " + weapon.getName() + ". Your damage increased to " + weaponDamage + ".");
            } else {
                System.out.println("Not enough gold to buy the weapon.");
            }
        } else if (choice == weapons.length + 1) {
            System.out.println("You canceled the purchase.");
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }

    /**
     * Allows the player to buy an armor from the shop.
     *
     * @param player The player object making the purchase.
     */
    public void buyArmor(Player player) {
        System.out.println("\nAvailable Armor:");
        for (int i = 0; i < armors.length; i++) {
            ShopItem armor = armors[i];
            System.out.println((i + 1) + ". " + armor.getName() + " (Defense: " + armor.getDefense() + ") - " + armor.getPrice() + " gold");
        }
        System.out.println((armors.length + 1) + ". Cancel");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice >= 1 && choice <= armors.length) {
            int armorIndex = choice - 1;
            ShopItem armor = armors[armorIndex];
            int armorPrice = armor.getPrice();
            int armorDefense = armor.getDefense();

            if (player.getPlayerGold() >= armorPrice) {
                player.setPlayerGold(player.getPlayerGold() - armorPrice);
                player.setPlayerHealth(player.getHealth() + armorDefense);
                System.out.println("You bought the " + armor.getName() + ". Your defense increased to " + player.getHealth() + ".");
            } else {
                System.out.println("Not enough gold to buy the armor.");
            }
        } else if (choice == armors.length + 1) {
            System.out.println("You canceled the purchase.");
        } else {
            System.out.println("Invalid choice. Try again.");
        }
    }
}

/**
 * Represents a room containing different locations or areas in the game world.
 */
class Room {
    private final String[] rooms;
    /**
     * Constructs a new Room object and initializes the available room names.
     */
    public Room() {
        rooms = new String[]{"Forest", "Cave", "Castle"};
    }

    /**
     * Returns an array of available room names.
     *
     * @return An array of strings representing available room names.
     */
    public String[] getRooms() {
        return rooms;
    }
}