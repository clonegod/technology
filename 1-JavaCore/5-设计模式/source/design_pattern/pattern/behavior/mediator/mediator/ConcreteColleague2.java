package mediator;

public class ConcreteColleague2 extends Colleague {

	public ConcreteColleague2(Mediator mediator) {
		super(mediator);
	}

	@Override
	public void action() {
		System.out.println(getClass().getName() + "... action running");
	}

}
