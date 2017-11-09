package mutantSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author phantom
 */
public class BinSet {
    private List<Mutant> mutants;
    public BinSet() { mutants = new ArrayList<Mutant>(); }

    public void add (Mutant mutant){mutants.add(mutant);}

    public int size(){return mutants.size();}

    public String getMutantName(int index){return mutants.get(index).getFullName();}

    public void remove(int index){mutants.remove(index);}
}
