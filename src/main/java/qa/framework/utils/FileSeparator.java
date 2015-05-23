package main.java.qa.framework.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import main.java.qa.framework.main.TestBase;
import main.java.qa.framework.main.WebElements;

public class FileSeparator extends TestBase implements WebElements {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		PropertyUtils.loadPropertyFile(proprtyFile);
		String imageFile = PropertyUtils.getProperty("imageFile");
		String imageFile_ = Paths.get(imageFile).toFile().toString();
		System.out.println(imageFile_);

		String userHome = System.getProperty("user.home");
		String screenShotsFolder = userHome + File.separator + "Documents"
				+ File.separator + "Tests" + File.separator;

		System.out.println(TestBase.getCurrentTime());

		int second = 2;

		while (second > 0) {
			try {
				Thread.sleep(500);
				System.out.println(screenShotsFolder);

			} catch (Exception e) {
			}
			second--;
		}

		System.getProperty("sun.arch.data.model");
		System.out.println(System.getProperty("sun.arch.data.model"));

		String book1 = "Book1";
		String book2 = "Book2";
		String book3 = "Book3";
		String book4 = "Book4";

		List<String> bookList = new ArrayList<String>();
		bookList.add(book1);
		bookList.add(book2);
		bookList.add(book3);
		bookList.add(book4);

		TestBase.writeFile(urlsFile, bookList);

		TestBase.readFile(Paths.get("scripts/create_db.sql"));

		Path testOutput = Paths.get("test-output/html");

		if (testOutput.toFile().exists()) {
			try {
				System.out.println("Real path: "
						+ testOutput.toRealPath(LinkOption.NOFOLLOW_LINKS));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("The directory does not exist");
		}

		List<File> files = new ArrayList<File>();

		File[] children = testOutput.toFile().listFiles();

		for (int i = 0; i < children.length; i++) {
			System.out.println(children[i]);

			files.add(children[i]);
			// TestBase.openAll(files);

		}

	}
}