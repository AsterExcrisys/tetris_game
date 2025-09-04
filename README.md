# 🎮 TetrisGame

A modern, fully-featured **Tetris game built with JavaFX**, offering classic gameplay with added flair: music, sound effects, advanced mechanics, and local leaderboard support.

---

## 🚀 Features

* 🎵 **Dynamic Music & SFX**
  Choose from multiple music tracks and enjoy immersive sound effects during gameplay. All audio settings are adjustable in the settings menu.

* 📈 **Level & Score System**
  Progress through levels as you clear lines, with increasing difficulty and scoring.

* 🔍 **Preview & Wall Kicks**
  See the next upcoming pieces with a preview system. Modern wall kick mechanics are implemented, allowing intuitive and responsive rotation near walls.

* 🔄 **7-Bag Randomizer & Hold System**
  True 7-bag tetromino generation ensures fairness and balance. Use the hold function strategically to store or swap pieces.

* 🏆 **Local Leaderboard**
  High scores are stored locally using SQLite. Challenge yourself and climb your personal leaderboard!

* ⚙️ **Settings Menu**
  Customize your experience:

  * Adjust **music and sound volume**
  * Toggle **fixed or randomized tetromino colors** for increased difficulty and visual challenge

---

## 🛠️ Requirements

* Java 21 or higher
* JavaFX SDK
* SQLite JDBC driver (bundled or separately)

---

## 📦 Setup Instructions

1. **Clone the Repository:**

```bash
git clone https://github.com/AsterExcrisys/tetris_game.git
cd tetris_game
```

2. **Build & Run (using Maven):**

```bash
mvn clean compile
mvn javafx:run
```

Or open the project in your favorite IDE (e.g., IntelliJ IDEA or Eclipse) and run the `MainApplication` class.

---

## 🧩 Controls

| Action            | Key      |
| ----------------- | -------- |
| Move Left / Right | A / D    |
| Soft Drop         | S        |
| Hard Drop         | W        |
| Rotate (CCW / CW) | Q / E    |
| Hold Piece        | H        |
| Pause / Resume    | P        |

---

## 🗃️ Save Data

* **Leaderboard data** is stored locally using SQLite in a file called `leaderboard.db` in the application 'data' directory.
* **Settings** are not saved in a configuration file (for now at least), so you will have to reset everytime.

---

## 🧪 Development Notes

* Game logic is separated from UI logic to ensure maintainability.
* Tetromino rendering, movement, and collision detection follow modern Tetris guidelines.
* Audio is handled via JavaFX's built-in media player, with support for multiple concurrent tracks.
* The 7-bag randomizer guarantees that all 7 pieces appear once per cycle for fair and predictable randomness.

---

## 📝 License

**GNU General Public License v3.0 (GPLv3)**
This project is licensed under the terms of the GPLv3. You are free to use, modify, and distribute this software, provided that any derivative works are also licensed under the GPLv3. See the [LICENSE](LICENSE) file for full details.

---

## 🙌 Credits

* JavaFX for GUI and multimedia support
* SQLite JDBC for local database integration
* Music/Sound assets
