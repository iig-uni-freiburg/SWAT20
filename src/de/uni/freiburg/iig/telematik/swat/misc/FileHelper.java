package de.uni.freiburg.iig.telematik.swat.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class FileHelper {

	public static long getLinesCount(String fileName, String encodingName) {
		long linesCount = 0;
		File file = new File(fileName);
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(file);
			Charset encoding = Charset.forName(encodingName);
			Reader fileReader = new InputStreamReader(fileIn, encoding);
			int bufferSize = 4096;
			Reader reader = new BufferedReader(fileReader, bufferSize);
			char[] buffer = new char[bufferSize];
			int prevChar = -1;
			int readCount = reader.read(buffer);
			while (readCount != -1) {
				for (int i = 0; i < readCount; i++) {
					int nextChar = buffer[i];
					switch (nextChar) {
					case '\r': {
						// The current line is terminated by a carriage return
						// or by a carriage return immediately followed by a
						// line feed.
						linesCount++;
						break;
					}
					case '\n': {
						if (prevChar == '\r') {
							// The current line is terminated by a carriage
							// return immediately followed by a line feed.
							// The line has already been counted.
						} else {
							// The current line is terminated by a line feed.
							linesCount++;
						}
						break;
					}
					}
					prevChar = nextChar;
				}
				readCount = reader.read(buffer);
			}
			if (prevChar != -1) {
				switch (prevChar) {
				case '\r':
				case '\n': {
					// The last line is terminated by a line terminator.
					// The last line has already been counted.
					break;
				}
				default: {
					// The last line is terminated by end-of-file.
					linesCount++;
				}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return linesCount;
	}

}
