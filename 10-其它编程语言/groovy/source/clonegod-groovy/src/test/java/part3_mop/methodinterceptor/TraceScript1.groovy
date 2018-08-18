package part3_mop.methodinterceptor

import org.codehaus.groovy.runtime.StringBufferWriter

def log = new StringBuffer()
def traceMe = new WhatEver(writer: new StringBufferWriter(log))

assert 1 == traceMe.outer()

println log.toString()
