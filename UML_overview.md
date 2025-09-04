classDiagram
direction BT
class CNFSATSolver {
<<Interface>>

}
class DPLL
class Danstin
class DeterministicAlgorithm
class PPSZ {
  - int s
  - int I
}
class Papadimitriou {
  - Map~Integer, Boolean~ beta_0
  - int Repetitions
}
class RandomizedAlgorithm {
  # Random rand
}
class SCH_withH {
  - int Repetitions
  - boolean DoubleSided
  - boolean ClauseWeighing
  - boolean InitialCleanup
  - boolean SmallestNextClause
  - boolean NextLiteral
}
class SearchWithRadius
class pureSCH {
  - int Repetitions
}

DPLL  -->  DeterministicAlgorithm 
Danstin  -->  DeterministicAlgorithm 
DeterministicAlgorithm  ..>  CNFSATSolver 
PPSZ  -->  RandomizedAlgorithm 
Papadimitriou  -->  RandomizedAlgorithm 
RandomizedAlgorithm  ..>  CNFSATSolver 
SCH_withH  -->  RandomizedAlgorithm 
SearchWithRadius  -->  DeterministicAlgorithm 
pureSCH  -->  RandomizedAlgorithm 
