package com.darwinsys.net;

import java.net.HttpURLConnection;

/** Encode the Result of a Network operation, typically over HTTP */
public class NetResult<T> {
    int status;
    T payload;
    final int HTTP_REDIRECTS = 
		HttpURLConnection.HTTP_MULT_CHOICE; // lowest 3xx error def'n

    public int getStatus() {
        return status;
    }

	public void setStatus(int status) {
		this.status = status;
	}

    public T getPayload() {
        return payload;
    }

	public void setPayload(T payload) {
		this.payload = payload;
	}

    public boolean isSuccess() {
        return status >= HttpURLConnection.HTTP_OK && status < HTTP_REDIRECTS;
    }

    @Override
    public String toString() {
        return "Result(" + (isSuccess()? "Success" : "Failure") + " (" + status + ") " + payload;
    }
}
