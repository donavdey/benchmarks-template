package org.sample;

import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class Streams {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        static List<Integer> data;
        static {
            data = IntStream.range(0, 5000000).boxed().collect(Collectors.toList());
            Collections.shuffle(data);
        }
    }

    @Benchmark
    public Set<Integer> sequentialCollect(BenchmarkState state) {
        return state.data.stream()
                .map(x -> x * 2)
                .collect(Collectors.toSet());

    }

    @Benchmark
    public Set<Integer> parallelCollect(BenchmarkState state) {
        return state.data.stream()
                .unordered().parallel()
                .map(x -> x * 2)
                .collect(Collectors.toSet());

    }

    @Benchmark
    public Set<Integer> parallelOrderedCollect(BenchmarkState state) {
        return state.data.parallelStream()
                .map(x -> x * 2)
                .collect(Collectors.toSet());

    }


    @Benchmark
    public Integer sequentialReduce(BenchmarkState state) {
        return state.data.stream()
                .map(x -> x * 2)
                .reduce(0, Integer::sum);

    }

    @Benchmark
    public Integer parallelReduce(BenchmarkState state) {
        return state.data.stream()
                .unordered().parallel()
                .map(x -> x * 2)
                .reduce(0, Integer::sum);

    }

    @Benchmark
    public Integer parallelOrderedReduce(BenchmarkState state) {
        return state.data.parallelStream()
                .map(x -> x * 2)
                .reduce(0, Integer::sum);

    }

    @Benchmark
    public Integer sequentialMax(BenchmarkState state) {
        return state.data.stream()
                .map(x -> x * 2)
                .max(Integer::compareTo)
                .get();

    }

    @Benchmark
    public Integer parallelMax(BenchmarkState state) {
        return state.data.stream()
                .unordered().parallel()
                .map(x -> x * 2)
                .max(Integer::compareTo)
                .get();

    }

    @Benchmark
    public Integer parallelOrderedMax(BenchmarkState state) {
        return state.data.parallelStream()
                .map(x -> x * 2)
                .max(Integer::compareTo)
                .get();

    }
}
