package com.xy.gdu;

import java.io.IOException;
import java.util.Arrays;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

public class FileManager {

	// Please change folder id on your goole driver
	public static final String apksLocationId = "0B-0iN5m8YqglMHJxdy13Z0JkU3c";
	private static final String finalApksLoaction = "/ReleaseApks.zip";
	private static final String uploadingFileMimeType = "application/octet-stream";
	private static final String buildFolderPath = GoogleDriveManager.getJarPath()
			+ GoogleDriveManager.getApkOutputFolderPath() + finalApksLoaction;

	private Util util;
	private Drive service;

	public FileManager() throws IOException {
		this.service = GoogleDriveManager.getDriveService();
		this.util = new Util();
	}

	public void insertFile() throws IOException, InterruptedException {
		File remoteFileFolder = this.createRemoteApkFolder(apksLocationId);
		System.out.println("Remote Folder Id on Google Drive: " + remoteFileFolder.getId());

		Thread.sleep(10000);

		String uploadingFileTitle = "gdu_android_apks";

		System.out.println("test: " + buildFolderPath);

		File remoteFile = this.createRemoteApkFiles(uploadingFileTitle, remoteFileFolder.getId(), buildFolderPath,
				uploadingFileMimeType);
		System.out.println("Remote File Id on Google Drive: " + remoteFile.getId());
	}

	public File createRemoteApkFolder(String parentId) throws IOException {
		File fileMetadata = new File();
		StringBuffer folderName = new StringBuffer();
		String fileTitle = folderName.append("gduSampleFolder").append(this.util.getDateTime()).toString();

		fileMetadata.setTitle(fileTitle);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");

		if (parentId != null && parentId.length() > 0) {
			fileMetadata.setParents(Arrays.asList(new ParentReference().setId(parentId)));
		}

		File remoteFileFolder = this.service.files().insert(fileMetadata).setFields("id").execute();

		return remoteFileFolder;
	}

	public File createRemoteApkFiles(String fileTitle, String parentId, String filePath, String mediaContentMimeType)
			throws IOException {
		File fileMetadata = new File();
		fileMetadata.setTitle(fileTitle);
		fileMetadata.setMimeType(mediaContentMimeType);

		if (parentId != null && parentId.length() > 0) {
			fileMetadata.setParents(Arrays.asList(new ParentReference().setId(parentId)));
		}

		java.io.File localFile = new java.io.File(filePath);
		FileContent mediaContent = new FileContent(mediaContentMimeType, localFile);
		File remoteFile = this.service.files().insert(fileMetadata, mediaContent).setFields("id").execute();

		return remoteFile;
	}
}
