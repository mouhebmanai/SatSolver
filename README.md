This project is a collection of various algorithms implemented in Java   <span style="color: grey;">(another version in c may follow) </span>
to solve the Boolean Satisfiability Problem (SAT).
It includes classic deterministic algorithms and
stochastic local search methods. 
The program is designed to parse boolean formulas in the
DIMACS CNF format and determine if they are satisfiable.
Instances are taken from https://www.cs.ubc.ca/~hoos/SATLIB/benchm.html.

##  <span style="color: red;"> Parsing  and Formatting </span
### <span style="color: blue;"> Parsing </span>
For the  parsing of the formulas in DIMACS format, the class ParseCnf can be used
it currently contains the  functions :
* <span style="color: green;">parseCnf(String filePath, int MaxN, int MAxC) </span>:  parses a formula in DIMACS format, but limit the number of variables
to MaxN, and the number of literals in each clause to MaxC.

* <span style="color: green;">  ReadCNFFilePath() </span>: a simple wrapper for a reader that would read a filepath to cnf formula
###  <span style="color: blue;"> Formatting </span>
formulas are defined as CnfFormula objects , they contain 5 components
* Type : gives the maximum number of variables throughout the clauses
* A List of clauses (which in turn are lists of literals )
* A set of variables : the variables present in the formula
* the number of variables
* the number of clauses
##  <span style="color: red;"> Presented algorithms </span>
In this project a variation randomized and deterministic algorithms is applied.
They all implement the interface CNFSATSolver in which common needed procedures are defined.
The following UML diagram show the structure of the currently presented algorithms, with a more thorough description below.
It will be extended with the other algorithms, once they are implemented
![UML_overview.png](UML_overview.png)

### <span style="color: blue;"> Randomized Algorithms </span>
They all inherit the class RandomizedAlgorithms and heavily  rely on functions defined there. \
Presented algorithms :
* Papadimitirou's algorithm : implement papadimitriou's algorithm for 2-SAT
  (A simple and elegant randomized algorithm which "only" run in quadratic time)
* Schoening's algorithm : a simple algorithm implemented in two different classes 
  * pureSCH : here the pure algorithm based on complete randomness is presented
  * SCH_withH : some other heuristics are added to the algorithm
* PPSZ :  improvement on the previous algorithm that uses resolution
* Hertli : to be implemented

###   <span style="color: blue;"> Deterministic Algorithms </span>
* DPLL : Davis-Putnam-Logemann-Loveland: A classic, complete, 
backtracking-based algorithm that is guaranteed to find a solution.
* Danstin and al. : A deterministic algorithm for k-SAT that runs in O((1.5)^n) for 3-SAT and is based on derandomization.
* Moser-Scheder : to be implemented
* Liu : to be implemented

