package com.javarush.task.task21.task2113;

import java.util.ArrayList;
import java.util.List;

public class Hippodrome {

    private List<Horse> horses;
    static Hippodrome game;

    public Hippodrome(List<Horse> horses) {
        this.horses = horses;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public void run() throws InterruptedException {
        for (int i = 1; i <=100 ; i++) {
            move();
            print();
            Thread.sleep(100);
        }
    }

    public void move(){
horses.forEach(Horse::move);
}
    

    public void print(){
horses.forEach(Horse::print);
        for (int i = 1; i <=10 ; i++) {
            System.out.println();

        }
    }
    public Horse getWinner(){
        Horse result = horses.get(0);
        for(Horse horse:horses){
            if(horse.getDistance() > result.getDistance()) {
                result = horse;
            }
        }
        return result;
    }
    public void printWinner(){
        System.out.println("Winner is " + getWinner().getName() + "!");
    }

    public static void main(String[] args) throws InterruptedException {
        List<Horse> horses = new ArrayList<>();
        Horse one = new Horse("One",3,0);
        Horse two = new Horse("Two",3,0);
        Horse three = new Horse("Three",3,0);
        horses.add(one);
        horses.add(two);
        horses.add(three);
        game = new Hippodrome(horses);
        game.run();
        game.printWinner();
    }
}
