#!/bin/bash

# -------------------------------------------------------------------
if [ -z "$1" ] ; then
	city="Berlin"
else
	city=
    	for i in "$@"; do 
		city="$city%20$i"
    	done

fi
echo "Fetching weather for .. "
(cd /Users/phanisaripalli/Projects/Weather/ && php Weather.php city=$city)


