package it.unibo.oop.lab.workers02;

import java.util.*;
import java.util.Arrays;

public class MultiThreadedSumMatrixClassic implements SumMatrix{
    private final int nthread;

    //construct a new multithreaded matrix sum
    public MultiThreadedSumMatrixClassic(final int nthread) {
        super();
        if(nthread<1) {
            throw new IllegalArgumentException();
        }
        this.nthread = nthread;
    }
    
    public class Worker extends Thread{
        private final double[][] matrix;
        private final int startPos;
        private final int nelem;
        private double res;
         
        Worker(final double [][] matrix, final int startPos, final int nelem){
            super();
            this.matrix = Arrays.copyOf(matrix, matrix.length);
            this.startPos = startPos;
            this.nelem = nelem;
        }
        @Override
        public void run() {
            for(int i = startPos; i < matrix.length && i < startPos + nelem; i++) {
                for(final double d : this.matrix[i]) {
                    this.res += d;
                }
            }
        }
        public double getResult() {
            return this.res;
        }
        
    }
    
    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for(int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        
        for(final Thread worker: workers) {
            worker.start();
        }
        double sum = 0;
        for(final Worker worker: workers) {
            try {
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }
   

}
