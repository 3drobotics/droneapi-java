package com.geeksville.apiproxy;

import java.io.IOException;

import com.geeksville.dapi.Webapi.Envelope;

public interface IProtobufClient {
	/**
	 * Send a message
	 * 
	 * @param msg
	 * @throws IOException
	 */
	void send(Envelope msg) throws IOException;

	/**
	 * Block until a message can be read
	 * 
	 * @return
	 * @throws IOException
	 */
	Envelope receive() throws IOException;

	void close() throws IOException;

	void flush() throws IOException;
}
