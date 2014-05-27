package com.geeksville.apiproxy;

import java.io.*;

/**
 * A utility that scans through all suitable files in a src directory, uploading
 * them and then moving them to a 'sent' directory.
 */
public class DirectoryUploader {

	private File srcDir;
	private File destDir;
	private IUploadListener callback;
	private String userId, userPass, vehicleId;
	private String apiKey;

	public DirectoryUploader(File srcDir, File destDir,
			IUploadListener callback, String userId, String userPass,
			String vehicleId, String apiKey) {
		this.srcDir = srcDir;
		this.destDir = destDir;
		this.callback = callback;
		this.userId = userId;
		this.userPass = userPass;
		this.vehicleId = vehicleId;
		this.apiKey = apiKey;
	}

	public void run() {

		File[] files = srcDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".tlog");
			}
		});

		for (File f : files) {
			try {
				callback.onUploadStart(f);
				String url = RESTClient.doUpload(f, userId, userPass,
						vehicleId, apiKey);

				destDir.mkdirs();
				File newName = new File(destDir, f.getName());
				f.renameTo(newName);

				callback.onUploadSuccess(f, url);
			} catch (IOException ex) {
				callback.onUploadFailure(f, ex);
			}
		}
	}
}
