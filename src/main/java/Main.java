import looptesting.TimingTestResult;
import looptesting.TimingsTest;
import looptesting.TimingsTestBuilder;
import looptesting.export.TimingsExportFactory;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Random r = new Random();
        List<Integer> nums = Collections.unmodifiableList(
                IntStream.generate(r::nextInt)
                .limit(20)
                .boxed()
                .toList()
        );
        Runnable forLoopTask = () -> {
            int sum = 0;
            for(var n : nums) {
                sum += n;
            }
        };
        Runnable streamTask = () -> {
            int sum = nums.stream().mapToInt(Integer::intValue).sum();
        };
        Runnable parallelStreamTask = () -> {
            int sum = nums.stream().parallel().mapToInt(Integer::intValue).sum();
        };

        TimingsTest test = new TimingsTestBuilder(forLoopTask, streamTask, parallelStreamTask)
                .nameTests("Fors", "Streams", "Parallel Streams")
                .setLoopEndingValue(1000)
                .useAtomicIteratorVariable(null)
                .build();
        test.start();
        TimingTestResult[] results = test.getResults();
        TimingsExportFactory factory = new TimingsExportFactory(results, ",").useMovingAverage(100);
        System.out.println(factory.exportToString());
        factory.exportRawNumbersByTask(new File("/Users/LVeenker22/Documents/GitHub/Loop Timings/src/test"));
    }
}
