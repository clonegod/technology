package part2_oop.grovvybean

def bean = new MyBean()

assert 'default value' == bean.getAssigned()

bean.setUntyped('some value')
assert 'some value' == bean.getUntyped()

bean = new MyBean(typed: 'another value')
assert 'another value' == bean.getTyped()

