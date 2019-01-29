package com.gf.util;

import java.io.IOException;
import java.io.OutputStream;

import org.csource.fastdfs.UploadCallback;

public class UploadbackImpl implements UploadCallback{

	@Override
	public int send(OutputStream out) throws IOException {
		System.out.println("out==="+out);
		return 0;
	}

}
