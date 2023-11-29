## How to compile and run

From the parent/root directory of this project do the following:

If the _bin_ folder does not exist, create it with:

`mkdir bin`

then run javac as follows:

`javac -cp 'lib/junit-4.13.2.jar:lib/javax.json-1.0.jar' -d bin/ $(find src/ -name '*.java')`

This will find all the .java files inside the _src_ directory and compile them to the _bin_ directory using the neccessary libraries specfied in _lib_.

If you  then change to the bin directory with:

`cd bin`

you can then run the app using:

`java Main`


## Running the tests

- The tests I've written are very basic and I admit that.

- I used JUnit v4 and it works if you import the project into VSCode and use the Java Extension Manager.

- I tried to figure out how to get it running from the command line to no avail.

Note: The tests exist in ModelTest.java and the junit library in the lib/ directory. I googled and found that I need to run something like this: 

`java -cp '.:lib/junit-4.13.2.jar' org.junit.runner.JUnitCore bin/ModelTest.class`

But that does not work and I get "java.lang.IllegalArgumentException: Could not find class [bin/ModelTest.class]."



## General info

- Drawing squares and circles is implemented by pressing down the SHIFT key while dragging to create a rectangle or an ellipse.

- Selecting a previously drawn object is implemented by turning on "Select mode" at the bottom right corner of the app and then picking an object from the canvas using the mouse cursor.

- The rotation and scale of an object can **only** be changed while Select Mode is on. Two spinners will appear at the bottom left once you enable select mode that will allow you to change the rotation and scale after you select a shape with the mouse cursor.

- Changing the border/line color and the fill color can be done by clicking on the "Pick Color" button (top right) and selecting in the dialog that shows up whether you wanna change the fill color or not and what you wanna change it to. If the fill color remains unchecked, only the border color will change.

- To connect to a drawing server go to Network on the top menu bar and click connect. Enter the server details and your authentication token and click connect.

- If you successfully connected to a drawing server, you can fetch and push drawings via the "Fetch shapes" and "Push shapes" submenu items present in the Network tab on the top menu bar.


- To export the canvas as an image go to File on the top menu bar and then click Export. You will be asked for a filename and the directory to save the image.

- Design choice: The "Clear" button will clear the canvas **and any states saved by undo/redo**.

## What's working partially:

1. _Selecting a rotated or scaled shape_. That's because (except for Lines) the rotated shape coordinates are not calculated correctly and the non-rotated coordinates are used. This means that it is still possible to select a scaled/rotated shape but at a specific region/spot only (one where the coordinates are the same as before rotation).

2. _Triangles_. My program supports only isosceles triangles not scalenes.


3. Networking. My program supports only fetching shapes drawn by other users from the user (getDrawings) and pushing locally created shapes (addDrawing). Also, this works for all shapes **except Triangles** because as previously stated my program supports only isosceles triangles.

## What's not working:

1. Suppose two shapes are drawn, call them _A_ and _B_. The user clicks undo, so _B_ is removed. The user enters select mode and drags/modifies properties of A. The user exist select mode. The user clicks undo two times. In that case what _should_ happen is that B reappears in the canvas. But in my implementation it gets completely lost. I just noticed this two hours before submission so I have no time to fix it :/

2. _updateDrawing_ and _deleteDrawing_ from the networking spec have NOT been implemented.
