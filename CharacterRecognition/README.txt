This README file explains how to use the program for "CharacterRecognition",
which can be downloaded from my submission on canvas and from the git 
repository https://git-teaching.cs.bham.ac.uk/mod-40cr-proj-2016/myc304/tree/master

The .zip file submission for my project includes 
	-A source code file for the user interface, 
	-Testing and training set images folder, 
	-Images folder for learning algorithms, and
	-A project report file for the final report.

In the source code file, the user interface were subdivided into different panels 
which were represented in individual files.  The main running class is in the mainGUI folder.

There are several options for running the user interface...
--------------------------------------------SOURCE CODE FOLDER-----------------------

--------RUN IN AN IDE------------
To run the classes in an IDE such as Eclipse, copy the entire content from the 
source folder into a project in the IDE, and then run the MainGUI.java class in 
the mainGUI folder.  Note: do not copy the subfolder itself; open the folder and 
copy the content

------COMPILE AND RUN ON THR COMMAND LINE---------
The classes can also be compiled using a command line.
The command 
	javac *.java
compiles all the classes from that folder.  Your compiler should be able to 
support Java 7 or higher in order to compile it.
To run the whole user interface, use the command
	java MainGui

----------------------------TRAINING AND TESTING DATA SET-----------------------
The testing and training set folder contains English character images for
recognition, those images can be used to test the image conversion function. 
The convertion will then convert those images into a binary txt file which was
named Test.txt and Train.txt.

------------------IMAGES FOLDER FOR LEARNING ALGORITHMS--------------
The folder BaclProp_images and handWritingImages contain images which
teach users about the neural network algorithms.  all the images were
embeded into the user interface, but users can always look at it spearetly
without running the program.

----------------------------PROJECT REPORT----------------------------
The folder project report contains the final report named FYP_Report as well as three appendices.
