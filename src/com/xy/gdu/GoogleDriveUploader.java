package com.xy.gdu;

import java.io.IOException;

public class GoogleDriveUploader {

	public static void main(String[] args) throws IOException, InterruptedException {
		FileManager fileManager = new FileManager();
		fileManager.insertFile();
	}

}
