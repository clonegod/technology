package part1_basic

// 迭代rang
result = ''
(5..9).each{ ele ->
	result += ele
}
assert result == '56789'

assert (0..10).isCase(5)

// 按range匹配
age = 36
insuranceRate = 0
switch(age) {
	case 16..20: insuranceRate = 0.05; break
	case 21..50: insuranceRate = 0.06; break
	case 51..65: insuranceRate = 0.07; break
	default: throw new IllegalArgumentException()
}
assert insuranceRate == 0.06

// 按range过滤
ages = [20,35,42,56]
midage = 21..50
assert ages.grep(midage) == [35, 42]
