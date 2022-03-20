package looptesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingsTest {

    protected Runnable[] tasks;
    protected String[] names;
    protected TimingTestResult[] results;
    protected int startingValue;
    protected int endingValue;
    protected int testRunCountPerIteration;
    protected boolean usingAtomicIterVar;
    protected AtomicInteger atomicIterVar;

    public TimingsTest(){}

    public Runnable[] getTasks() {
        return tasks;
    }

    public String[] getNames() {
        return names;
    }

    public TimingsTest setTaskNames(String[] names) {
        assert names.length == tasks.length;
        this.names = names;
        return this;
    }

    public int getTestRunCountPerIteration() {
        return testRunCountPerIteration;
    }

    public void setTestRunCountPerIteration(int testRunCountPerIteration) {
        this.testRunCountPerIteration = testRunCountPerIteration;
    }

    public AtomicInteger getAtomicIterationVariable() {
        return atomicIterVar;
    }

    public void start(){

        ArrayList<Long>[] timings = new ArrayList[tasks.length];
        Arrays.setAll(timings, ArrayList<Long>::new);

        long start, end;
        if(usingAtomicIterVar) {
            for(; atomicIterVar.get() < endingValue; atomicIterVar.incrementAndGet()) {
                for (int i = 0; i < tasks.length; i++) {
                    start = System.nanoTime();
                    tasks[i].run();
                    end = System.nanoTime();
                    timings[i].add(end - start);
                }
            }
        } else {
            for(; startingValue < endingValue; startingValue++) {
                for (int i = 0; i < tasks.length; i++) {
                    start = System.nanoTime();
                    tasks[i].run();
                    end = System.nanoTime();
                    timings[i].add(end - start);
                }
            }
        }

        results = new TimingTestResult[timings.length];
        Arrays.setAll(results, i -> new TimingTestResult(timings[i],
                names == null ? "Task #" + i : names[i]));
    }

    public TimingTestResult[] getResults() {
        return results;
    }

    public void setTasks(Runnable[] tasks) {
        this.tasks = tasks;
    }

    public boolean isUsingAtomicIterVar() {
        return usingAtomicIterVar;
    }

    public void setUsingAtomicIterVar(boolean usingAtomicIterVar, AtomicInteger atomicInteger) {
        this.atomicIterVar = atomicInteger;
        this.usingAtomicIterVar = usingAtomicIterVar;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.atomicIterVar = new AtomicInteger(startingValue);
        this.startingValue = startingValue;
    }

    public int getEndingValue() {
        return endingValue;
    }

    public void setEndingValue(int endingValue) {
        this.endingValue = endingValue;
    }

    @Override
    public String toString() {
        return "TimingsTest{" +
                "tasks=" + Arrays.toString(tasks) +
                ", startingValue=" + startingValue +
                ", endingValue=" + endingValue +
                ", testRunCountPerIteration=" + testRunCountPerIteration +
                ", atomicIterVar=" + atomicIterVar +
                ", usingAtomicIterVar=" + usingAtomicIterVar +
                ", results=" + results +
                '}';
    }
}
