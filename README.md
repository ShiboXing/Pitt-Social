# Pitt-Social  <br> 

###### A social networking system for University of Pittsburgh built with PostgresSQL and operated by an application written in Java with JDBC  <br>
### project owners:  <br>
###### Zhuolun Li, Fangzheng Guo, Shibo Xing

### Phase 1
instantiated schema and all relations with contraints\
defined triggers on tables\
generated sample data 

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
#### `compile.sh`
for windows users:<br>
#### `compile.bat`

after compiling, you can run:

#### `run.sh`
for windows users:<br>
#### `run.bat`
to start the program.




