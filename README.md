# Pitt-Social 
This project is a social networking system for University of Pittsburgh built with PostgresSQL and operated by an application written in Java with JDBC  <br>
Contributors: [Fangzheng Guo](https://github.com/toobbby), [Zhuolun Li](https://github.com/Zhuolun1996) and [Shibo Xing](https://github.com/ShiboXing)

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
###### have Java 8 installed on your local machine, a postgresql database installed on localhost
### Unix/Linux users:
First change the credentials in
<h4> <code>src/DataManager.java</code> </h4> in accordance with your postgresql database <br> <br>

In the root directory of the repository, you can run:<br>
#### `$  sh ./compile.sh` to compile. <br>

#### `$  sh ./run.sh I` to initialize database   
#### `$  sh ./run.sh` to run without initialization
to start the program.   


### Windows users:
Run <br>
#### `>  compile.bat` to compile. <br>

#### `$  ./run.bat I` to initialize database   
#### `$  ./run.bat` to run without initialization   
to start the program.


>For any question or concern, please contact <shx26@pitt.edu>, <zhl137@pitt.edu> or <fag24@pitt.edu>.

