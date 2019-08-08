This repository exists to contain and explain the code that imports contents from a given .csv file to a .db file,
as well as my process in so doing, including any assumptions I made during writing.

Brief summary of components-

sqlite-jdbc-3.27.2.1.jar
Executes prepared insertion:
INSERT INTO employeeData(A,B,C,D,E,F,G,H,I,j) VALUES(?,?,?,?,?,?,?,?,?,?) CREATE TABLE employeeData(A string,B string,C string,D string,E string,F string,G string,H string,I string,J string);
Prepared statement from MS3:
CREATE TABLE employeeData + "(" +"A string"+"B string"+"C string"+"D string"+"E string"+"F string"+"G string"+"H string"+"I string"+"J string"+")";

MS3.java
Creates GUI for running and re-running code, accepts file name, keeps track of good and bad results, and creates prepared statements.

SQLconnecter.java
Establishes connection to sqlite

Working Assumptions-

Project will be executed on the system in which it was created.
Input files do not contain errors, may be of any reasonable size.
Other developers will read the README and be sufficiently informed.
Validity of inputs is determined by their completeness.
Units of code from previous projects may be used, provided they suit the purposes of this one.

Process-
Extracting data from a .csv is something I've done before, but in a different language, so I looked for examples of how this general process could be accomplished. What was more difficult was
the insertion into a .db file by means of SQL, as it crosses some wires and I learned that I needed the above .jar file in order to run the program. In the MS3 file you'll find a simple gui
that I have in there to simplify the process, which I usually do whenever I have to run something more than twice. The gui has an action listener that basically runs the entire main method, 
reading the data, converting the data, and keeping track of the stats, which are exported to the log file(stored in the src folder). I had to use the SQL connector object because I couldn't 
think of any way of subsuming the code into the MS3 file, and it made the code somewhat easier to test, knowing when the object was working and when it was not. Thinking that the code might 
be tested with something other than the sample csv, I checked it with an exception and I tested it with an old csv from a previous project.
For transparency, the entire project was constructed and tested inside IntelliJ, a JetBrains product.

Running-
Other developers will likely have to configure their path to have the correct directory. Be sure to be in the correct destination when you run javac on both .java files. They may also run java
with the location of the .jar file specified in the class path.

