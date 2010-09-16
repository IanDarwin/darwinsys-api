package com.darwinsys.ant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Feeble attempt at an Ant Task for running Make
 */
public class MakeTask extends Task {
	
	private String baseDir;
	private String target;
	private String makeFile = null;

	@Override
	public void init() throws BuildException {
		// TODO Auto-generated method stub
		super.init();
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			StringBuilder sb = new StringBuilder();
			if (baseDir != null) {
				sb.append("cd ").append(baseDir).append(";");
			}
			sb.append("make ");
			if (makeFile != null) {
				sb.append("-f ").append(makeFile).append(' ');
			}
			if (target != null) {
				sb.append(target);
			}
			List<String> command = new ArrayList<String>();
			command.add("sh");
			command.add("-c");
			final String commandString = sb.toString();
			System.out.println(commandString);
			command.add(commandString);
			builder.command(command);
			final Process process = builder.start();
			final InputStream inputStream = process.getInputStream();
			final BufferedReader is = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = is.readLine()) != null) {
				System.out.println(line);
			}
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException("Unexpected Exception in process", e);
		}
	}
	
	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMakeFile() {
		return makeFile;
	}

	public void setMakeFile(String makeFile) {
		this.makeFile = makeFile;
	}

}
