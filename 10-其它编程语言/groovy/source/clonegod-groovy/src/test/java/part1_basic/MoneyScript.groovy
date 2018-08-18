package part1_basic

def buck =  new Money(1, 'USD')
assert buck
assert buck == new Money(1, 'USD')
assert buck + buck == new Money(2, 'USD')

assert buck + 1 == new Money(2, 'USD')

