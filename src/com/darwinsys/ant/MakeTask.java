package com.darwinsys.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

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
			final String commandString = sb.toString();
			System.out.println(commandString);
			builder.command(commandString);
			final Process process = builder.start();
			process.wait();
		} catch (Exception e) {
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
