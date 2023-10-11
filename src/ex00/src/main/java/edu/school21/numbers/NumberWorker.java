package edu.school21.numbers;

public class NumberWorker {

    public  boolean isPrime(int number) {
        boolean isPrime = true;
        if(number <= 1){
          throw new IllegalNumberException("The number should be positive integer more than one");
        }
        for (int i = 2; (i < Math.sqrt(number) + 1) && isPrime; i++) {
            if ((number % i) == 0) {
                isPrime = false;
            }
        }
        return  isPrime;
    }

    public  int digitsSum(int number){
        int sum = 0;
        number = Math.abs(number);
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }
}

