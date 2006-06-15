package com.darwinsys.genericui;

/**
 * General contract of display components that can indicate success or failure.
 */
public interface SuccessFailureUI {
	/**
	 * Show the indicator of "success" (often: green)
	 */
	void showSuccess();
	
	/**
	 * Show the indicator of "failure" (usually: red), e.g., when a test fails or errors.
	 */
	void showFailure();
	
	/**
	 * Show a neutral indication (e.g., gray, or, the screen background)
	 */
	void reset();
}