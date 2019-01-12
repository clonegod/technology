package part1_basic


def mon = new Weekday('Mon')
def fri = new Weekday('Fri')

def worklog = ''

// customer range: weekdays
(mon..fri).each {
	worklog += 	it.toString() + " "
}

assert worklog == 'Mon Tue Wed Thu Fri '
