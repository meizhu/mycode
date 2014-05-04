package com.mei.scalastudy.casepattern

object Expression {
  def main(args: Array[String]) {
    val v = Variable("v");
    println("Variable type: " + v.toString());

    val op = BinOperator("+", Number(4.0), v)
    println("Bin opertor: " + op)
  }
}

abstract class Expr
case class Variable(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOperator(operator: String, arg: Expr) extends Expr
case class BinOperator(operator: String, left: Expr, right: Expr) extends Expr

