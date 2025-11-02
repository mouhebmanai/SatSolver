This project is a collection of various algorithms implemented in Java  
to solve the Boolean Satisfiability Problem (SAT).
It includes classic deterministic algorithms and
stochastic local search methods. These algorithm are also split in two types; local search algorithms and Davis Putnam algorithms.
The program is designed to parse boolean formulas in the
DIMACS CNF format and determine if they are satisfiable.
Instances are taken from https://www.cs.ubc.ca/~hoos/SATLIB/benchm.html.

##  <span style="color: blue;"> Parsing  and Formatting </span>
###  Parsing 
For the  parsing of the formulas in DIMACS format, the class ParseCnf can be used
it currently contains the  functions :
* <span style="color: green;">parseCnf(String filePath, int MaxN, int MAxC) </span>:  parses a formula in DIMACS format, but limit the number of variables
to MaxN, and the number of literals in each clause to MaxC.

* <span style="color: green;">  ReadCNFFilePath() </span>: a simple wrapper for a reader that would read a filepath to cnf formula
###   Formatting 
formulas are defined as CnfFormula objects , they contain 5 components
* Type : gives the maximum number of variables throughout the clauses
* A List of clauses (which in turn are lists of literals )
* A set of variables : the variables present in the formula
* the number of variables
* the number of clauses
##  <span style="color: blue;"> SatGenerator </span>
SatGenerator is a small help program, that bases itself on taking (or generating) an assignment as a parameter. For a given number of values per formula,number of clauses, number of variables per clause and some seed it generated a formula which is satisfied by that given assignment.
##  <span style="color: blue;"> Presented algorithms </span>

In this project a variation randomized and deterministic algorithms is applied.

They all implement the interface CNFSATSolver in which common needed procedures are defined. Currently, they implement 
the procedures  <span style="font-weight: bold;">solve(CnfFormula) </span> which should return a tuple of truth value and certificate, in case a satisfiable 
assignment is found. They also implement the procedure <span style="font-weight: bold;">Output(CnfFormula) </span> which, apart from solving, gives out some insight about Benchmarking anf outputs the 
formatted satisfying assignment, if found.

The following UML diagram show the structure of the currently presented algorithms, with a more thorough description below.
It will be extended with the other algorithms, once they are implemented. 

![UML_overview.png](UML_overview.png)

###  Randomized Algorithms 
They all inherit the class RandomizedAlgorithms and heavily  rely on functions defined there. \
Presented algorithms :
* Papadimitirou's algorithm (Local Search (_LS_)): implement papadimitriou's algorithm for 2-SAT
  (A simple and elegant randomized algorithm which "only" run in quadratic time)
* Schoening's algorithm (_LS_): a simple algorithm implemented in two different classes 
  * pureSCH : here the pure algorithm based on complete randomness is presented
  * SCH_withH : some other heuristics are added to the algorithm
* PPSZ (_DP_):  improvement on the previous algorithm that uses resolution
* Hertli (_DP_): generalizes PPSZ's unique 3 and 4-SAT bounds to the general case by introducing
 S-implication instead of resolution.


###   Deterministic Algorithms
They all inherit the class DeterministicAlgorithms\
Presented algorithms :
* DPLL (_DP_): Davis-Putnam-Logemann-Loveland: A classic, complete, 
backtracking-based algorithm that is guaranteed to find a solution.
* Dantsin and al. (_LS_): A deterministic algorithm for k-SAT that runs in $(O((1.5)^n)$ for 3-SAT and is based on derandomization.


