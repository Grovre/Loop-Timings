package looptesting.export;

import looptesting.TimingTestResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TimingsExportFactory {

    private final TimingTestResult[] data;
    private final String delimiter;
    private boolean usingMovingAverage;
    private ArrayList<ArrayList<Double>> movingAverages;

    public TimingsExportFactory(@NotNull TimingTestResult[] data, @Nullable String delimiter) {
        this.data = data.clone();
        this.delimiter = delimiter;
        usingMovingAverage = false;
    }

    public String exportToString() {
        String toWrite = separatedValues(delimiter);
        assert toWrite.length() > 0;
        return toWrite;
    }

    public File exportToFile(@NotNull File file) {
        String fileText = separatedValues(delimiter);
        if(file.exists()) {
            System.out.println("File already exists. Enter any key to continue.");
            Scanner s = new Scanner(System.in);
            String ans = s.nextLine();
            s.close();
            if(ans.length() > 0) return null;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(fileText);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public TimingsExportFactory useMovingAverage(int numbersPerAverage) {
        usingMovingAverage = true;
        this.movingAverages = new ArrayList<>(data.length);
        ArrayList<Double> averagedNumbers = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            TimingTestResult dat = data[i];
            ArrayList<Long> numbersToAverage = new ArrayList<>(numbersPerAverage);
            List<Long> numbers = dat.getRawData();
            for (long n : numbers) {
                numbersToAverage.add(n);
                if (numbersToAverage.size() != numbersPerAverage) {
                    continue;
                }
                averagedNumbers.add(numbersToAverage.stream()
                        .mapToLong(Long::longValue)
                        .average()
                        .getAsDouble());
                numbersToAverage.clear();
            }
            movingAverages.add((ArrayList<Double>) averagedNumbers.clone());
            averagedNumbers.clear();
        }

        return this;
    }

    public File[] exportRawNumbersByTask(File directory) {
        File[] files = new File[data.length];
        Arrays.setAll(files, i -> new File(directory + File.separator + data[i].getName() + ".txt"));
        try {
            for (int i = 0; i < data.length; i++) {
                File file = files[i];
                TimingTestResult dat = data[i];
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                for(var n : usingMovingAverage ? movingAverages.get(i) : dat.getRawData()) {
                    bw.write(n.toString());
                    bw.newLine();
                }
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    private String separatedValues(@Nullable String delimiter) {
        if(delimiter == null) delimiter = ",";
        StringBuilder s = new StringBuilder("Tests");
        long[][] rawLongData = null;
        double[][] rawDoubleData = null;
        if(usingMovingAverage) {
            rawDoubleData = new double[data.length][data[0].getRawData().size()];
        } else {
            rawLongData = new long[data.length][data[0].getRawData().size()];
        }
        for (int i = 0; i < data.length; i++) {
            TimingTestResult dat = data[i];
            s.append(delimiter).append(dat.getName());
            if(usingMovingAverage) {
                assert rawDoubleData != null;
                rawDoubleData[i] = movingAverages.get(i).stream().mapToDouble(Double::doubleValue).toArray();
            } else {
                assert rawLongData != null;
                rawLongData[i] = dat.getRawData().stream().mapToLong(Long::longValue).toArray();
            }
        }
        s.append(System.lineSeparator());

        if(usingMovingAverage) {
            assert rawDoubleData != null;
            for(int i = 0; i < rawDoubleData[0].length; i++) {
                s.append(i+1);
                for(int j = 0; j < rawDoubleData.length; j++) {
                    s.append(delimiter).append(rawDoubleData[j][i]);
                }
                s.append(System.lineSeparator());
            }
        } else {
            assert rawLongData != null;
            for (int i = 0; i < rawLongData[0].length; i++) {
                s.append(i + 1);
                for (int j = 0; j < rawLongData.length; j++) {
                    s.append(delimiter).append(rawLongData[j][i]);
                }
                s.append(System.lineSeparator());
            }
        }

        return s.toString();
    }
}
