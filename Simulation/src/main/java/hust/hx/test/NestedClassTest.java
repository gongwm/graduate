package hust.hx.test;

public class NestedClassTest {
	public static void main(String[] args) {
		Sub sub = new Sub();
		Sub.Inner1 si1 = sub.new Inner1();
		si1.f();
		Sub.Inner2 si2=sub.new Inner2();
		si2.f();
		Sub.Inner2Sub si2s=sub.new Inner2Sub();
		si2s.f();
	}
}

class Outer {
	void f() {
		System.out.println("outer");
	}
	
	class Inner{
		void f(){
			System.out.println("inner in outer");
		}
	}

	class Inner1 {
		void f() {
			System.out.println("inner1 in outer");
		}
	}

	class Inner2 {
		void f() {
			System.out.println("inner2 in outer");
		}
	}

}

class Sub extends Outer {
	class Inner1 {// override
		void f() {
			new Outer.Inner1().f();
			System.out.println("inner1 in Sub");
		}
	}

	class Inner2Sub extends Inner2 {// extends
		void f() {
			System.out.println("inner2 in sub:extends another inner-class inheritted by Sub");
		}
	}
}
