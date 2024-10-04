# Tenner-puzzle
Tenner-Grid
This project implements a solver for the Tenner Grid puzzle using constraint satisfaction problem (CSP) algorithms in Java.

# Problem Statement
The Tenner Grid puzzle involves filling a rectangular grid with numbers from 0 to 9 while satisfying specific constraints. Each row must contain unique numbers, column sums are predefined, and adjacent digits must not repeat. This project aims to solve Tenner Grid puzzles by modeling them as CSPs and implementing three algorithms: simple backtracking, forward checking, and forward checking with the MRV heuristic.

# Project Structure:
This project consists of the following components:

# Backtrack Class:
Contains the implementation of the simple backtracking algorithm for solving Tenner Grid puzzles.

# Cell Class:
Represents a single cell in the Tenner Grid, storing its value and constraints.

# ForwardChecking Class:
Implements the forward checking algorithm for solving Tenner Grid puzzles.

# ForwardCheckingMRV Class:
Implements the forward checking algorithm with the MRV heuristic for solving Tenner Grid puzzles.

# TennerGrid Class:
Contains the main logic for generating Tenner Grid puzzles, applying CSP algorithms, and analyzing performance. It coordinates the interaction between the various algorithms.

# Contents:
"src/": contains the source code of the program in Java.

"README.md": you are reading it right now!
