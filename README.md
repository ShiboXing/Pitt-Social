# Pitt-Social  <br> 

###### A social networking system for University of Pittsburgh built with PostgresSQL and operated by an application written in Java with JDBC  <br>
### project owners:  <br>
###### Fangzheng Guo, Zhuolun Li, Shibo Xing

### Phase 1
Instantiated schemas and all relations with contraints\
Defined triggers on tables\
Generated sample data 

### Phase 2
Implemented SQL procedures and functions to support the JDBC methods\
Improved SQL relation schemas for better performance and modularity\
Implemented Java methods and classes to accomplish user's operations


## How to run
###### prerequisites: 
###### have Java 8 or higher installed on your local machine, a postgresql database installed on localhost

First change the credentials in
<h4> <code>src/DataManager.java</code> </h4> in accordance with your postgresql database <br> <br>

In the root directory of the repository, you can run:<br>
#### `$  sh ./compile.sh`
for windows users:<br>
#### `>  compile.bat`

after compiling, you can run:

#### `$  sh ./run.sh I` to initialized database   
#### `$  sh ./run.sh` to run without initialization   
for windows users:<br>
#### `$  ./run.bat I` to initialized database   
#### `$  ./run.bat` to run without initialization   
to start the program.




