import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Run {
	
	public static void main(String argv[])
	{
		
		REPO_TYPES repoType = REPO_TYPES.NATIVE_SIMPLE;
		//REPO_TYPES repoType = REPO_TYPES.MEMORY_SIMPLE;
		//REPO_TYPES repoType = REPO_TYPES.NATIVE_WITH_LUCENE_INDEX_IN_FILE;
		String dirPath = System.getProperty("user.dir") + "/RDF4JTemp"; //";///home/amir/RDFTemps/RDF4JTemp";
		String RDFFile=""; // = "/home/amir/CPMeta.RDF";
		int queryIterations = 1;
		int warmUPIterations = 20;
		String count = "off";
		String mode = "";
		
		for(String cmdParam: argv) {
			
			String keyValue[] = cmdParam.split("=");
			if(keyValue[0].toLowerCase().equals("data")) {
				RDFFile = keyValue[1];
			}
			else if (keyValue[0].toLowerCase().equals("repeat")) {
				queryIterations = Integer.parseInt(keyValue[1]) ;
			}
			else if (keyValue[0].toLowerCase().equals("warmup")) {
				warmUPIterations = Integer.parseInt(keyValue[1]) ;
			}
			else if (keyValue[0].toLowerCase().equals("count")) {
				count = keyValue[1];
			}
			else if (keyValue[0].toLowerCase().equals("mode")) {
				if(keyValue[1].toLowerCase().equals("general") || keyValue[1].toLowerCase().equals("geographica")) {
					mode = keyValue[1];
				}
			}
		}
		
		if(RDFFile=="" || mode=="") {
			System.out.println("\n Data File not provided. Usage is given below");
			System.out.println("\n java -cp RDF4Jtest.jar Run data=<data_file> repeat=<test_iterations> warmup=<warmup_iterations> count=<on/off> mode=<geographica/general> ");
			System.out.println("\n java -cp RDF4Jtest.jar Run data=/home/CPMeta.rdf repeat=20 warmup=20 count=on mode=geographica ");
			return;
		}
		
		ArrayList<String> resultSet = RDF4JTestDriver.runTest(repoType,RDFFile,dirPath,queryIterations,warmUPIterations,count,mode);
		System.out.println("*********************  Test Peformance *********************");	
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter(dirPath + "/RDF4JPerfs.txt",false)); 			
			//write the result on console and file. Change here to write in a file or in a file
			for (String res:resultSet) {
				System.out.println(res);	
				out.write(res); 
				out.write("\n"); 
			}
			out.close();	
		} 
		
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
	}
	
}
