package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Matrix {
	private static final int nColums = 10;
	private static final int nRows = 10;
	private static final String matrix[][] = new String[nColums][nRows];
	private String fileName;
	private static int numberOfPlanes = 3;
	private static final int numberOfConfigs = 10;

	public static int getNumberofconfigs() {
		return numberOfConfigs;
	}

	public static int getNumberOfPlanes() {
		return numberOfPlanes;
	}

	public static void setNumberOfPlanes(int numberOfPlanes) {
		Matrix.numberOfPlanes = numberOfPlanes;
	}

	public String[][] getMatrix() {
		return matrix;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static int getNcolums() {
		return nColums;
	}

	public static int getNrows() {
		return nRows;
	}

	public Matrix(String fileName) throws Exception {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = "";
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		fileReader.close();

		String lines[] = stringBuilder.toString().split("/");
		for (int i = 0; i < lines.length; i++) {
			String values[] = lines[i].split(",");
			for (int x = 0; x < values.length; x++) {
				matrix[i][x] = values[x].toString();
			}
		}
	}

	public static String composePath(int i) {
		Random random = new Random();
		int randomNumber = random.nextInt(i);
		String path = "configs\\config" + randomNumber + ".txt";
		return path;
	}

	public static String configPath() {
		String path = "configs\\config";
		return path;
	}

	public static void printMatrix(Matrix m) {
		System.out.println("display matrix");
		System.out.println();
		for (int i = 0; i < Matrix.nColums; i++) {
			for (int j = 0; j < Matrix.nRows; j++)
				System.out.print(Matrix.matrix[i][j] + ",");
			System.out.println();
		}
	}

	public static String shot(int x, int y) // x->row y->column
	{
		String result = "";
		if (x <= 10 && y <= 10 && x > 0 && y > 0) {
			if (Matrix.matrix[x - 1][y - 1].toString().equals(1 + "")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				result = "1"; // return 1 hit plane
			} else if (Matrix.matrix[x - 1][y - 1].toString().equals("A")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				for (int i = 1; i < Matrix.nColums + 1; i++) {
					for (int j = 1; j < Matrix.nRows + 1; j++)
						if (Matrix.matrix[i - 1][j - 1].toString().equals(1 + "")) {
							Matrix.matrix[i - 1][j - 1] = "X";
						}
				}
				Matrix.setNumberOfPlanes(Matrix.getNumberOfPlanes() - 1);
				result = "X";
			} else if (Matrix.matrix[x - 1][y - 1].toString().equals(2 + "")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				result = "1";
			} else if (Matrix.matrix[x - 1][y - 1].toString().equals("B")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				for (int i = 1; i < Matrix.nColums + 1; i++) {
					for (int j = 1; j < Matrix.nRows + 1; j++)
						if (Matrix.matrix[i - 1][j - 1].equals(2 + "")) {
							Matrix.matrix[i - 1][j - 1] = "X";
						}
				}
				Matrix.setNumberOfPlanes(Matrix.getNumberOfPlanes() - 1);
				result = "X";
			} else if (Matrix.matrix[x - 1][y - 1].toString().equals(3 + "")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				result = "1";
			} else if (Matrix.matrix[x - 1][y - 1].equals("C")) {
				Matrix.matrix[x - 1][y - 1] = "X";
				for (int i = 1; i < Matrix.nColums + 1; i++) {
					for (int j = 1; j < Matrix.nRows + 1; j++)
						if (Matrix.matrix[i - 1][j - 1].toString().equals(3 + "")) {
							Matrix.matrix[i - 1][j - 1] = "X";
						}
				}
				Matrix.setNumberOfPlanes(Matrix.getNumberOfPlanes() - 1);
				result = "X";
			} else if (Matrix.matrix[x - 1][y - 1].toString().equals("X")) {
				result = "0";
			} else {
				Matrix.matrix[x - 1][y - 1] = "X";
				result = "0";
			}
		} else {
			result = "Coordinates Must Be Positive Betwenn 1 and 10";
		}
		return result;
	}

}