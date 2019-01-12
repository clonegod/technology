package mediator;

public class ConcreteColleague1 extends Colleague {

	public ConcreteColleague1(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void action() {
		System.out.println(getClass().getName() + "... action running");
	}

}
