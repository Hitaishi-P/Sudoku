
Sudoku

A Java console Sudoku game I made for fun during my freshman year. It generates a random puzzle (4x4 or 9x9), lets you pick a difficulty, and then you solve it right in the terminal — it checks your answers against the solution as you go.

Features


Choose between a 4x4 or 9x9 grid
Pick a difficulty: easy, medium, or hard (controls how many cells are filled in at the start)
Randomly generates a fresh, valid, fully-solvable puzzle every time using backtracking
Play interactively: enter a move as row,column,number and it tells you if you're right
The puzzle updates live as you get answers correct
Detects when the board is fully solved


How to run


Clone the repo:

bash:  git clone https://github.com/Hitaishi-P/Sudoku.git

Compile it:

bash:   javac cmsc256/Sudoku.java


Run it:

bash:  java cmsc256.Sudoku

How to play

When you start it up, you'll be asked to pick a grid size and difficulty. Once the puzzle prints, enter your guesses in the form:

row,column,number

For example, typing 3,5,9 places 9 in row 3, column 5 (rows/columns start at 1, not 0). Keep going until every cell is filled in — the game tells you if a guess is right or wrong, and lets you type quit to stop early.

Why I made this

Wanted a way to practice backtracking algorithms and 2D arrays in Java, and Sudoku felt like a fun way to actually see it work instead of just printing numbers to a console. Built it in cmsc256 (intro CS course) my freshman year.

Possible future improvements

GUI version instead of console-only
Difficulty that guarantees a unique solution (right now it just removes a set number of cells)
Hint system
Save/load progress