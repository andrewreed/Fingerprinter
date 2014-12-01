## _Fingerprinter_

A utility to create fingerprints of Netflix videos that can be  used in _dashid_ 
to identify specific videos in anonymized, header-only network traces.

_Fingerprinter_ downloads the header of each encoding's .ismv, which are then used to calculate 
the sequence of segment sizes for each encoding.

### Instructions

1. Create a directory structure of the form:

		netflix/movie/{movie name}/urls.txt
		netflix/show/{series name}/{season number}/{episode number}/urls.txt

	For example:

		netflix/movie/The_Avengers_(2012)/urls.txt
		netflix/show/House_of_Cards_(2013)/01/01/urls.txt

	At this point, each _urls.txt_ is an empty file.
2. Install the Firebug plugin for Firefox.
3. Open Firefox and go to Netflix.
4. Open the Firebug plugin.
5. Play a Netflix video.
6. Inside Firebug, right click the first line that says __GET 0-#####?o=__. Select _Copy Location_.
7. Paste the URL in the corresponding _urls.txt_. Press __enter__ to move to the next line.
8. Repeat steps 6-7 for the remaining lines that say __GET 0-#####?o=__. You do not need to press __enter__ after the final URL.
9. Save the current _urls.txt_.
8. Repeat steps 5-9 for several videos. Do not spend too much time on this task, 
as each playback of a video will timeout after some time, at which point the URLs will become invalid.
10. From within the directory that contains the __netflix__ directory, run the following command:

		find . -type d -links 2 | sed 's/^..//' > pathsToURLs.txt

	This will produce a list of directories that contain the individual URL lists.
10. Now you may run Fingerprinter:

		cat pathsToURLs.txt | java -jar Fingerprinter.jar >> netflix_fingerprints.txt

### Credit / Copying

As a work of the United States Government, _Fingerprinter_ is 
in the public domain within the United States. Additionally, 
Andrew Reed waives copyright and related rights in the work 
worldwide through the CC0 1.0 Universal public domain dedication 
(which can be found at http://creativecommons.org/publicdomain/zero/1.0/).
