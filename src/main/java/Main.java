import looptesting.TimingTestResult;
import looptesting.TimingsTest;
import looptesting.TimingsTestBuilder;
import looptesting.export.TimingsExportFactory;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Runnable explicitArrayGeneration = () -> {
            Random r = new Random();
            int[] arr = new int[1000000];
            for(int i = 0; i < arr.length; i++) {
                arr[i] = r.nextInt();
            }
        };
        Runnable setAllArrayGeneration = () -> {
            Random r = new Random();
            int[] arr = new int[1000000];
            Arrays.setAll(arr, i -> r.nextInt());
        };

        Runnable streamArrayGeneration = () -> {
            Random r = new Random();
            int[] arr = IntStream.generate(r::nextInt).limit(1000000).toArray();
        };

        Runnable randomIntsArrayGeneration = () -> {
            Random r = new Random();
            int[] arr = r.ints(1000000).toArray();
        };

        TimingsTest test = new TimingsTestBuilder(explicitArrayGeneration, setAllArrayGeneration, streamArrayGeneration, randomIntsArrayGeneration)
                .nameTests("Explicit Generation", "Arrays.setAll", "IntStream Generation", "Ints Generation")
                .setLoopEndingValue(1000)
                .build();
        test.start();
        TimingTestResult[] results = test.getResults();
        TimingsExportFactory factory = new TimingsExportFactory(results, ",");
        System.out.println(factory.exportToString());
        factory.exportToFile(new File("/Users/LVeenker22/Documents/GitHub/Loop Timings/src/test/numbers.csv"));
    }
}
