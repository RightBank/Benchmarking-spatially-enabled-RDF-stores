# Introduction

The artifacts provided here were developed during a research ([Assessment and Benchmarking of Spatially-Enabled RDF stores for Next Generation of Spatial Data Infrastructure]). The research is aimed at the evaluation of geospatial capabilities (with focus on GeoSPARQL-compliance) of five RDF stores: 

 - Eclipse RDF4J 2.4.2
 - Apache Jena 3.9.0 with GeoSPARQL-Jena 1.0.3
 - Stardog 6.0.1
 - Openlink Virtuoso 8.2 (commercial edition)
 - GraphDB 8.8.0
   
The tests are performed from Java prorgams using the provided Java APIs for each of the RDF stores. Each folder corresponds to the source code and the compiled test programs of the rleated platform. For RDF4J, Jena, Stardog and GraphDB the test prorgams are capable to launch an embeded RDF engine/server  which resides within  the Same JVM. For Virtuoso however, a running Virtuoso server instance is required and the test prorgams require the parameters to the server as command line arguements. 

### How to use the source code

For each platform, a separate folder is available here for compiled test programs as well as source code. The Java files are provided in the `src` folder. Each of these folders has a `Run.java` file which is the starting point of program execution. Each prorgam also require run time command line parameters which are explained below. The library dependencies for each RDF store are different and hence relevant dependecies can be aquired from the folder  <RDF_Store_folder>\test\<RDF_Store_Name>test_lib folder. 

For example the source code for RDF4J could be found in `RDF4J\src`, while the dpendencies are found in `RDF4J\test\RDF4Jtest_lib`. similarly the source code for Jena could be found in `Jena\src`, while the dpendencies are found in `Jena\test\Jenatest_lib`.

## How to use the compiled test programs

The programs have been tested on Microsoft Windwows 7 with Java 1.8.0_31 as welll as on Ubuntu 18.04 with Java 1.8.0_121. The general syntax of running an individual test is:

```sh
$ java -cp <JarName>.jar Run <Command line parameter values>
```
Hence in order to run RDF4J tests, the working directory should be changed to the `RDF4J\test` folder and then following command should be used.

```sh
$ java -cp RDF4Jtest.jar Run data=/home/CPMeta.rdf repeat=3 warmup=10 mode=general count=off
```
while to run Jena tests, the working directory should be changed to the `Jena\test` folder and then following command should be used.

```sh
$ java -cp Jenatest.jar Run data=/home/CPMeta.rdf repeat=3 warmup=10 mode=general count=off
```

Similarly for all other platforms we can proceed so. Each test folder has a related `RDFStore`test_lib folder where the dependent libraries are available. It is however important to note that some command line arguments are available in all tests while a few are available in only some of the tests as explained below.

### Command line swicthes and inputs for test prorgrams

Platform | Switch | Value |
| ------ | ------ | ------ |
|All tests| data  | The value of this switch is complete path to the input data file containing RDF data preferably in RDF/XML format |
|All tests| repeat | The value of this switch is the number of times the Query set is required to be executed |
|All tests| warmup | The value of this switch is the number of times the unrelated Query set is required to be executed to warmup the execution environment |
|All tests| mode | possible values are : `geographica` OR `general`. when the value is general, the GeoSPARQL query set with general scope is selected and when the value is geographica, the query set with scopes limiting to Geographica benchmark queries is selected|
|All tests| count | possible values are : `on` OR `off`. when the value is on, the results of each query are counted and when the value is off then query results are not counted. |
|GraphDB only| config | The value of this switch is complete path to the GraphDB database configuration file in Turtle format. A sample `config.ttl` is availabel in Geographica folder |
|Virtuoso only| host | IP adress of the Virtuoso server |
|Virtuoso port| port | Port number of database connection  port listener on the Virtuoso server |
|Virtuoso only| user | Database username for connection to the Virtuoso server |
|Virtuoso only| password | Database users password for connection to the Virtuoso server |

##### Stardog license file

In order to run Stardog tests or from development environment, a valid stardog license file is required at the root folder. The trial license can be aquired from https://www.stardog.com/download/. The license file should be put in the same folder as Stardogtest.jar.

### Output of the test prorgrams

All prorgrams generate console output consisting of following
- data load time
- comma separated query times for each iteration of each query
- if the count=on was provided at the command line on test startup, than count of results for each query is generated
- if the number of query set repeatition at startup was provided greater than 20,then average of all repeatitions of each query is also produced.

The same console output is also generated in a text file in a temp folder at current path. The name of that text file is of the form `RDFStore`Temp\ `RDFStore`Perfs.txt. Hence for RDF4J the out file is produced at RDF4JTemp\RDF4Jperfs.txt while for jena it would be JenaTemp\JenaPers.txt and so on for all other platforms.

For Virtuoso, the temp folder is not generated and the output file VirtuosoPerfs.txt is generated at the current path.

##### Reseting the Data store
All the test programs are devised in a way to upload the data each time the programs are executed. Hence, if the data is already uploaded, then on each next iterarion, the same data would be loaded once more.If it is intended to delete the previous data, than the relevant temp (RDF4JTemp or JenaTemp etc.) folders should be deleted before prceeding with the execution again.

For Stardog, the system folder should also be deleted whenever the StardogTemp folder is removed.

For Virtuoso, the database cannot be flushed with deletion of any data, rather the Virtuoso Conductor avalailabe at http://localhost:8890/conductor/ should be used for deletion of older data.
## License
The java libraries used in these folders are open-source from the respective vendors and the Java source code on this repository is also released for free and open use of the research and development community.
## Contact
[Weiming Huang](mailto:weiming.huang@nateko.lu.se)
[Amir Raza](mailto:razaamirsyed@gmail.com)