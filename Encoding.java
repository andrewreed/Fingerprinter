import java.io.*;
import java.util.*;
import java.net.*;

public class Encoding {
	// HTTP headers for a valid GET request to Netflix
	private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	private static final String ACCEPT_ENCODING = "gzip, deflate";
	private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";
	private static final String CONNECTION = "keep-alive";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:27.0) Gecko/20100101 Firefox/27.0";

	// extra URL items not present in manifest.xml
	private static final String RANGE = "/range/0-99999?";
	private static final String RANDOM = "&random=123456789";

	private int bitrate;
	private String urlOfVideo;
	private String parameters;
	private String host;
	private int[] segmentSizes;
	private byte[] header;

	// Constructor
	public Encoding(String bitrate, String url) {
		this.bitrate = Integer.parseInt(bitrate);
		parseUrl(url);
		getHeader();
		calculateSegmentSizes();
	}

	public int getBitrate() {
		return bitrate;
	}

	public int[] getSegmentSizes() {
		return segmentSizes;
	}

	private void parseUrl(String url) {
		String[] urlHalves = url.split("\\?");
		urlOfVideo = urlHalves[0];
		parameters = urlHalves[1];
		host = urlOfVideo.split("/")[2];
	}

	private void getHeader() {
		URL url;
		HttpURLConnection conn;
		InputStream is;

		try {
			url = new URL(urlOfVideo + RANGE + parameters + RANDOM);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", ACCEPT);
			conn.setRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
			conn.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
			conn.setRequestProperty("Connection", CONNECTION);
			conn.setRequestProperty("Host", host);
			conn.setRequestProperty("User-Agent", USER_AGENT);
		
			is = conn.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			for (int count; ((count = is.read(buffer)) != -1); ) {
				baos.write(buffer, 0, count);
			}
			baos.close();

			header = baos.toByteArray();

			is.close();
		} catch (Exception e) {
			System.out.println("ERROR: Unable to download header.");
			System.exit(0);
		}
	}

	private void calculateSegmentSizes() {
		int sidxLoc = 0;

		for (int i = 0; i < 99997; i++) {

			char c1 = (char) (header[i] & 0xFF);
			char c2 = (char) (header[i+1] & 0xFF);
			char c3 = (char) (header[i+2] & 0xFF);
			char c4 = (char) (header[i+3] & 0xFF);

			if ((c1 == 's') && (c2 == 'i') && (c3 == 'd') && (c4 == 'x')) {
				sidxLoc = i;
				break;
			}
		}

		if (sidxLoc == 0) {
			System.out.println("ERROR: Could not find segment index box.");
			System.exit(0);
		}

		int refCountLoc = sidxLoc + 34;

		int refCount = 0;
		refCount = (header[refCountLoc] & 0xff);
		refCount = (refCount << 8) + (header[refCountLoc+1] & 0xff);

		segmentSizes = new int[(int)Math.ceil(refCount / 2.0)];

		int segmentSizeLoc = refCountLoc + 2;

		for (int i = 0; i < refCount; i++) {
			int currentSize = 0;
			currentSize = (header[segmentSizeLoc] & 0xff);
			currentSize = (currentSize << 8) + (header[segmentSizeLoc+1] & 0xff);
			currentSize = (currentSize << 8) + (header[segmentSizeLoc+2] & 0xff);
			currentSize = (currentSize << 8) + (header[segmentSizeLoc+3] & 0xff);

			segmentSizes[i/2] += currentSize;

			segmentSizeLoc += 12;
		}
	}
}
