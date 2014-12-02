## _Fingerprinter_

A utility to create fingerprints of Netflix videos that can be  used in _dashid_ 
to identify specific videos in anonymized, header-only network traces.

_Fingerprinter_ downloads the header of each encoding's .ismv, which are then used to calculate 
the sequence of segment sizes for each encoding.

### Instructions

1. Create a directory structure of the form:

		netflix/movie/{movie name}/
		netflix/show/{series name}/{season number}/{episode number}/

	At this point, each folder should be empty.
2. Install the Tamper Data plugin for Firefox.
3. Open Firefox and go to Netflix.
4. Open the Tamper Data plugin.
5. Play a Netflix video.
6. Once the video has started to stream, right click inside the upper window of Tamper Data and select _Export XML - All_.
7. Save the file as _capture.xml_.
8. Copy _capture.xml_ to the appropriate video subfolder created in Step 1.
9. Repeat steps 5-8 for several videos. Do not spend too much time on this task, 
as each playback of a video will timeout after some time, at which point the URLs will become invalid.
10. From within the directory that contains the __netflix__ directory, run the following command:

		./runFingerprinter.bash >> netflix_fingerprints.txt

	This script will parse the XML files and create text files in each video subfolder that list the URLs needed by Fingerprinter.
  The script will then run Fingerprinter and provide it with the list of subfolders via stdin.
11. Once you have run Fingerprinter in Step 10, you should verify that fingerprints were produced correctly. If so, you should delete all 
folders inside __netflix/__ before creating additional fingerprints.

### Credit / Copying

As a work of the United States Government, _Fingerprinter_ is 
in the public domain within the United States. Additionally, 
Andrew Reed waives copyright and related rights in the work 
worldwide through the CC0 1.0 Universal public domain dedication 
(which can be found at http://creativecommons.org/publicdomain/zero/1.0/).
