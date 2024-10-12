package com.dkatalis;

import java.util.HashMap;
import java.util.Scanner;

class Customer {
    private String name;
    private double balance;
    private double debt;

    public Customer(String name) {
        this.name = name;
        this.balance = 0;
        this.debt = 0;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public double getDebt() {
        return debt;
    }

    public void deposit(double amount) {
        if (debt > 0) {
            if (amount >= debt) {
                amount -= debt;
                System.out.println("Transferred $" + debt + " to settle debts.");
                debt = 0;
            } else {
                debt -= amount;
                System.out.println("Transferred $" + amount + " to settle debts.");
                return;
            }
        }
        balance += amount;
        System.out.println("Your balance is $" + balance);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Your balance is $" + balance);
        } else {
            System.out.println("Insufficient balance. Your balance is $" + balance);
        }
    }

    public void transfer(Customer target, double amount) {
        if (balance >= amount) {
            balance -= amount;
            target.receiveTransfer(amount);
            System.out.println("Transferred $" + amount + " to " + target.getName());
        } else {
            double transferred = balance;
            target.receiveTransfer(transferred);
            target.increaseDebt(amount - transferred);
            debt += (amount - transferred);
            balance = 0;
            System.out.println("Transferred $" + transferred + " to " + target.getName());
            System.out.println("Owed $" + debt + " to " + target.getName());
        }
        System.out.println("Your balance is $" + balance);
    }

    private void receiveTransfer(double amount) {
        balance += amount;
    }

    private void increaseDebt(double amount) {
        debt += amount;
    }
}

public class ATM {
    private static HashMap<String, Customer> customers = new HashMap<>();
    private static Customer currentCustomer = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Welcome to the ATM!");

        while (true) {
            command = scanner.nextLine().trim();
            String[] tokens = command.split(" ");

            switch (tokens[0]) {
                case "login":
                    login(tokens[1]);
                    break;
                case "deposit":
                    deposit(Double.parseDouble(tokens[1]));
                    break;
                case "withdraw":
                    withdraw(Double.parseDouble(tokens[1]));
                    break;
                case "transfer":
                    transfer(tokens[1], Double.parseDouble(tokens[2]));
                    break;
                case "logout":
                    logout();
                    break;
                case "exit":
                    System.out.println("Exiting the ATM. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void login(String name) {
        if (!customers.containsKey(name)) {
            customers.put(name, new Customer(name));
        }
        currentCustomer = customers.get(name);
        System.out.println("Hello, " + currentCustomer.getName() + "!");
        System.out.println("Your balance is $" + currentCustomer.getBalance());
    }

    private static void deposit(double amount) {
        if (currentCustomer == null) {
            System.out.println("Please login first.");
            return;
        }
        currentCustomer.deposit(amount);
    }

    private static void withdraw(double amount) {
        if (currentCustomer == null) {
            System.out.println("Please login first.");
            return;
        }
        currentCustomer.withdraw(amount);
    }

    private static void transfer(String targetName, double amount) {
        if (currentCustomer == null) {
            System.out.println("Please login first.");
            return;
        }
        if (!customers.containsKey(targetName)) {
            System.out.println("Customer " + targetName + " does not exist.");
            return;
        }
        Customer target = customers.get(targetName);
        currentCustomer.transfer(target, amount);
    }

    private static void logout() {
        if (currentCustomer == null) {
            System.out.println("No customer is logged in.");
        } else {
            System.out.println("Goodbye, " + currentCustomer.getName() + "!");
            currentCustomer = null;
        }
    }
}