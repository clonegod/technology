package part3_mop.methodinterceptor

class WhatEver extends Traceable {
	
	int outer() {
		return inner()
	}
	
	int inner() {
		return 1
	}
}
