import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Run {
	public static void main(String argv[])
	{
		String virtuosoHost = "";
		String dbPort = "";
		String username = "";
		String pwd = "";
		String RDFFile = "/home/amir/CPMeta.RDF";
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
			else if(keyValue[0].toLowerCase().equals("host")) {
				virtuosoHost = keyValue[1];
			}
			else if(keyValue[0].toLowerCase().equals("user")) {
				username = keyValue[1];
			}
			else if(keyValue[0].toLowerCase().equals("password")) {
				pwd = keyValue[1];
			}
			else if(keyValue[0].toLowerCase().equals("port")) {
				dbPort = keyValue[1];
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

		if(RDFFile=="" || virtuosoHost=="" || dbPort=="" || username=="" || pwd=="" || mode=="") {
			System.out.println("\n Data File not provided. Usage is given below");
			System.out.println("\n java -cp Virtuosotest.jar Run data=<data_file> repeat=<test_iterations> host=<server> port=<server_port> user=<user_name> password=<passord>  warmup=<warmup_iterations> count=<on/off> mode=<geographica/general> ");
			System.out.println("\n java -cp Virtuosotest.jar Run data=/home/CPMeta.rdf repeat=20 warmup=20 host=localhost port=1111 user=dba password=dba count=on mode=geographica");
			return;
		}
		
		ArrayList<String> resultSet1 = VirtuosoTestDriver.runVirtuosoRDF4JTest(virtuosoHost,dbPort,username,pwd,RDFFile,queryIterations,warmUPIterations,count,mode);
		
		ArrayList<String> resultSet2 = VirtuosoTestDriver.runVirtuosoJDBCTest(virtuosoHost, dbPort, username, pwd,queryIterations,count,mode);
		
		/*for (String res:resultSet1) {
			System.out.println(res);			
		}
		
		for (String res:resultSet2) {
			System.out.println(res);			
		}*/
		
		System.out.println("*********************  Test Peformance *********************");			
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter("VirtuosoPerfs.txt",false)); 			
			//write the result on console and file. Change here to write in a file or in a file
			for (String res:resultSet1) {
				System.out.println(res);	
				out.write(res); 
				out.write("\n"); 
			}
			System.out.println();
			out.write("\n");
			for (String res:resultSet2) {
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
