# FleSe

## Changes in new version

This project is the result of applying several changes to the project
FleSe that included Dan's code and algorithms.

The main purpose of this project is to introduce machine learning over
the customizations that the users perform over the fuzzy concepts
definitions.

### First change:

class: src/auxiliar/LocalUserInfo.java

It is introduced a user property to show a list of numbers that will
represent the list of characteristic that will be used by the machine
learning algorithms.


### Second change:
 
class: manager/FuzzificationsManager.java
 
The method updateDefaults() will be called whenever FleSe needs to
update the fuzzy concepts definitions that the user is going to
use. The algorithm will cluster all the users and select the
customizations done by the users that belong to the same cluster as
the actual user.
  
### Third change
 
class: manager/FuzzificationAlgorithms.java
 
The method machineLearning() will receive the list of personalizations
that has been selected by the method explained above. It will apply
two simple algorithms to return a new definition of the fuzzy
concept. This result would be the representative definition of all the
customizations received as an input.
 
### Fourth change

class: servlets/servlet.java

Changes introduced in order to call the new method updateDefaults()
when it is necessary.

### Fifth change

class: manager/FilesManager.java

This last change has no relation with the rest. The purpose is to call
the prolog compilator before uploading files to avoid having files
with errors and also some bugs related with the file plserver
 
