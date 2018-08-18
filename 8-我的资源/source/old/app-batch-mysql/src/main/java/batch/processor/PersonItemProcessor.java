package batch.processor;

import java.util.Random;

import org.springframework.batch.item.ItemProcessor;

import batch.model.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    @Override
    public Person process(final Person person) throws Exception {
    	if(new Random().nextInt(10)%2==0) {
    		throw new RuntimeException("PersonItemProcessor异常");
    	}
    	person.setFirstName(person.getFirstName()+"-"+System.currentTimeMillis());
        return person;
    }
}