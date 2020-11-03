/**
Take as an input an Normal Polish Notation (NPN) statement and prints the result
Example:  x + 2 3 + 2 2 (result is 20)
We compute the result by storing the info in a binary tree and computing the answer in the same tree in a recursive way.

@Author: Fernando Ramirez de Aguilar Centeno
@Version 24/02/2020
*/

import java.util.Scanner;

public class NPN {
	private Tree binTree = new Tree();

	public static void main(String[] args) {
		NPN sub = new NPN();
		sub.mainProcess();
	}

	private void mainProcess() {
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			binTree.insertValue(in.next());
		}

		System.out.println(binTree.calculate());
	}


	private class Tree {
		private class Node {
			Node left = null;
			Node right = null;
			String value;
			boolean empty = true; //True when thr node contains a value
			boolean isSign = false; //True when the node stores an operator (+, -, x)
			boolean full = false; //True when there are no children available to store values.


			/**
			Return the node value
			*/
			public String getValue() {
				return value;
			}

			/**
			Return true if the value stored is a sign (operand)
			Returns false if there is a number or nothing stored
			*/
			public boolean getSign() {
				return isSign;
			}

			/**
			returns true if there is no value stored in the node
			*/
			public boolean isEmpty() {
				return empty;
			}

			/**
			Set a node to full following the rules.
			The rules:
				A node is full when it doesn't contain left or right children (==null)
				A node is full if its 2 children are full
			*/
			private void makeFull() {
				if(right == null || left == null) {
					full = true;
				}
				else if(right.isFull() && left.isFull()) {
					full = true;
				}
				else {
					full = false;
				}
			}

			/**
			Return true if the node has 2 children which store a number value
			*/
			public boolean isFull() {
				return full;
			}

			/**
			Add a new child node for a new value stored
			Rules:
				A node can only have left and right children if the value is a sign operand
				A node storing a value (number) cannot have children
				The left child node must have something stored to store something in the right child node.
				A node is full when the left and right children have something stored
			Done recursively
			*/
			public void insert(String value){
				if(empty == true) {
					//Means that node parents are operands, impossible to have a value parent

					this.value = value;
					empty = false;

					if(value.equals("+") || value.equals("-") || value.equals("x")) {
						//only create children if node is an operand
						left = new Node();
						right = new Node();
						isSign = true;
					}
				}
				else if (isSign) {
					//This means we are a sign and we have children

					//We only check if it is full at the left node, the right should always be free if we manage to access this node
					if((left.isEmpty() || left.getSign()) && !left.isFull()) {
						left.insert(value);
					}
					else if(right.isEmpty() || right.getSign()) {
						right.insert(value);
					}					
				}
				makeFull();
			}

			public int calc() {
				if(!isSign) {
					return Integer.parseInt(value);
				}
				else {
					switch(value) {
						case "+" :
							return left.calc() + right.calc();
						case "-" :
							return left.calc() - right.calc();
						case "x" :
							return left.calc() * right.calc();
						default :
							throw new ArithmeticException("Error logic in calc");
						}
					}
				}
			}

		Node top = null;
		public void insertValue(String value) {
			if(top == null) {
				top = new Node();
				top.insert(value);
			}
			else if(top.isFull()) {
				//Check if the parent node is full (There is no more space to store stuff)
				//Triggered when the NPN is incorrect
            	throw new NullPointerException("Top is full");
        	}
			else {
				top.insert(value);
			}
		}

		public int calculate() {
			return top.calc();
		}
	}
}
