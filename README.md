# TwoDotsPlayer

Download the JAR [here](http://example.com/)

Run `javac -jar TwoDotsPlayer.jar <path_to_YAML_file> <number_of_levels>`

`path_to_YAML_file` can be a relative or absolute path to a YAML file containing game state information.  Dots can be entered with the following syntax:
* **r**=red
* **b**=blue
* **p**=purple
* **y**=yellow
* **g**=green
* **w**=white
* **a**=anchor
* **-**=blank
* **&**=block
* **_**=empty

`number_of_levels` indicates how far ahead the program looks ahead. If this isn't entered, it will default to 4.  It's often better to keep it low because dropped dots are likely to overpower anything else in the game state after about 4 turns.
