package com.sdstudio.iproxy.core;

import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.sdstudio.iproxy.Utils;

import static org.junit.Assert.*;

public class TestUtils {
	@Test
	public void testGetOrCreate() throws IOException {
		String tmpDir = Utils.getTmpDir();
		String notExist = Utils.combinePaths(tmpDir, "not", "exists");
		assertThat(new File(notExist).exists(), is(false));
		assertThat(Utils.getOrCreate(notExist, true).exists(), is(true));
		new File(Utils.combinePaths(tmpDir, "not")).delete();
	}
}
