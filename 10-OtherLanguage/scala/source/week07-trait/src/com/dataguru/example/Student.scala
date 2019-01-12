package com.dataguru.example

class Student {
  // 限制只有com.dataguru下的子包可以访问
  protected[example] def takeExam() = { println("taking exam: " + name) }
  
}