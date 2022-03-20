package looptesting;

import java.util.List;
import java.util.LongSummaryStatistics;

public class TimingTestResult {

    List<Long> rawNums;
    LongSummaryStatistics stats;
    String name;

    public TimingTestResult(List<Long> numbers, String name) {
        rawNums = numbers;
        this.stats = numbers.stream().mapToLong(Long::longValue).summaryStatistics();
        this.name = name;
    }

    public List<Long> getRawData() {
        return rawNums;
    }

    @Override
    public String toString() {
        return "TimingTestResult{" +
                "name='" + name + '\'' +
                ", stats=" + stats +
                '}';
    }

    public String getName() {
        return name;
    }

    public TimingTestResult setName(String name) {
        this.name = name;
        return this;
    }

    public LongSummaryStatistics getStats() {
        return stats;
    }
}
