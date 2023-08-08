# AI-Assisted-Design-CSC207
**Program Summary:**

This Java program implements a simple text-based RPG (Role-Playing Game) where players can explore rooms, encounter monsters, recruit companions, visit a shop, and engage in battles. The program is designed to interact with the player through the command line. Players can make choices to explore, fight, recruit companions, buy equipment, and more. I hope you enjoy the game and find it interesting!

**Use Cases:**
- Players can explore different rooms and encounter monsters.
- Players can engage in turn-based battles with monsters, making decisions on whether to attack or run.
- Players can recruit companions (Healer or Warrior) to aid them in battles.
- Players can visit a shop to buy weapons or armor to improve their stats.
- Players can check their own statistics, such as health, damage, gold, and companions.

**User Story:**
As a player, I want to navigate through various rooms, encounter monsters, recruit companions, and enhance my equipment by visiting a shop. I want to make strategic decisions in battles and manage my resources effectively to progress in the game.

**Design Patterns:**
- **Command Pattern:** The CommandProcessor class processes player commands and uses a switch-case structure to execute different actions based on the player's choice. (https://www.geeksforgeeks.org/command-pattern/)
- **Strategy Pattern:** The different companion types (Healer and Warrior) are represented using the Companion class, utilizing the strategy pattern to define behavior for each type.
- **Factory Pattern:** The createMonsters and createCompanions methods in the main class use the factory pattern to create arrays of Monster and Companion objects respectively.

**Java Version and Testing Framework:**
I am using Java11 and the JUnit testing framework.

**Potential Code Smells**
- **Error Handling:** The code doesn't seem to include robust error handling for unexpected input, exceptions, or edge cases during user interaction. Adding appropriate error handling mechanisms could enhance user experience and program stability.
- I could not sufficiently deal with this due to length requirements.

  
**Clean Architecture/SOLID violations:** There are no violations of CA or SOLID
  

**Other Notes:**
- Many (the majority) of my functions relied on user input from a scanner. Due to the length restrictions, there was no way for me to properly test all of these methods without violating the length requirement. I thus focused the majority of my tests on other types of methods.
- The conversation I had with ChatGPT may seem long but this is simply due to the fact that I had to send the entire codebase to it multiple times throughout our conversations (this is how I dealt with context issues) feel free to skip over that when reading the log.
