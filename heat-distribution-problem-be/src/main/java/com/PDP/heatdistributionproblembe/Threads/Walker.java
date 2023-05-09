package com.PDP.heatdistributionproblembe.Threads;

import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class Walker implements Runnable {

    private final ReentrantLock chooseEmptyElementLock;

    private final ReentrantLock writeMutex;

    private final Double[][] heatmap;

    private final String function;

    private final Integer steps;

    @Override
    public void run() {
        Pair<Integer, Integer> initialIndex = chooseEmptyElement(heatmap);
        populateValueAndGoToNeighbour(heatmap, function, initialIndex, steps);
    }

    private Pair<Integer, Integer> chooseEmptyElement(Double[][] heatmap) {
        Random random = new Random();
        Integer rowIdx = random.nextInt(heatmap.length);
        Integer colIdx = random.nextInt(heatmap.length);

        chooseEmptyElementLock.lock();
        if (heatmap[rowIdx][colIdx] != null) {
            chooseEmptyElementLock.unlock();
            return chooseEmptyElement(heatmap);
        }
        chooseEmptyElementLock.unlock();

        return new Pair(rowIdx, colIdx);
    }

    private void populateValueAndGoToNeighbour(Double[][] heatmap,
                                               String function,
                                               Pair<Integer, Integer> currentElement,
                                               Integer steps) {

        if (steps == 0 || !validateNextStep(heatmap.length, currentElement)) {
            return;
        }

        Integer rowIndex = currentElement.getValue0();
        Integer colIndex = currentElement.getValue1();

        heatmap[rowIndex][colIndex] = calculateInputFunction(function, currentElement);

        ArrayList<Integer> attemptedDirections = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        writeMutex.lock();
        Pair<Integer, Integer> nextBlock = walkNextDirection(currentElement, attemptedDirections, heatmap);
        writeMutex.unlock();
        populateValueAndGoToNeighbour(heatmap, function, nextBlock, steps - 1);
    }

    private double calculateInputFunction(String function, Pair<Integer, Integer> currentElement) {
        Function f = new Function("f(x,y) = " + function);

        Argument x = new Argument("x", currentElement.getValue0());
        Argument y = new Argument("y", currentElement.getValue1());

        Expression exp = new Expression("f(x,y)", f, x, y);
        return exp.calculate();
    }

    private boolean validateNextStep(Integer heatmapSize, Pair<Integer, Integer> nextStep)
            throws ArrayIndexOutOfBoundsException {
        boolean isValid = false;

        if (Math.min(nextStep.getValue0(), nextStep.getValue1()) >= 0
                && Math.max(nextStep.getValue0(), nextStep.getValue1()) < heatmapSize) {
            isValid = true;
        }

        return isValid;
    }

    private Pair<Integer, Integer> walkNextDirection(Pair<Integer, Integer> currentElement,
                                                     ArrayList<Integer> attempted,
                                                     Double[][] heatmap) {

        Random random = new Random();

        Integer randomGenerator = attempted.size() > 0
                ? random.nextInt(attempted.size())
                : -1;

        if (randomGenerator == -1) {
            return new Pair<>(-1, -1);
        }

        Integer nextStep = attempted.get(randomGenerator);
        Pair<Integer, Integer> nextBlock = getNextStepIndex(currentElement, nextStep);

        return validateNextBlock(nextBlock, attempted, heatmap, nextStep)
                ? nextBlock
                : walkNextDirection(currentElement, attempted, heatmap);
    }

    private boolean validateNextBlock(Pair<Integer, Integer> nextBlock,
                                      ArrayList<Integer> attempted,
                                      Double[][] heatmap,
                                      Integer nextStep) {

        boolean isValid;

        if (validateNextStep(heatmap.length, nextBlock)) {
            if(heatmap[nextBlock.getValue0()][nextBlock.getValue1()] == null) {
                isValid = true;
            } else {
                attempted.remove(Integer.valueOf(nextStep));
                isValid = false;
            }
        } else {
            attempted.remove(Integer.valueOf(nextStep));
            isValid = false;
        }

        return isValid;
    }

    private Pair<Integer, Integer> getNextStepIndex(Pair<Integer, Integer> currentValue, Integer nextDirection) {
        return switch (nextDirection) {
            case 0 ->
                    // top
                    new Pair<>(currentValue.getValue0() - 1, currentValue.getValue1());
            case 1 ->
                    // bottom
                    new Pair<>(currentValue.getValue0() + 1, currentValue.getValue1());
            case 2 ->
                    // left
                    new Pair<>(currentValue.getValue0(), currentValue.getValue1() - 1);
            case 3 ->
                    // right
                    new Pair<>(currentValue.getValue0(), currentValue.getValue1() + 1);
            default ->
                    new Pair<>(-1, -1);
        };
    }

}
