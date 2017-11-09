package mutantSet;

/**
 * @author phantom
 */
public class Mutant {
    private String fullName;//变异体完整的类名
    public Mutant(String fullName) {setFullName(fullName);}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
