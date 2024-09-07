package hiflsklasse;

public class BasicWekaTools
{
	  // Valid combinations of evaluators and search methods
    public static final String CFS_BESTFIRST = "cfssubseteval_bestfirst";
    public static final String CFS_GREEDYSTEPWISE = "cfssubseteval_greedystepwise";
    public static final String CFS_GENETICSEARCH = "cfssubseteval_geneticsearch";
    public static final String INFOGAIN_RANKER = "infogain_ranker";
    public static final String GAINRATIO_RANKER = "gainratio_ranker";
	public BasicWekaTools()
	{}
	public boolean isValidWorkdir(String dir)
	{
		if ((dir.endsWith("endtest")) || (dir.endsWith("long")) || (dir.endsWith("workflowname")))
			return false;
		return true;
		
	}
}
