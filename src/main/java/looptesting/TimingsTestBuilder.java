package looptesting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class TimingsTestBuilder {

    private Runnable[] tasks;
    private String[] taskNames;
    private boolean useAtomicVariable;
    private AtomicInteger atomicIterVar;
    private int startingValue;
    private int endingValue;
    private boolean endingValueSet;

    public TimingsTestBuilder(@NotNull Runnable... tasks) {
        this.tasks = tasks;
    }

    public TimingsTestBuilder setTasks(Runnable[] tasks) {
        this.tasks = tasks;
        return this;
    }

    public TimingsTestBuilder nameTests(String... taskNames) {
        this.taskNames = taskNames;
        return this;
    }

    public TimingsTestBuilder setLoopStartingValue(int val) {
        startingValue = val;
        return this;
    }

    public TimingsTestBuilder setLoopEndingValue(int val) {
        endingValue = val;
        endingValueSet = true;
        return this;
    }

    public TimingsTestBuilder useAtomicIteratorVariable(@Nullable AtomicInteger atomicInteger) {
        atomicIterVar = atomicInteger;
        useAtomicVariable = atomicIterVar != null;
        return this;
    }

    public TimingsTest build() {
        assert tasks.length != 0;

        TimingsTest createdTest = new TimingsTest();
        createdTest.setTasks(tasks);
        createdTest.setTaskNames(taskNames);
        createdTest.setUsingAtomicIterVar(useAtomicVariable, atomicIterVar);
        createdTest.setStartingValue(startingValue);
        createdTest.setEndingValue(endingValueSet ? endingValue : 100);

        System.out.printf("""
                New test looks like:
                
                long start;
                long end;
                for(int i = %d; i < %d; i++) {
                        
                    start = System.nanoTime();
                    %s
                    end = System.nanoTime();
                    task1Timings.add(end - start);
                        
                    other tasks...
                }
                """, startingValue, endingValue, tasks[0].toString());

        System.out.println(createdTest);
        return createdTest;
    }
}
