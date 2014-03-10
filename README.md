## _SignatureMaker_

A utility to create signatures of Netflix videos. These signatures are used in _dashid_ 
to identify specific videos in anonymized, header-only network traces. These signatures 
can also be used to create video profiles for _dashem_.

_SignatureMaker_ uses a video's manifest to obtain the URLs for each bitrate encoding. It 
then downloads the header of each encoding's .ismv. The headers are then used to calculate 
the sequence of segment sizes for each encoding.

### Instructions

1. Create a directory structure of the form:

		netflix/movie/{movie name}/manifest.xml
		netflix/show/{series name}/{season number}/{episode number}/manifest.xml

	For example:

		netflix/movie/The_Avengers_(2012)/manifest.xml
		netflix/show/House_of_Cards_(2013)/01/01/manifest.xml

	At this point, each _manifest.xml_ is an empty file.
2. Install the Firebug plugin for Firefox.
3. Open Firefox and go to Netflix.
4. Open the Firebug plugin.
5. Play a Netflix video.
6. Inside Firebug, right click the line that says __POST authorization__. Select _Copy Response Body_.
7. Paste the response body in the corresponding _manifest.xml_. Save the _manifest.xml_.
8. Repeat steps 5-7 for several videos. Do not spend too much time on this task, 
as each playback of a video will timeout after some time, at which point the URLs will become invalid.
9. From within the directory that contains the __netflix__ directory, run the following command:

		find . -type d -links 2 | sed 's/^..//' > pathsToManifests.txt

	This will produce a list of directories that contain the manifests.
10. Now you may run SignatureMaker:

		cat pathsToManifests.txt | java -jar SignatureMaker.jar >> netflix_signatures.txt

### Credit / Copying

As a work of the United States Government, _SignatureMaker_ is 
in the public domain within the United States. Additionally, 
Andrew Reed waives copyright and related rights in the work 
worldwide through the CC0 1.0 Universal public domain dedication 
(which can be found at http://creativecommons.org/publicdomain/zero/1.0/).
