import java.util.List;

public class FactorizationResult {
    public int totalFactors;
    public String factorsString;      
    public long solutionCount;        
    public List<String> allSolutions; 

    public FactorizationResult(int totalFactors, String factorsString, long solutionCount, List<String> allSolutions) {
        this.totalFactors = totalFactors;
        this.factorsString = factorsString;
        this.solutionCount = solutionCount;
        this.allSolutions = allSolutions;
    }
}