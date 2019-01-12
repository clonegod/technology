package part2_oop.grovvybean

def bean = new MrBean(firstname: 'Rowan') // generic constructor
bean.lastname = 'Atkinson' // call setter

assert 'Rowan Atkinson' == bean.name